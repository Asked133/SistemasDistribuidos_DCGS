using Microsoft.AspNetCore.Mvc;
using PokedexApi.Dtos;

namespace PokedexApi.Controllers;

[ApiController]
[Route("api/v1/[controller]")]
public class PokemonsController : ControllerBase
{
    [HttpGet("{id}")] // api/v1/pokemons/{id}
    public async Task<ActionResult<PokemonResponse>> GetPokemonByIdAsync(Guid id, CancellationToken cancellationToken)
    {
        //Http 200 - OK
        return Ok(); 
    }
}