namespace FileDownload;

class Program
{
    public static async Task Main(string[] args)
    {
        var cancellationToken = new CancellationTokenSource().Token;
        var peer = new Peer();

        var task = peer.Start(cancellationToken);

        if (args.Length > 0 && args[0] == "download")
        {
            //Esperar args[1] (IP)
            //Esperar args[2] (Puerto)
            //Esperar args[3] (Nombre de archivo)
            //Esperar args[4] (Ruta de guardado)
            await peer.DownloadFileAsync(args[1], int.Parse(args[2]), args[3], args[4], cancellationToken);
        }
        else
        {
            Console.WriteLine("Waiting for other peers...");
        }
        await task;
    }
}