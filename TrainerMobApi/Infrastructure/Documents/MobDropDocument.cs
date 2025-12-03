using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using TrainerMobApi.Models;

namespace TrainerMobApi.Infrastructure.Documents;

public class MobDropDocument
{
    [BsonElement("item_name")]
    public string ItemName { get; set; } = null!;

    [BsonElement("rarity")]
    [BsonRepresentation(BsonType.String)]
    public DropRarity Rarity { get; set; }
}