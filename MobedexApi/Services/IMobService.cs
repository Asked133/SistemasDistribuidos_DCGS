namespace MobedexApi.Services;
using MobedexApi.Models;
using MobedexApi.Dtos;

public interface IMobService
{
    Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken);
    Task<Mob> CreateMobAsync(Mob mob, CancellationToken cancellationToken);
    Task<PagedResponse<MobResponse>> GetMobsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken);
    Task DeleteMobAsync(Guid id, CancellationToken cancellationToken);

    Task<Mob> UpdateMobAsync(Mob mob, CancellationToken cancellationToken);

    Task<Mob> PatchMobAsync(Guid id, string? name, string? type, int? attack, int? defense, int? speed, int? HP, CancellationToken cancellationToken);
}