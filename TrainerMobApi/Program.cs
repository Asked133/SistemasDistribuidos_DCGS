using Microsoft.Extensions.Options;
using MongoDB.Driver;
using TrainerMobApi.Infrastructure;
using TrainerMobApi.Repositories;
using TrainerMobApi.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.Configure<MongoDBSettings>(
    builder.Configuration.GetSection("MongoDB"));

builder.Services.AddSingleton<IMongoDatabase>(sp =>
{
    var settings = sp.GetRequiredService<IOptions<MongoDBSettings>>().Value;
    var client = new MongoClient(settings.ConnectionString);
    return client.GetDatabase(settings.DatabaseName);
});

builder.Services.AddScoped<IMobRepository, MobRepository>();

builder.Services.AddGrpc();

var app = builder.Build();

app.MapGrpcService<MobService>();

app.MapGet("/", () => "TrainerMobApi gRPC service is running.");

app.Run();