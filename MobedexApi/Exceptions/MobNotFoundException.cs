namespace MobedexApi.Exceptions;

public class MobNotFoundException : Exception
{
    public MobNotFoundException(Guid id) : base($"Mob with id {id} was not found")
    {
    }
}