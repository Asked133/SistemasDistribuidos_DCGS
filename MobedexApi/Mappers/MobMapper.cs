
using MobedexApi.Dtos;
using MobedexApi.Infrastructure.Soap.Dtos;
using MobedexApi.Models;

namespace MobedexApi.Mappers;

public static class MobMapper
{


    public static Mob ToModel(this UpdateMobRequest updateMobRequest, Guid id)
    {
        return new Mob
        {
            Id = id,
            Name = updateMobRequest.Name,
            Type = updateMobRequest.Type,
            Behavior = updateMobRequest.Behavior,
            Stats = new Stats
            {
                Attack = updateMobRequest.Stats.Attack,
                Speed = updateMobRequest.Stats.Speed,
                HP = updateMobRequest.Stats.HP
            }
        };
    }
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
    public static IList<Mob> ToModel(this IList<MobResponseDto> mobResponseDtos)
    {
        if (mobResponseDtos == null)
            return new List<Mob>();
            
        return mobResponseDtos.Select(s => s.ToModel()).ToList();
    }

    public static MobResponse ToResponse(this Mob pokemon)
    {
        return new MobResponse
        {
            Id = pokemon.Id,
            Name = pokemon.Name,
            Type = pokemon.Type,
            Attack = pokemon.Stats.Attack
        };
    }
    public static Mob ToModel(this CreateMobRequest createMobRequest)
    {
        return new Mob
        {
            Name = createMobRequest.Name,
            Type = createMobRequest.Type,
            Behavior = createMobRequest.Behavior,
            Stats = new Stats
            {
                Attack = createMobRequest.Stats.Attack,
                Speed = createMobRequest.Stats.Speed,
                HP = createMobRequest.Stats.HP
            }
        };
    }
    public static CreateMobDto ToRequest(this Mob pokemon)
    {
        return new CreateMobDto
        {
            Name = pokemon.Name,
            Type = pokemon.Type,
            Behavior = pokemon.Behavior,
            Stats = new StatsDto
            {
                Attack = pokemon.Stats.Attack,
                Speed = pokemon.Stats.Speed,
                HP = pokemon.Stats.HP
            }
        };
    }

    public static UpdateMobDto ToUpdateRequest(this Mob mob)
    {
        return new UpdateMobDto
        {
            Id = mob.Id,
            Name = mob.Name,
            Type = mob.Type,
            Behavior = mob.Behavior,
            Stats = new StatsDto
            {
                Attack = mob.Stats.Attack,
                Speed = mob.Stats.Speed,
                HP = mob.Stats.HP
            }
        };
    }

    public static Mob MobPatch(this Mob mob, string? name, string? type, int? attack, int? speed, int? hp)
{
    mob.Name = name ?? mob.Name;
    mob.Type = type ?? mob.Type;
    mob.Stats.Attack = attack ?? mob.Stats.Attack;
    mob.Stats.Speed = speed ?? mob.Stats.Speed;
    mob.Stats.HP = hp ?? mob.Stats.HP;
    
    return mob;
}

    public static IList<MobResponse> ToResponse(this IList<Mob> mobs)
    {
        return mobs.Select(s => s.ToResponse()).ToList();
    }
}