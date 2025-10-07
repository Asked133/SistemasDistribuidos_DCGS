using MobedexApi.Exceptions;
using MobedexApi.Gateways;
using MobedexApi.Models;
using MobedexApi.Dtos;
using MobedexApi.Mappers;

namespace MobedexApi.Services;

public class MobService : IMobService
{
    private readonly IMobGateway _mobGateway;

    public MobService(IMobGateway mobGateway)
    {
        _mobGateway = mobGateway;
    }

    public async Task<Mob> PatchMobAsync(Guid id, string? name, string? type, int? attack, int? defense, int? speed, int? HP, CancellationToken cancellationToken)
    {
    var mob = await _mobGateway.GetMobByIdAsync(id, cancellationToken);
    if (mob == null)
    {
        throw new MobNotFoundException(id);
    }

    mob.MobPatch(name, type, attack, speed, HP);
    await _mobGateway.UpdateMobAsync(mob, cancellationToken);
    
    return mob;
    }
    

    public async Task<Mob> UpdateMobAsync(Mob mob, CancellationToken cancellationToken)
    {
        return await _mobGateway.UpdateMobAsync(mob, cancellationToken);
    }

    public async Task DeleteMobAsync(Guid id, CancellationToken cancellationToken)
    {
        await _mobGateway.DeleteMobAsync(id, cancellationToken);
    }

    public async Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        return await _mobGateway.GetMobByIdAsync(id, cancellationToken);
    }

    public async Task<PagedResponse<MobResponse>> GetMobsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken)
    {
        var (mobs, totalRecords) = await _mobGateway.GetMobsAsync(name, type, pageNumber, pageSize, orderBy, orderDirection, cancellationToken);
        return PagedResponse<MobResponse>.Create(mobs.ToResponse(), totalRecords, pageNumber, pageSize);
    }

public async Task<Mob> CreateMobAsync(Mob mob, CancellationToken cancellationToken)
{
    var mobs = await _mobGateway.GetMobsByNameAsync(mob.Name, cancellationToken);
    if (MobExists(mobs, mob.Name))
    {
        throw new MobAlreadyExistsException(mob.Name);
    }

    return await _mobGateway.CreateMobAsync(mob, cancellationToken);
}

    private static bool MobExists(IList<Mob> mobs, string mobNameToSearch)
    {
        return mobs.Any(s => s.Name.ToLower().Equals(mobNameToSearch.ToLower()));
    }
}