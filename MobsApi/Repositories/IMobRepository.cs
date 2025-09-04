using MobApi.Models;

namespace MobApi.Repositories;

public interface IMobRepository
{
    Task<Mob> GetByNameAsync(string name, CancellationToken cancellationToken);
    Task<Mob> CreateAsync(Mob mob, CancellationToken cancellationToken);
    Task<Mob> GetByIdAsync(Guid id, CancellationToken cancellationToken);
    Task DeleteMobAsync(Mob mob, CancellationToken cancellationToken);
}