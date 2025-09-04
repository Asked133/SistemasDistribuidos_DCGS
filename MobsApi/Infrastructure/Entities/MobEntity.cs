namespace MobApi.Infrastructure.Entities;

public class MobEntity
{
    public Guid Id { get; set; }
    public string Name { get; set; }
    public string Type { get; set; }
    public string Behavior { get; set; }
    public int Attack { get; set; }
    public int Speed { get; set; }
    public int HP { get; set; }
}