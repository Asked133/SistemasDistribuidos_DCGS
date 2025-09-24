using MobedexApi.Models;

namespace MobedexApi.Gateways;

public interface IMobGateway
{
    Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken);

}