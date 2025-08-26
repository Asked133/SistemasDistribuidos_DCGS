using Microsoft.EntityFrameworkCore;
using PokemonApi.Infraestructure;
using PokemonApi.Services;
using SoapCore;


var builder = WebApplication.CreateBuilder(args);
builder.Services.AddSoapCore();
builder.Services.AddSingleton<IPokemonService, PokemonService>();

builder.Services.AddDbContext<RelationalDBContext>(options =>
options.UseMySql(builder.Configuration.GetConnectionString("DefaultConnection"),
    ServerVersion.AutoDetect(builder.Configuration.GetConnectionString("DefaultConnection"))));

var app = builder.Build();
    app.UseSoapEndpoint<IPokemonService>("/PokemonService.svc", new SoapEncoderOptions());
    app.Run();