namespace TrainerMobApi.Models;

public class Mob
{
    public string Id { get; set; } = null!;
    public string Name { get; set; } = null!;
    public string Type { get; set; } = null!;
    public string Behavior { get; set; } = null!;
    public MobStats Stats { get; set; } = null!;
    public DateTime CreatedAt { get; set; }
    public List<MobDrop> Drops { get; set; } = new(); 
}