using System.Runtime.Serialization;
namespace MobApi.Dtos;

[DataContract(Name = "GetMobsRequestDto", Namespace = "http://mob-api/mob-service")]
public class GetMobsRequestDto
{
    [DataMember(Name = "Name", Order = 1)]
    public string? Name { get; set; }
    
    [DataMember(Name = "Type", Order = 2)]
    public string? Type { get; set; }
    
    [DataMember(Name = "PageNumber", Order = 3)]
    public int PageNumber { get; set; }
    
    [DataMember(Name = "PageSize", Order = 4)]
    public int PageSize { get; set; }
    
    [DataMember(Name = "OrderBy", Order = 5)]
    public string? OrderBy { get; set; }
    
    [DataMember(Name = "OrderDirection", Order = 6)]
    public string? OrderDirection { get; set; }
}