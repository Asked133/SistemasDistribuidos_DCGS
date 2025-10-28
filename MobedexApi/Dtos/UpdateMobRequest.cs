namespace MobedexApi.Dtos
{
    public class UpdateMobRequest
    {
        public string Name { get; set; }
        public string Type { get; set; }
        public string Behavior { get; set; }
        
        public StatsRequest Stats { get; set; }
    }
}