using Microsoft.EntityFrameworkCore;
using MobApi.Infrastructure;
using MobApi.Repositories;
using MobApi.Services;
using SoapCore;


var builder = WebApplication.CreateBuilder(args); // preparar todo el aplicativo para levantar una app web
builder.Services.AddSoapCore();
builder.Services.AddScoped<IMobRepository, MobRepository>();
builder.Services.AddScoped<IMobService, MobService>();


builder.Services.AddDbContext<RelationalDbContext>(options =>
    options.UseMySql(builder.Configuration.GetConnectionString("DefaultConnection"),
    ServerVersion.AutoDetect(builder.Configuration.GetConnectionString("DefaultConnection"))));

var app = builder.Build();
app.UseSoapEndpoint<IMobService>("/MobService.svc", new SoapEncoderOptions());
app.Run();