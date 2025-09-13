namespace PokedexApi.Services;
using PokedexApi.Models;

public interface IPokemonService
{
    Task<Pokemon> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken);
    Task<Pokemon> CreatePokemonAsync(Pokemon pokemon, CancellationToken cancellationToken);
}