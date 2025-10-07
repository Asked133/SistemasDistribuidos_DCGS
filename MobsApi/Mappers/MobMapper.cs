using Microsoft.AspNetCore.StaticAssets;
using MobApi.Dtos;
using MobApi.Infrastructure.Entities;
using MobApi.Models;

namespace MobApi.Mappers;

public static class MobMapper
{
    public static Mob ToModel(this MobEntity mobEntity)
    {
        if (mobEntity is null)
        {
            return null;
        }

        return new Mob
        {
            Id = mobEntity.Id,
            Name = mobEntity.Name,
            Type = mobEntity.Type,
            Behavior = mobEntity.Behavior,
            Stats = new Stats
            {
                Attack = mobEntity.Attack,
                Speed = mobEntity.Speed,
                HP = mobEntity.HP
            }
        };
    }

    public static MobEntity ToEntity(this Mob mob)
    {
        return new MobEntity
        {
            Id = mob.Id,
            Behavior = mob.Behavior,
            Type = mob.Type,
            Name = mob.Name,
            Attack = mob.Stats.Attack,
            Speed = mob.Stats.Speed,
            HP = mob.Stats.HP
        };
    }

    public static MobResponseDto ToResponseDto(this Mob mob)
    {
        return new MobResponseDto
        {
            Id = mob.Id,
            Behavior = mob.Behavior,
            Type = mob.Type,
            Name = mob.Name,
            Stats = new StatsDto
            {
                Attack = mob.Stats.Attack,
                Speed = mob.Stats.Speed,
                HP = mob.Stats.HP
            }
        };
    }

    public static Mob ToModel(this CreateMobDto requestMobDto)
    {
        return new Mob
        {
            Behavior = requestMobDto.Behavior,
            Type = requestMobDto.Type,
            Name = requestMobDto.Name,
            Stats = new Stats
            {
                Attack = requestMobDto.Stats.Attack,
                Speed = requestMobDto.Stats.Speed,
                HP = requestMobDto.Stats.HP
            }
        };
    }
    public static IList<MobResponseDto> ToResponseDto(this IReadOnlyList<Mob> mobs)
    {
        return mobs.Select(s => s.ToResponseDto()).ToList();
    }

    public static IReadOnlyList<Mob> ToModel(this IReadOnlyList<MobEntity> mobs)
    {
        return mobs.Select(s => s.ToModel()).ToList();
    }

    public static PagedMobResponseDto ToPagedResponseDto(
        this IReadOnlyList<Mob> mobs,
        int totalRecords)
    {
        return new PagedMobResponseDto
        {
            Mobs = mobs?.Select(m => m.ToResponseDto()).ToList()
                   ?? new List<MobResponseDto>(),
            TotalRecords = totalRecords
        };
    }

    public static Mob UpdateFrom(this Mob mob, UpdateMobDto updateDto)
    {
        mob.Name = updateDto.Name;
        mob.Type = updateDto.Type;
        
        
        mob.Stats.Attack = updateDto.Stats.Attack;
        mob.Stats.Speed = updateDto.Stats.Speed;
        mob.Stats.HP = updateDto.Stats.HP;
        
        return mob;
    }
    
}
