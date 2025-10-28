using System.ComponentModel.DataAnnotations;

namespace MobedexApi.Dtos;

public class CreateMobRequest
{
    [Required]
    public string Name { get; set; }
    [MinLength(3)]
    public string Type { get; set; }
    public string Behavior { get; set; }
    public StatsRequest Stats { get; set; }
}
