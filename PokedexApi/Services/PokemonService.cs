using PokedexApi.Exceptions;
using PokedexApi.Gateways;
using PokedexApi.Models;
using PokedexApi.Dtos;
using PokedexApi.Mappers;

namespace PokedexApi.Services;

public class PokemonService : IPokemonService
{
    private readonly IPokemonGateway _pokemonGateway;

    public PokemonService(IPokemonGateway pokemonGateway)
    {
        _pokemonGateway = pokemonGateway;
    }

    public async Task DeletePokemonAsync(Guid id, CancellationToken cancellationToken)
    {
        await _pokemonGateway.DeletePokemonAsync(id, cancellationToken);
    }

    public async Task<Pokemon> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        return await _pokemonGateway.GetPokemonByIdAsync(id, cancellationToken);
    }

    public async Task<PagedResponse<PokemonResponse>> GetPokemonsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken)
    {
        var (pokemons, totalRecords) = await _pokemonGateway.GetPokemonsAsync(name, type, pageNumber, pageSize, orderBy, orderDirection, cancellationToken);
        return PagedResponse<PokemonResponse>.Create(pokemons.ToResponse(), totalRecords, pageNumber, pageSize);
    }

public async Task<Pokemon> CreatePokemonAsync(Pokemon pokemon, CancellationToken cancellationToken)
{
    var pokemons = await _pokemonGateway.GetPokemonsByNameAsync(pokemon.Name, cancellationToken);
    if (PokemonExists(pokemons, pokemon.Name))
    {
            throw new PokemonAlreadyExistsException(pokemon.Name);
        }

        return await _pokemonGateway.CreatePokemonAsync(pokemon, cancellationToken);
    }

    private static bool PokemonExists(IList<Pokemon> pokemons, string PokemonNameToSearch)
    {
        return pokemons.Any(s => s.Name.ToLower().Equals(PokemonNameToSearch.ToLower()));
    }
}

