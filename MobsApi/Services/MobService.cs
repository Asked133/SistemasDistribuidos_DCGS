using System.ServiceModel;
using MobApi.Dtos;
using MobApi.Repositories;
using MobApi.Mappers;
using MobApi.Validators;
using MobApi.Models;

namespace MobApi.Services;

public class MobService : IMobService
{
    private readonly IMobRepository _mobRepository;
    public MobService(IMobRepository mobRepository)
    {
        _mobRepository = mobRepository;
    }

    public async Task<DeleteMobResponseDto> DeleteMob(Guid id, CancellationToken cancellationToken)
    {
        var mob = await _mobRepository.GetByIdAsync(id, cancellationToken);
        if (!MobExists(mob))
        {
            throw new FaultException("Mob not found");
        }
        await _mobRepository.DeleteMobAsync(mob, cancellationToken);
        return new DeleteMobResponseDto { Success = true };
    }


    public async Task<MobResponseDto> GetMobById(Guid id, CancellationToken cancellationToken)
    {
        var mob = await _mobRepository.GetByIdAsync(id, cancellationToken);
        return MobExists(mob) ? mob.ToResponseDto() : throw new FaultException("Mob not found");
    }

    public async Task<MobResponseDto> CreateMob(CreateMobDto mobRequest, CancellationToken cancellationToken)
    {
        mobRequest.ValidateName().ValidateType();
        mobRequest.Stats.ValidateHP();

        if (await MobAlreadyExists(mobRequest.Name, cancellationToken))
        {
            throw new FaultException("Mob already exists");
        }

        var mob = await _mobRepository.CreateAsync(mobRequest.ToModel(), cancellationToken);

        return mob.ToResponseDto();
    }

    private static bool MobExists(Mob mob) => mob is not null;

    private async Task<bool> MobAlreadyExists(string name, CancellationToken cancellationToken)
    {
        var mobs = await _mobRepository.GetByNameAsync(name, cancellationToken);
        return mobs is not null;
    }
}