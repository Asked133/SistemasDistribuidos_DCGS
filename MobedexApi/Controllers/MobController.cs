using Microsoft.AspNetCore.Mvc;
using MobedexApi.Dtos;
using MobedexApi.Services;
using MobedexApi.Mappers;
using MobedexApi.Exceptions;
using MobedexApi.Models;

namespace MobedexApi.Controllers;

[ApiController]
[Route("api/v1/[controller]")]
public class MobsController : ControllerBase
{
    private readonly IMobService _mobService;
    public MobsController(IMobService mobService)
    {
        _mobService = mobService;
    }

    [HttpGet]
    public async Task<ActionResult<PagedResponse<MobResponse>>> GetMobsAsync(
        [FromQuery] string? name,
        [FromQuery] string? type,
        CancellationToken cancellationToken,
        [FromQuery] int pageNumber = 1,
        [FromQuery] int pageSize = 10,
        [FromQuery] string orderBy = "Name",
        [FromQuery] string orderDirection = "asc")
    {
        var mobs = await _mobService.GetMobsAsync(name, type, pageNumber, pageSize, orderBy, orderDirection, cancellationToken);
        return Ok(mobs);
    }

    //Http Verb -Get
    [HttpGet("{id}", Name = "GetMobByIdAsync")]
    public async Task<ActionResult<MobResponse>> GetMobByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        var mob = await _mobService.GetMobByIdAsync(id, cancellationToken);
        return mob is null ? NotFound() : Ok(mob.ToResponse());
    }



    [HttpPost]
    public async Task<ActionResult<MobResponse>> CreateMobAsync([FromBody] CreateMobRequest createMob, CancellationToken cancellationToken)
    {
        try
        {
            if (!IsValidAttack(createMob.Stats.Attack))
            {
                return BadRequest(new { Message = "Attack does not have a valid value" });
            }

            var mob = await _mobService.CreateMobAsync(createMob.ToModel(), cancellationToken);

            // 201
            return CreatedAtRoute("GetMobByIdAsync", new { id = mob.Id }, mob.ToResponse());
        }
        catch (MobAlreadyExistsException e)
        {
            // 409 Conflict - { Message = "Mob NAME already exists" }
            return Conflict(new { Message = e.Message });
        }
    }




    [HttpDelete("{id}")]
    public async Task<ActionResult> DeletePokemonAsync(Guid id, CancellationToken cancellationToken)
    {
        try
        {
            await _mobService.DeleteMobAsync(id, cancellationToken);
            return NoContent(); // 204 No Content
        }
        catch (MobNotFoundException)
        {
            return NotFound(); // 404 Not Found
        }
    }


    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateMobAsync(Guid id, [FromBody] UpdateMobRequest mob, CancellationToken cancellationToken)
    {
        try
        {
            if (!IsValidAttack(mob.Stats.Attack))
            {
                return BadRequest(new { Message = "Attack does not have a valid value" });
            }

            await _mobService.UpdateMobAsync(mob.ToModel(id), cancellationToken);
            return NoContent(); //204
        }
        catch (MobNotFoundException)
        {
            return NotFound(); //404
        }
        catch (MobAlreadyExistsException ex)
        {
            return Conflict(new { Message = ex.Message }); // 409
        }
    }

  

    [HttpPatch("{id}")]
    public async Task<ActionResult> PatchMobAsync(Guid id, [FromBody] PatchMobRequest mobRequest, CancellationToken cancellationToken)
    {
        try
        {
            if (mobRequest.Attack.HasValue && !IsValidAttack(mobRequest.Attack.Value))
            {
                return BadRequest(new { Message = "Attack does not have a valid value" });
            }

            var mob = await _mobService.PatchMobAsync(id, mobRequest.Name, mobRequest.Type, mobRequest.Attack, mobRequest.Defense, mobRequest.Speed, mobRequest.HP, cancellationToken);
            return Ok(mob.ToResponse()); //200
        }
        catch (MobNotFoundException)
        {
            return NotFound(); //404
        }
        catch (MobAlreadyExistsException ex)
        {
            return Conflict(new { Message = ex.Message }); // 409
        }
    }


    private static bool IsValidAttack(int attack)
    {
        return attack > 0;
    }
}