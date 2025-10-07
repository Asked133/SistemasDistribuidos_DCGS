using Microsoft.AspNetCore.Mvc;
using MobedexApi.Dtos;
using MobedexApi.Services;
using MobedexApi.Mappers;

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

    //Http Status
    // 200 OK (Si existe el mob)
    //400 Bad Request (Id invalido) --- Casi no se usa
    // 404 Not Found (no existe el pokemon)
    // 500 Internal Server Error (Error del servidor)

    //Http Verb -Get
    [HttpGet("{id}", Name = "GetMobByIdAsync")]
public async Task<ActionResult<MobResponse>> GetMobByIdAsync(Guid id, CancellationToken cancellationToken)
{
    if (id == Guid.Empty)
        return BadRequest("El id proporcionado no es válido.");

    var mob = await _mobService.GetMobByIdAsync(id, cancellationToken);
    return mob is null ? NotFound() : Ok(mob.ToResponse());
}
}