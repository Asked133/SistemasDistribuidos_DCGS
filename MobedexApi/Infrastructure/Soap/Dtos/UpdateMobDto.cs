using System.Runtime.Serialization;

namespace MobedexApi.Infrastructure.Soap.Dtos;

[DataContract(Name = "UpdateMobDto", Namespace = "http://mob-api/mob-service")]
public class UpdateMobDto
{
    [DataMember(Name = "Id", Order = 1)]
    public Guid Id { get; set; }

    [DataMember(Name = "Name", Order = 2)]
    public string Name { get; set; }

    [DataMember(Name = "Type", Order = 3)]
    public string Type { get; set; }

    [DataMember(Name = "Behavior", Order = 4)]
    public string Behavior { get; set; }

    [DataMember(Name = "Stats", Order = 5)]
    public StatsDto Stats { get; set; }
}