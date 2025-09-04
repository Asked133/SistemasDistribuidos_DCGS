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