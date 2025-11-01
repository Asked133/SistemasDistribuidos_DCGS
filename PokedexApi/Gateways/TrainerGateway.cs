using PokedexApi.Exceptions;
using PokedexApi.Infrastructure.Grpc;
using PokedexApi.Models;

namespace PokedexApi.Gateways;

public class TrainerGateway : ITrainerGateway
{
    private readonly TrainerService.TrainerServiceClient _client;

    public TrainerGateway(TrainerService.TrainerServiceClient client)
    {
        _client = client;
    }

    public async Task<Trainer> GetTrainerById(string id, CancellationToken cancellationToken)
    {
        try
        {
            var trainer =  await _client.GetTrainerByIdAsync(new TrainerByIdRequest { Id = id }, cancellationToken: cancellationToken);
            return ToModel(trainer);
        }
        catch (Grpc.Core.RpcException ex) when (ex.StatusCode == Grpc.Core.StatusCode.NotFound)
        {
            throw new TrainerNotFoundException(id);
        }
    }


    private static Trainer ToModel(TrainerResponse trainerResponse)
    {
        return new Trainer
        {
            Id = trainerResponse.Id,
            Name = trainerResponse.Name,
            Age = trainerResponse.Age,
            BirthDate = trainerResponse.Birthdate.ToDateTime(),
            CreatedAt = trainerResponse.CreatedAt.ToDateTime(),
            Medals = trainerResponse.Medals.Select(ToModel).ToList()
        };
    }

    private static Models.Medal ToModel(Infrastructure.Grpc.Medal medal)
    {
        return new Models.Medal
        {
            Region = medal.Region,
            Type = (MedalType)medal.Type
        };
    }
}