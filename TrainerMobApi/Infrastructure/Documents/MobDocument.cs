
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace TrainerMobApi.Infrastructure.Documents;

public class MobDocument
{
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public string Id { get; set; } = null!;
    [BsonElement("name")]
    public string Name { get; set; } = null!;
    [BsonElement("type")]
    public string Type { get; set; } = null!;
    [BsonElement("behavior")]
    public string Behavior { get; set; } = null!;
    [BsonElement("stats")]
    public MobStatsDocument Stats { get; set; } = null!;
    [BsonElement("created_at")]
    public DateTime CreatedAt { get; set; }
    
    [BsonElement("drops")]
    public List<MobDropDocument> Drops { get; set; } = new();
}