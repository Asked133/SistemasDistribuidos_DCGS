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

    public async Task<Pokemon> PatchPokemonAsync(Guid id, string? name, string? type, int? attack, int? defense, int? speed, int? HP, CancellationToken cancellationToken)
    {
        var pokemon = await _pokemonGateway.GetPokemonByIdAsync(id, cancellationToken);
        if (pokemon == null)
        {
            throw new PokemonNotFoundException(id);
        }
        pokemon.Name = name ?? pokemon.Name;
        pokemon.Type = type ?? pokemon.Type;
        pokemon.Stats.Attack = attack ?? pokemon.Stats.Attack;
        pokemon.Stats.Defense = defense ?? pokemon.Stats.Defense;
        pokemon.Stats.Speed = speed ?? pokemon.Stats.Speed;
        pokemon.Stats.HP = HP ?? pokemon.Stats.HP;

        await _pokemonGateway.UpdatePokemonAsync(pokemon, cancellationToken);
        return pokemon;
    }
    

    public async Task<Pokemon> UpdatePokemonAsync(Pokemon pokemon, CancellationToken cancellationToken)
    {
        return await _pokemonGateway.UpdatePokemonAsync(pokemon, cancellationToken);
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

