using Microsoft.AspNetCore.Mvc;
using PokedexApi.Dtos;
using PokedexApi.Services;
using PokedexApi.Mappers;
using PokedexApi.Exceptions;

namespace PokedexApi.Controllers;

[ApiController]
[Route("api/v1/[controller]")]
public class PokemonsController : ControllerBase
{
    private readonly IPokemonService _pokemonService;
    public PokemonsController(IPokemonService pokemonService)
    {
        _pokemonService = pokemonService;
    }

    [HttpGet]
    public async Task<ActionResult<PagedResponse<PokemonResponse>>> GetPokemonsAsync(
        [FromQuery] string? name,
        [FromQuery] string? type,
        CancellationToken cancellationToken,
        [FromQuery] int pageNumber = 1,
        [FromQuery] int pageSize = 10,
        [FromQuery] string orderBy = "Name",
        [FromQuery] string orderDirection = "asc")
    {
        var pokemons = await _pokemonService.GetPokemonsAsync(name, type, pageNumber, pageSize, orderBy, orderDirection, cancellationToken);
        return Ok(pokemons);
    }

    //Http Status
    // 200 OK (Si existe el pokemon)
    //400 Bad Request (Id invalido) --- Casi no se usa
    // 404 Not Found (no existe el pokemon)
    // 500 Internal Server Error (Error del servidor)

    //Http Verb -Get
    [HttpGet("{id}", Name = "GetPokemonByIdAsync")]
    public async Task<ActionResult<PokemonResponse>> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        var pokemon = await _pokemonService.GetPokemonByIdAsync(id, cancellationToken);
        return pokemon is null ? NotFound() : Ok(pokemon.ToResponse());
    }

    //Http Verb - Post
    //Http Status
    // 400 Bad Request (Si usuario manda informacion erronea)
    // 409 Conflict (Si el pokemon ya existe)
    // 422 Unprocessable Entity (Si el pokemon no cumple con las reglas de negocio interna)
    // 500 Internal Server Error (Error del servidor)
    // 200 OK (El recurso creado + id) no sigue muchos para buenas practicas de RESTFul
    // 201 Created (El recurso creado + id) href = hace referencia al get para obtener el recurso
    // 202 Accepted (Si la creacion del recurso es asincrona y toma tiempo)

    [HttpPost]
    public async Task<ActionResult<PokemonResponse>> CreatePokemonAsync([FromBody] CreatePokemonRequest createPokemon, CancellationToken cancellationToken)
    {
        try
        {
            if (!IsValidAttack(createPokemon))
            {
                return BadRequest(new { Message = "Attack does not have a valid value" });
            }

            var pokemon = await _pokemonService.CreatePokemonAsync(createPokemon.ToModel(), cancellationToken);

            // 201
            return CreatedAtRoute("GetPokemonByIdAsync", new { id = pokemon.Id }, pokemon.ToResponse());
        }
        catch (PokemonAlreadyExistsException e)
        {
            // 409 Conflict - { Message = "Pokemon NAME already exists" }
            return Conflict(new { Message = e.Message });
        }
    }


    //LocalHost:Port/api/v1/pokemons/ID
    //Http Verb - Delete
    //Http Status
    // 200 OK (Si borro) No sigue muy bien el RESTFUL
    // 204 No Content (Si se borro correctamente)
    // 404 Not Found (Si el pokemon no existe)
    // 500 Internal Server Error (Error del servidor)

    [HttpDelete("{id}")]
    public async Task<ActionResult> DeletePokemonAsync(Guid id, CancellationToken cancellationToken)
    {
        try
        {
            await _pokemonService.DeletePokemonAsync(id, cancellationToken);
            return NoContent(); // 204 No Content
        }
        catch (PokemonNotFoundException)
        {
            return NotFound(); // 404 Not Found
        }
    }

    private static bool IsValidAttack(CreatePokemonRequest createPokemon)
    {
        return createPokemon.Stats.Attack > 0;
    }
}