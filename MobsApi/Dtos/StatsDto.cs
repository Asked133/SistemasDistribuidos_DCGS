using System.Runtime.Serialization;

namespace MobApi.Dtos;

[DataContract(Name = "StatsDto", Namespace = "http://mob-api/mob-service")]
public class StatsDto
{
    [DataMember(Name = "Attack", Order = 1)]
    public int Attack { get; set; }
    [DataMember(Name = "Speed", Order = 2)]
    public int Speed { get; set; }
    [DataMember(Name ="HP", Order = 3)]
    public int HP { get; set; }
}
