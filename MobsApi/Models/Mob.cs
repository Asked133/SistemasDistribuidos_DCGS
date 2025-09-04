using MobApi.Dtos;

namespace MobApi.Models;

public class Mob
{
    public Guid Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Type { get; set; } = string.Empty;
    public string Behavior { get; set; } = string.Empty;
    public Stats Stats { get; set; } = new();
}