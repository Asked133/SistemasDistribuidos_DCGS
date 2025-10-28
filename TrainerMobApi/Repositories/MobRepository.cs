using Microsoft.Extensions.Options;
using MongoDB.Driver;
using TrainerMobApi.Infrastructure;
using TrainerMobApi.Infrastructure.Documents;
using TrainerMobApi.Models;
using TrainerMobApi.Mappers;

namespace TrainerMobApi.Repositories;

public class MobRepository : IMobRepository
{
    private readonly IMongoCollection<MobDocument> _mobCollection;

    public MobRepository(IMongoDatabase database, IOptions<MongoDBSettings> settings)
    {
        _mobCollection = database.GetCollection<MobDocument>(settings.Value.MobsCollectionName);
    }

    public async Task<Mob> CreateAsync(Mob mob, CancellationToken cancellationToken)
    {
        mob.CreatedAt = DateTime.UtcNow;
        var mobToCreate = mob.ToDocument();
        await _mobCollection.InsertOneAsync(mobToCreate, cancellationToken: cancellationToken);
        return mobToCreate.ToDomain();
    }

    public async Task<IEnumerable<Mob>> GetByNameAsync(string name, CancellationToken cancellationToken)
    {
        var mobs = await _mobCollection.Find(filter: t => t.Name.Contains(name)).ToListAsync(cancellationToken);
        return mobs.Select(selector: t => t.ToDomain());
    }
}