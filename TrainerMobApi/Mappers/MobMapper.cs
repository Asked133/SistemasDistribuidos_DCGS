using Google.Protobuf.WellKnownTypes;
using TrainerMobApi.Infrastructure.Documents;
using TrainerMobApi.Models;


namespace TrainerMobApi.Mappers;

public static class MobMapper
{
    public static Mob ToModel(this CreateMobRequest request) => new()
    {
        Name = request.Name,
        Type = request.Type,
        Behavior = request.Behavior,
        Stats = new MobStats
        {
            Attack = request.Stats.Attack,
            Speed = request.Stats.Speed,
            HP = request.Stats.Hp
        },
        Drops = request.Drops.Select(d => new MobDrop
        {
            ItemName = d.ItemName,
            Rarity = (DropRarity)d.Rarity
        }).ToList()
    };

    public static MobResponse ToResponse(this Mob mob)
    {
        var response = new MobResponse
        {
            Id = mob.Id,
            Name = mob.Name,
            Type = mob.Type,
            Behavior = mob.Behavior,
            Stats = new MobStatsProto
            {
                Attack = mob.Stats.Attack,
                Speed = mob.Stats.Speed,
                Hp = mob.Stats.HP
            },
            CreatedAt = mob.CreatedAt.ToTimestamp()
        };
        
        response.Drops.AddRange(mob.Drops.Select(d => new MobDropProto
        {
            ItemName = d.ItemName,
            Rarity = (DropRarityProto)d.Rarity
        }));

        return response;
    }

    public static MobDocument ToDocument(this Mob mob) => new()
    {
        Id = mob.Id,
        Name = mob.Name,
        Type = mob.Type,
        Behavior = mob.Behavior,
        Stats = new MobStatsDocument
        {
            Attack = mob.Stats.Attack,
            Speed = mob.Stats.Speed,
            HP = mob.Stats.HP
        },
        CreatedAt = mob.CreatedAt,
        Drops = mob.Drops.Select(d => new MobDropDocument
        {
            ItemName = d.ItemName,
            Rarity = d.Rarity 
        }).ToList()
    };

    public static Mob ToDomain(this MobDocument doc) => new()
    {
        Id = doc.Id,
        Name = doc.Name,
        Type = doc.Type,
        Behavior = doc.Behavior,
        Stats = new MobStats
        {
            Attack = doc.Stats.Attack,
            Speed = doc.Stats.Speed,
            HP = doc.Stats.HP
        },
        CreatedAt = doc.CreatedAt,
        Drops = doc.Drops.Select(d => new MobDrop
        {
            ItemName = d.ItemName,
            Rarity = d.Rarity
        }).ToList()
    };
}