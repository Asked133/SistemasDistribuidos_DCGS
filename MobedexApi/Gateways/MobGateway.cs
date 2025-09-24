using System.ServiceModel;
using MobedexApi.Infrastructure.Soap.Contracts;
using MobedexApi.Models;
using MobedexApi.Mappers;

namespace MobedexApi.Gateways;

public class MobGateway : IMobGateway
{
    private readonly IMobContract _mobContract;
    private readonly ILogger<MobGateway> _logger;

    public MobGateway(IConfiguration configuration, ILogger<MobGateway> logger)
    {
        var binding = new BasicHttpBinding();
        var endpoint = new EndpointAddress(configuration.GetValue<string>("MobService:Url"));
        _mobContract = new ChannelFactory<IMobContract>(binding, endpoint).CreateChannel();
        _logger = logger;
    }




    public async Task<Mob> GetMobByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        try
        {
            var mob = await _mobContract.GetMobById(id, cancellationToken);
            return mob.ToModel();
        }
        catch (FaultException ex) when (ex.Message == "Mob not found")
        {
            _logger.LogWarning("Mob not found.");
            return null;
        }
    }
}