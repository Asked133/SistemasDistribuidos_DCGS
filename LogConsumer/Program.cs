using Confluent.Kafka;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

var builder = new ConfigurationBuilder()
    .SetBasePath(Directory.GetCurrentDirectory())
    .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
    .AddEnvironmentVariables();


var configuration = builder.Build();

var loggerFactory = LoggerFactory.Create(loggingBuilder =>
{
    loggingBuilder.AddConsole();
    loggingBuilder.SetMinimumLevel(LogLevel.Information);
});

var logger = loggerFactory.CreateLogger("KafkaLogConsumer");

logger.LogInformation("Kafka Log Consumer started.");

var consumerConfig = new Confluent.Kafka.ConsumerConfig
{
    BootstrapServers = configuration.GetValue<string>("Kafka:BootstrapServers"),
    GroupId = configuration.GetValue<string>("Kafka:GroupId"),
    AutoOffsetReset = AutoOffsetReset.Earliest
};

LogLevel MapKafkaLogLevel(Confluent.Kafka.SyslogLevel kafkaLevel)
{
    return kafkaLevel switch
    {   
        Confluent.Kafka.SyslogLevel.Debug => LogLevel.Debug,
        Confluent.Kafka.SyslogLevel.Info => LogLevel.Information,
        Confluent.Kafka.SyslogLevel.Warning => LogLevel.Warning,
        Confluent.Kafka.SyslogLevel.Error => LogLevel.Error,
        _ => LogLevel.Trace,
    };
}

using var consumer = new ConsumerBuilder<Ignore, string>(consumerConfig).SetLogHandler((_, logMessage) =>
{
    logger.Log(MapKafkaLogLevel(logMessage.Level), logMessage.Message);
}).Build();

var topics = configuration.GetSection("Kafka:Topics").Get<string[]>();

logger.LogInformation("Subscribing to topics: {Topics}", string.Join(", ", topics));

consumer.Subscribe(topics);

try
{
    while (true)
    {
        var consumeResult = consumer.Consume();

        logger.LogInformation("[{Topic}] Message: {Message}", 
            consumeResult.Topic, 
            consumeResult.Message.Value);
    }
}
catch (OperationCanceledException)
{
    logger.LogError("Kafka log consumer stopped.");
    consumer.Close();
}