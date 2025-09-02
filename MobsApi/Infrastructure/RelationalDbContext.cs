using Microsoft.EntityFrameworkCore;
using MobApi.Infrastructure.Entities;

namespace MobApi.Infrastructure;

public class RelationalDbContext : DbContext
{
    public DbSet<MobEntity> Mobs { get; set; }
    public RelationalDbContext(DbContextOptions<RelationalDbContext> db) : base(db)
    {
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<MobEntity>(entity =>
        {
            entity.HasKey(e => e.Id);
            entity.Property(e => e.Name).IsRequired().HasMaxLength(100);
            entity.Property(e => e.Type).IsRequired().HasMaxLength(50);
            entity.Property(e => e.Behavior).IsRequired();
            entity.Property(e => e.Attack).IsRequired();
            entity.Property(e => e.Speed).IsRequired();
            entity.Property(e => e.HP).IsRequired();
        });
    }
}