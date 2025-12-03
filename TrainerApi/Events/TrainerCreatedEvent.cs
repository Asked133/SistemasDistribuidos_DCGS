namespace TrainerApi.Events;
using System.Text.Json.Serialization;

public class TrainerCreatedEvent : IEventMessage
{
    public string Id { get; set; }
    public string Name { get; set; }
    public int Age { get; set; }
    public DateTime Birthdate { get; set; }
    public DateTime CreatedAt { get; set; }
    public List<MedalEvent> Medals { get; set; } = new();   

    [JsonIgnore]
    public string Topic => "trainer.created";

    public string? GetEventKey() => Id;
}