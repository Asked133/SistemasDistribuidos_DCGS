using Microsoft.EntityFrameworkCore;
using PokemonApi.Infrastructure;
using PokemonApi.Repositories;
using PokemonApi.Services;
using SoapCore;


var builder = WebApplication.CreateBuilder(args); // preparar todo el aplicativo para levantar una app web
builder.Services.AddSoapCore();
builder.Services.AddScoped<IPokemonRepository, PokemonRepository>();
builder.Services.AddScoped<IPokemonServices, PokemonService>();


builder.Services.AddDbContext<RelationalDBContext>(options =>
    options.UseMySql(builder.Configuration.GetConnectionString("DefaultConnection"),
    ServerVersion.AutoDetect(builder.Configuration.GetConnectionString("DefaultConnection"))));

var app = builder.Build();
app.UseSoapEndpoint<IPokemonServices>("/PokemonService.svc", new SoapEncoderOptions());
app.Run();