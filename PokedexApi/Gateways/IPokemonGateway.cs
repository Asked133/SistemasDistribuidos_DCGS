using PokedexApi.Models;

namespace PokedexApi.Gateways;

//Como si fuera un repositorio
//Clean Architecture y a Hexagonal Architecture
public interface IPokemonGateway
{
    Task<Pokemon> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken);
    Task<IList<Pokemon>> GetPokemonsByNameAsync(string name, CancellationToken cancellationToken);
    Task<Pokemon> CreatePokemonAsync(Pokemon pokemon, CancellationToken cancellationToken);
    Task DeletePokemonAsync(Guid id, CancellationToken cancellationToken);
    Task<(IList<Pokemon> pokemons, int totalRecords)> GetPokemonsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken);
}