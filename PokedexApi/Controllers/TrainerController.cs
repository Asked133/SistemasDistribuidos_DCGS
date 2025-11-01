using Microsoft.AspNetCore.Mvc;
using PokedexApi.Exceptions;
using PokedexApi.Infrastructure.Grpc;
using PokedexApi.Models;
using PokedexApi.Dtos;
using Medal = PokedexApi.Models.Medal;
using PokedexApi.Services;

namespace PokedexApi.Controllers;

[ApiController]
[Route("api/v1/[controller]")]
public class TrainersController : ControllerBase
{
    private readonly ITrainerService _TrainerService;
    public TrainersController(ITrainerService trainerService)
    {
        _TrainerService = trainerService;
    }
    
    [HttpGet("{id}")]
    public async Task<ActionResult<TrainerResponseDto>> GetTrainerByIdAsync(string id, CancellationToken cancellationToken)
    {
        try
        {
            var trainer = await _TrainerService.GetByIdAsync(id, cancellationToken);
            return Ok(ToDto(trainer));
        }
        catch (TrainerNotFoundException)
        {
            return NotFound();
        }
    }

    private static TrainerResponseDto ToDto(Trainer trainer)
    {
        return new TrainerResponseDto
        {
            Id = trainer.Id,
            Name = trainer.Name,
            Age = trainer.Age,
            BirthDate = trainer.BirthDate,
            CreatedAt = trainer.CreatedAt,
            Medals = trainer.Medals.Select(ToDto).ToList()
        };
    }

    private static MedalDto ToDto(Medal medal)
    {
        return new MedalDto
        {
            Region = medal.Region,
            Type = medal.Type.ToString()
        };
    }
}