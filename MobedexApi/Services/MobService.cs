using MobedexApi.Gateways;
using MobedexApi.Models;

namespace MobedexApi.Services;

public class MobService : IMobService
{
    private readonly IMobGateway _mobGateway;

    public MobService(IMobGateway mobGateway)
    {
        _mobGateway = mobGateway;
    }
    public async Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        return await _mobGateway.GetMobByIdAsync(id, cancellationToken);
    }
}
