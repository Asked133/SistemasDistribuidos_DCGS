using System.ServiceModel;

using MobedexApi.Infrastructure.Soap.Dtos;

namespace MobedexApi.Infrastructure.Soap.Contracts;



[ServiceContract(Name = "MobService", Namespace = "http://mob-api/mob-service")]
public interface IMobContract
{
    [OperationContract]
    Task<PagedMobResponseDto> GetMobsAsync(GetMobsRequestDto request, CancellationToken cancellationToken);

    [OperationContract]
    Task<MobResponseDto> CreateMob(CreateMobDto mob, CancellationToken cancellationToken);

    [OperationContract]
    Task<MobResponseDto> GetMobById(Guid mobId, CancellationToken cancellationToken);

    [OperationContract]
    Task<DeleteMobResponseDto> DeleteMob(Guid id, CancellationToken cancellationToken);

    [OperationContract]
    Task<MobResponseDto> UpdateMob(UpdateMobDto mob, CancellationToken cancellationToken);

    [OperationContract]
    Task<IList<MobResponseDto>> GetMobsByName(string name, CancellationToken cancellationToken);

}
