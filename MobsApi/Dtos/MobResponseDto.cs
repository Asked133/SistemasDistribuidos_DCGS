using System.Runtime.Serialization;
using MobApi.Dtos;

namespace MobApi.Dtos;

[DataContract(Name = "MobResponseDto", Namespace = "http://mob-api/mob-service")]
public class MobResponseDto
{
    [DataMember(Name = "Id", Order = 1)]
    public Guid Id { get; set; }

    [DataMember(Name = "Name", Order = 2)]
    public required string Name { get; set; }

    [DataMember(Name = "Type", Order = 3)]
    public required string Type { get; set; }

    [DataMember(Name = "Behavior", Order = 4)]
    public required string Behavior { get; set; }
    [DataMember(Name = "Stats", Order = 5)]
    public required StatsDto Stats { get; set; }
}