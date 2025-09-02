using System.ServiceModel;
using MobApi.Dtos;

namespace MobApi.Validators;

public static class MobValidator{
    public static CreateMobDto ValidateName(this CreateMobDto mob) =>
    string.IsNullOrEmpty(mob.Name) ? throw new FaultException("Mob name is required") : mob;

    public static CreateMobDto ValidateType(this CreateMobDto mob) =>
    string.IsNullOrEmpty(mob.Type) ? throw new FaultException("Mob Type is required") : mob;

    public static StatsDto ValidateHP(this StatsDto mob) =>
    mob.HP <= 0 ? throw new FaultException("Mob HP must be greater than 0") : mob;

}