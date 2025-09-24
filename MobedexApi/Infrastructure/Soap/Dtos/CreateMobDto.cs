using System.Runtime.Serialization;

namespace MobedexApi.Infrastructure.Soap.Dtos;

[DataContract(Name = "CreateMobDto", Namespace = "http://mob-api/mob-service")]
public class CreateMobDto
{
    [DataMember(Name = "Name", Order = 1)]
    public  string? Name { get; set; }

    [DataMember(Name = "Type", Order = 2)]
    public  string? Type { get; set; }

    [DataMember(Name = "Behavior", Order = 3)]
    public string? Behavior { get; set; }
    [DataMember(Name = "Stats", Order = 4)]
    public required StatsDto Stats { get; set; }
}