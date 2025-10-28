using Grpc.Core;
using TrainerMobApi.Mappers;
using TrainerMobApi.Repositories;

namespace TrainerMobApi.Services;

public class MobService : TrainerMobApi.MobService.MobServiceBase
{
    private readonly IMobRepository _mobRepository;
    
    public MobService(IMobRepository mobRepository)
    {
        _mobRepository = mobRepository;
    }

    public override async Task<CreateMobsResponse> CreateMobs(IAsyncStreamReader<CreateMobRequest> requestStream, ServerCallContext context)
    {
        var createdMobs = new List<MobResponse>();

        while (await requestStream.MoveNext(cancellationToken: context.CancellationToken))
        {
            var request = requestStream.Current;
            var mob = request.ToModel();
            
            var mobExists = await _mobRepository.GetByNameAsync(mob.Name, context.CancellationToken);
            if (mobExists.Any())
            {
                continue; 
            }

            var createdMob = await _mobRepository.CreateAsync(mob, context.CancellationToken);
            createdMobs.Add(createdMob.ToResponse());
        }

        return new CreateMobsResponse
        {
            SuccessCount = createdMobs.Count,
            Mobs = { createdMobs },
        };
    }
}