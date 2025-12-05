using Confluent.Kafka;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

var builder = new ConfigurationBuilder()
    .SetBasePath(Directory.GetCurrentDirectory())
    .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
    .AddEnvironmentVariables();

var configuration = builder.Build();

var serviceCollection = new ServiceCollection();
serviceCollection.AddSingleton<IConfiguration>(configuration);
serviceCollection.AddSingleton<EmailService>();
serviceCollection.AddLogging(builder =>
{
    builder.AddConsole();
    builder.SetMinimumLevel(LogLevel.Information);
});


var serviceProvider = serviceCollection.BuildServiceProvider();
var logger = serviceProvider.GetRequiredService<ILogger<Program>>();
var emailService = serviceProvider.GetRequiredService<EmailService>();

logger.LogInformation("Starting Kafka Log Consumer");

var consumeConfig = new ConsumerConfig
{
    BootstrapServers = configuration.GetValue<string>("Kafka:BootstrapServers"),
    GroupId = configuration.GetValue<string>("Kafka:GroupId"),
    AutoOffsetReset = AutoOffsetReset.Earliest
};

LogLevel MapKafkaLogLevel(Confluent.Kafka.SyslogLevel kafkalevel)
{
    return kafkalevel switch
    {
    Confluent.Kafka.SyslogLevel.Error => LogLevel.Error,
    Confluent.Kafka.SyslogLevel.Warning => LogLevel.Warning,
    Confluent.Kafka.SyslogLevel.Info => LogLevel.Information,
    Confluent.Kafka.SyslogLevel.Debug => LogLevel.Debug,
    _ => LogLevel. Trace,
    };
};

using var consumer = new ConsumerBuilder<Ignore, string>(consumeConfig).SetLogHandler((_, logMessage) =>
{
    logger.Log(MapKafkaLogLevel(logMessage.Level), logMessage.Message);
}).Build();

var topics = configuration.GetSection("Kafka:Topics").Get<string[]>();

logger.LogInformation("Subscribing to topics: {topics}", string.Join(", ", topics));

consumer.Subscribe(topics);

try {
    while(true) {
        var consumeResult = consumer.Consume();
        logger.LogInformation("[{Topic}] Message: {Message}", consumeResult.Topic, consumeResult.Message.Value);
        try
        {
         dynamic data = JsonConvert.DeserializeObject(consumeResult.Message.Value);
          switch (consumeResult.Topic)
          {
            case "trainer.created":
                // Espera correctamente la tarea asíncrona
                await emailService.SendWelcomeEmailAsync("dgomez50@alumnos.uaq.mx", (string)data.name);
                break;
            default:
                logger.LogWarning("No handler for topic: {Topic}", consumeResult.Topic);
                break;
          }
        } catch (Exception ex)
        {
            logger.LogError(ex, "Error processing message: {Message}", consumeResult.Message.Value);
        }
    }

} catch (OperationCanceledException) {
    consumer.Close();
    logger.LogError("Kafka Log Consumer stopped.");
}