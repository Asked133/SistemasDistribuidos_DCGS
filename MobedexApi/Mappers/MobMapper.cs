using MobedexApi.Infrastructure.Soap.Dtos;
using MobedexApi.Models;
using MobedexApi.Dtos;

namespace MobedexApi.Mappers;

public static class MobMapper
{
    public static Mob ToModel(this MobResponseDto mobResponseDto)
    {
        return new Mob
        {
            Id = mobResponseDto.Id,
            Name = mobResponseDto.Name,
            Type = mobResponseDto.Type,
            Behavior = mobResponseDto.Behavior,
            Stats = new Stats
            {
                Attack = mobResponseDto.Stats.Attack,
                Speed = mobResponseDto.Stats.Speed,
                HP = mobResponseDto.Stats.HP
            }
        };
    }

    public static MobResponse ToResponse(this Mob mob)
    {
        return new MobResponse
        {
            Id = mob.Id,
            Name = mob.Name,
            Type = mob.Type,
            Attack = mob.Stats.Attack
        };
    }
}