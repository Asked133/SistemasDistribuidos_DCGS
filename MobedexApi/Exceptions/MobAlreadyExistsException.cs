namespace MobedexApi.Exceptions;

public class MobAlreadyExistsException : Exception
{
    public MobAlreadyExistsException(string name) : base($"Mob {name} already exists")
    {
    }
}