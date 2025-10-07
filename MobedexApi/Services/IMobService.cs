namespace MobedexApi.Services;
using MobedexApi.Models;

public interface IMobService
{
    Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken);
}