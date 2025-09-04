using System.Runtime.Serialization;

namespace MobApi.Dtos;

[DataContract(Name = "DeleteMobResponseDto", Namespace = "http://mob-api/mob-service")]
public class DeleteMobResponseDto
{
    [DataMember(Name = "Success", Order = 1)]
    public bool Success { get; set; }
}