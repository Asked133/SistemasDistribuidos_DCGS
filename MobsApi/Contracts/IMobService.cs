using System.ServiceModel;
using MobApi.Dtos;

namespace MobApi.Services;

[ServiceContract(Name = "MobService", Namespace = "http://mob-api/mob-service")]
public interface IMobService
{
    [OperationContract]
    Task<MobResponseDto> CreateMob(CreateMobDto mob, CancellationToken cancellationToken);
}
