using System.ServiceModel;
using MobedexApi.Infrastructure.Soap.Contracts;
using MobedexApi.Models;
using MobedexApi.Mappers;
using MobedexApi.Exceptions;
using MobedexApi.Infrastructure.Soap.Dtos;

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

    public async Task<Mob> UpdateMobAsync(Mob mob, CancellationToken cancellationToken)
    {
        try
        {
            var updatedMob = await _mobContract.UpdateMob(mob.ToUpdateRequest(), cancellationToken);
            return updatedMob.ToModel();
        }
        catch (FaultException ex) when (ex.Message == "Mob not found")
        {
            throw new MobNotFoundException(mob.Id);
        }
        catch (FaultException e) when (e.Message.Contains("already exists"))
        {
            throw new MobAlreadyExistsException(mob.Name);
        }
    }

    public async Task<(IList<Mob> mobs, int totalRecords)> GetMobsAsync(string name, string type, int pageNumber, int pageSize, string orderBy, string orderDirection, CancellationToken cancellationToken)
    {

        var request = new GetMobsRequestDto
        {
            Name = name,
            Type = type,
            PageNumber = pageNumber,
            PageSize = pageSize,
            OrderBy = orderBy,
            OrderDirection = orderDirection
        };

        var response = await _mobContract.GetMobsAsync(request, cancellationToken);

        if (response == null)
        {
            _logger.LogWarning("GetMobsAsync returned null response");
            return (new List<Mob>(), 0);
        }

        var mobs = response.Mobs.ToModel();
        return (mobs, response.TotalRecords);
    }
    public async Task DeleteMobAsync(Guid id, CancellationToken cancellationToken)
    {
        try
        {
            await _mobContract.DeleteMob(id, cancellationToken);
        }
        catch (FaultException ex) when (ex.Message == "Mob not found")
        {
            _logger.LogWarning(ex, "Mob not found.");
            throw new MobNotFoundException(id);
        }
    }
    public async Task<Mob?> GetMobByIdAsync(Guid id, CancellationToken cancellationToken)
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
    public async Task<IList<Mob>> GetMobsByNameAsync(string name, CancellationToken cancellationToken)
    {
        _logger.LogDebug(":(");
        var mobs = await _mobContract.GetMobsByName(name, cancellationToken);
        return mobs.ToModel();
    }
    public async Task<Mob> CreateMobAsync(Mob mob, CancellationToken cancellationToken)
    {
        try
        {
            _logger.LogInformation("Sending request to SOAP API, with mob: {name}", mob.Name);
            var createdMob = await _mobContract.CreateMob(mob.ToRequest(), cancellationToken);
            return createdMob.ToModel();
        }
        catch (FaultException ex) when (ex.Message.Contains("already exists"))
        {
            _logger.LogWarning("Mob already exists: {name}", mob.Name);
            throw new MobAlreadyExistsException(mob.Name);
        }
        catch (Exception e)
        {
            _logger.LogError(e, "Error creating mob via SOAP: {message}", e.Message);
            throw; // Re-lanzar la excepción en lugar de retornar null
        }
    }
}