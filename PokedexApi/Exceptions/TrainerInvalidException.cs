namespace PokedexApi.Exceptions;    

public class TrainerInvalidException : Exception
{
    public TrainerInvalidException(string id) : base($"Invalid Trainer ID: {id}")
    {
    }
}