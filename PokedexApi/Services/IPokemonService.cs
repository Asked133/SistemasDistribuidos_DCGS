namespace PokedexApi.Services;
using PokedexApi.Models;
using PokedexApi.Dtos;

public interface IPokemonService
{
    Task<Pokemon> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken);
    Task<Pokemon> CreatePokemonAsync(Pokemon pokemon, CancellationToken cancellationToken);
    Task<PagedResponse<PokemonResponse>> GetPokemonsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken);
    Task DeletePokemonAsync(Guid id, CancellationToken cancellationToken);
}