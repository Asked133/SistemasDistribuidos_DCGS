namespace TrainerMobApi.Infrastructure;

public class MongoDBSettings
{
    public string ConnectionString { get; set; } = null!;
    public string DatabaseName { get; set; } = null!;
    public string MobsCollectionName { get; set; } = null!; 
}