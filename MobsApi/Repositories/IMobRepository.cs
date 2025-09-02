using MobApi.Models;

namespace MobApi.Repositories;

public interface IMobRepository
{
    Task<Mob> GetByNameAsync(string name, CancellationToken cancellationToken);

    Task<Mob> CreateAsync(Mob mob, CancellationToken cancellationToken);
}