using MobedexApi.Models;

namespace MobedexApi.Gateways;

//Como si fuera un repositorio
//Clean Architecture y a Hexagonal Architecture
public interface IMobGateway
{
    Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken);
    Task<IList<Mob>> GetMobsByNameAsync(string name, CancellationToken cancellationToken);
    Task<Mob> CreateMobAsync(Mob mob, CancellationToken cancellationToken);
    Task DeleteMobAsync(Guid id, CancellationToken cancellationToken);
    Task<(IList<Mob> mobs, int totalRecords)> GetMobsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken);
    Task<Mob> UpdateMobAsync(Mob mob, CancellationToken cancellationToken);
}