using Microsoft.EntityFrameworkCore;
using MobApi.Infrastructure;
using MobApi.Models;
using MobApi.Mappers;

namespace MobApi.Repositories;

public class MobRepository : IMobRepository
{
    private readonly RelationalDbContext _context;

    public MobRepository(RelationalDbContext context)
    {
        _context = context;

    }

    public async Task<IReadOnlyList<Mob>> GetMobsByNameAsync(string name, CancellationToken cancellationToken)
    {
        var mobs = await _context.Mobs.AsNoTracking().Where(s => s.Name.Contains(name)).ToListAsync(cancellationToken);
        return mobs.ToModel();
    }


    public async Task UpdateMobAsync(Mob mob, CancellationToken cancellationToken)
    {
        _context.Mobs.Update(mob.ToEntity());
        await _context.SaveChangesAsync(cancellationToken);
    }

    public async Task DeleteMobAsync(Mob mob, CancellationToken cancellationToken)
    {
        // Delete   * from Mobs where Id = mob.Id
        _context.Mobs.Remove(mob.ToEntity());
        await _context.SaveChangesAsync(cancellationToken);
    }

       public async Task<Mob> GetByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        var mob = await _context.Mobs.AsNoTracking().FirstOrDefaultAsync(s => s.Id == id, cancellationToken);
        return mob.ToModel();
    }

    public async Task<Mob> GetByNameAsync(string name, CancellationToken cancellationToken)
    {
        //select * from mobs where name like '%TEXTO%'
        var mob = await _context.Mobs.AsNoTracking().FirstOrDefaultAsync(s => s.Name.Contains(name));
        return mob.ToModel();
    }

    public async Task<Mob> CreateAsync(Mob mob, CancellationToken cancellationToken)
    {
        var mobToCreate = mob.ToEntity();
        mobToCreate.Id = Guid.NewGuid();
        await _context.Mobs.AddAsync(mobToCreate, cancellationToken);
        await _context.SaveChangesAsync(cancellationToken);

        return mobToCreate.ToModel();
    }
}