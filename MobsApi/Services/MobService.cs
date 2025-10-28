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

    public async Task<PagedMobResponseDto> GetMobsAsync(GetMobsRequestDto request, CancellationToken cancellationToken)
{
    var (mobs, totalRecords) = await _mobRepository.GetMobsAsync(
        request.Name,
        request.Type,
        request.PageNumber,
        request.PageSize,
        request.OrderBy,
        request.OrderDirection,
        cancellationToken);

    return mobs.ToPagedResponseDto(totalRecords);
}

    public async Task<IList<MobResponseDto>> GetMobsByName(string name, CancellationToken cancellationToken)
    {
        var mobs = await _mobRepository.GetMobsByNameAsync(name, cancellationToken);
        return mobs.ToResponseDto();
    }

    public async Task<MobResponseDto> UpdateMob(UpdateMobDto mobToUpdate, CancellationToken cancellationToken)
    {
        var mob = await _mobRepository.GetByIdAsync(mobToUpdate.Id, cancellationToken);
        if (!MobExists(mob))
        {
            throw new FaultException("Mob not found");
        }

        if (!await IsMobAllowedToBeUpdated(mobToUpdate, cancellationToken))
        {
            throw new FaultException("Another mob with the same name already exists");
        }

        mob.UpdateFrom(mobToUpdate);

        await _mobRepository.UpdateMobAsync(mob, cancellationToken);
        return mob.ToResponseDto();
    }

    private async Task<bool> IsMobAllowedToBeUpdated(UpdateMobDto mobToUpdate, CancellationToken cancellationToken)
    {
        var duplicatedMob = await _mobRepository.GetByNameAsync(mobToUpdate.Name, cancellationToken);
        return duplicatedMob is null || IsTheSameMob(duplicatedMob, mobToUpdate);
    }

    private static bool IsTheSameMob(Mob mob, UpdateMobDto mobToUpdate)
    {
        return mob.Id == mobToUpdate.Id;
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