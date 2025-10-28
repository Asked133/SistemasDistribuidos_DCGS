
using PokedexApi.Dtos;
using PokedexApi.Infrastructure.Soap.Dtos;
using PokedexApi.Models;

namespace PokedexApi.Mappers;

public static class PokemonMapper
{


    public static Pokemon ToModel(this UpdatePokemonRequest updatePokemonRequest, Guid id)
    {
        return new Pokemon
        {
            Id = id,
            Name = updatePokemonRequest.Name,
            Type = updatePokemonRequest.Type,
            Stats = new Stats
            {
                Attack = updatePokemonRequest.Stats.Attack,
                Defense = updatePokemonRequest.Stats.Defense,
                Speed = updatePokemonRequest.Stats.Speed,
                HP = updatePokemonRequest.Stats.HP
            }
        };
    }
    public static Pokemon ToModel(this PokemonResponseDto pokemonResposeDto)
    {
        return new Pokemon
        {
            Id = pokemonResposeDto.Id,
            Name = pokemonResposeDto.Name,
            Type = pokemonResposeDto.Type,
            Level = pokemonResposeDto.Level,
            Stats = new Stats
            {
                Attack = pokemonResposeDto.Stats.Attack,
                Defense = pokemonResposeDto.Stats.Defense,
                Speed = pokemonResposeDto.Stats.Speed,
                HP = pokemonResposeDto.Stats.HP
            }
        };
    }
    public static IList<Pokemon> ToModel(this IList<PokemonResponseDto> pokemonResposeDtos)
    {
        return pokemonResposeDtos.Select(s => s.ToModel()).ToList();
    }

    public static PokemonResponse ToResponse(this Pokemon pokemon)
    {
        return new PokemonResponse
        {
            Id = pokemon.Id,
            Name = pokemon.Name,
            Type = pokemon.Type,
            Attack = pokemon.Stats.Attack
        };
    }
    public static Pokemon ToModel(this CreatePokemonRequest createPokemonRequest)
    {
        return new Pokemon
        {
            Name = createPokemonRequest.Name,
            Type = createPokemonRequest.Type,
            Level = createPokemonRequest.Level,
            Stats = new Stats
            {
                Attack = createPokemonRequest.Stats.Attack,
                Defense = createPokemonRequest.Stats.Defense,
                Speed = createPokemonRequest.Stats.Speed,
                HP = createPokemonRequest.Stats.HP
            }
        };
    }
    public static CreatePokemonDto ToRequest(this Pokemon pokemon)
    {
        return new CreatePokemonDto
        {
            Name = pokemon.Name,
            Type = pokemon.Type,
            Level = pokemon.Level,
            Stats = new StatsDto
            {
                Attack = pokemon.Stats.Attack,
                Defense = pokemon.Stats.Defense,
                Speed = pokemon.Stats.Speed,
                HP = pokemon.Stats.HP
            }
        };
    }

    public static UpdatePokemonDto ToUpdateRequest(this Pokemon pokemon)
    {
        return new UpdatePokemonDto
        {
            Id = pokemon.Id,
            Name = pokemon.Name,
            Type = pokemon.Type,
            Stats = new StatsDto
            {
                Attack = pokemon.Stats.Attack,
                Defense = pokemon.Stats.Defense,
                Speed = pokemon.Stats.Speed,
                HP = pokemon.Stats.HP
            }
        };
    }

    public static IList<PokemonResponse> ToResponse(this IList<Pokemon> pokemons)
    {
        return pokemons.Select(s => s.ToResponse()).ToList();
    }
}