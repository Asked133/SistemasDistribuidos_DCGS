using System.ServiceModel;
using PokedexApi.Infrastructure.Soap.Contracts;
using PokedexApi.Models;
using PokedexApi.Mappers;
using PokedexApi.Exceptions;

namespace PokedexApi.Gateways;

public class PokemonGateway : IPokemonGateway
{
    private readonly IPokemonContract _pokemonContract;
    private readonly ILogger<PokemonGateway> _logger;

    public PokemonGateway(IConfiguration configuration, ILogger<PokemonGateway> logger)
    {
        var binding = new BasicHttpBinding();
        var endpoint = new EndpointAddress(configuration.GetValue<string>("PokemonService:Url"));
        _pokemonContract = new ChannelFactory<IPokemonContract>(binding, endpoint).CreateChannel();
        _logger = logger;
    }public async Task<(IList<Pokemon> pokemons, int totalRecords)> GetPokemonsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken)
    {

        var request = new GetPokemonsRequestDto
        {
            Name = name,
            Type = type,
            PageNumber = pageNumber,
            PageSize = pageSize,
            OrderBy = orderBy,
            OrderDirection = orderDirection
        };

        var response = await _pokemonContract.GetPokemonsAsync(request);

  
        var pokemons = response.Pokemons.ToModel();
        return (pokemons, response.TotalRecords);
    }
    public async Task DeletePokemonAsync(Guid id, CancellationToken cancellationToken)
    {
        try
        {
            await _pokemonContract.DeletePokemon(id, cancellationToken);
        }
        catch (FaultException ex) when (ex.Message == "Pokemon not found")
        {
            _logger.LogWarning(ex, "Pokemon not found.");
            throw new PokemonNotFoundException(id);
        }
    }
    public async Task<Pokemon> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        try
        {
            var pokemon = await _pokemonContract.GetPokemonById(id, cancellationToken);
            return pokemon.ToModel();
        }
        catch (FaultException ex) when (ex.Message == "Pokemon not found")
        {
            _logger.LogWarning("Pokemon not found.");
            return null;
        }
    }
    public async Task<IList<Pokemon>> GetPokemonsByNameAsync(string name, CancellationToken cancellationToken)
    {
        _logger.LogDebug(":(");
        var pokemons = await _pokemonContract.GetPokemonsByName(name, cancellationToken);
        return pokemons.ToModel();
    }
    public async Task<Pokemon> CreatePokemonAsync(Pokemon pokemon, CancellationToken cancellationToken)
    {
        try
        {
            _logger.LogInformation("Sending request to SOAP API, with pokemon: {name}", pokemon.Name);
            var createdPokemon = await _pokemonContract.CreatePokemon(pokemon.ToRequest(), cancellationToken);
            return createdPokemon.ToModel();
        }
        catch (FaultException ex) when (ex.Message.Contains("already exists"))
        {
            _logger.LogWarning("Pokemon already exists: {name}", pokemon.Name);
            throw new PokemonAlreadyExistsException(pokemon.Name);
        }
        catch (Exception e)
        {
            _logger.LogError(e, "Error creating pokemon via SOAP: {message}", e.Message);
            throw; // Re-lanzar la excepción en lugar de retornar null
        }
    }
        
        

        //Fatal
        //Error
        //Warning
        //Info
        //Debug

        //LogLevel = Information    
}
