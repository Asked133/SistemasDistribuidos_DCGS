using TrainerMobApi.Models;

namespace TrainerMobApi.Repositories;

public interface IMobRepository
{
    Task<Mob> CreateAsync(Mob mob, CancellationToken cancellationToken);
    Task<IEnumerable<Mob>> GetByNameAsync(string name, CancellationToken cancellationToken);
}