using System.Runtime.Serialization;

namespace MobApi.Dtos;

[DataContract(Name = "PagedMobResponseDto", Namespace = "http://mob-api/mob-service")]
public class PagedMobResponseDto
{
    [DataMember(Name = "Mobs", Order = 1)]
    public List<MobResponseDto> Mobs { get; set; } = new List<MobResponseDto>();
    
    [DataMember(Name = "TotalRecords", Order = 2)]
    public int TotalRecords { get; set; }
}