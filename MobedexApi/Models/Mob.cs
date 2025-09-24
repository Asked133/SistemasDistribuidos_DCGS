using MobedexApi.Infrastructure.Soap.Dtos;

namespace MobedexApi.Models;

public class Mob
{
    public Guid Id { get; set; }
    public string Name { get; set; }
    public string Type { get; set; }
    public string Behavior { get; set; }
    public Stats Stats { get; set; }
}

public class Stats
{
    public int Attack { get; set; }
    public int Speed { get; set; }
    public int HP { get; set; }
}