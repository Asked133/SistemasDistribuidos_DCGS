using System.ServiceModel;
using MobApi.Dtos;
using MobApi.Repositories;
using MobApi.Mappers;
using MobApi.Validators;

namespace MobApi.Services;

public class MobService : IMobService
{
    private readonly IMobRepository _mobRepository;
    public MobService(IMobRepository mobRepository)
    {
        _mobRepository = mobRepository;
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

    private async Task<bool> MobAlreadyExists(string name, CancellationToken cancellationToken)
    {
        var mobs = await _mobRepository.GetByNameAsync(name, cancellationToken);
        return mobs is not null;
    }
}