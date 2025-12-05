using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using SendGrid;


public class EmailService
{
    private readonly ILogger<EmailService> _logger;
    private readonly IConfiguration _configuration;
    private readonly SendGridClient _client;
    private readonly string _senderEmail;
    private readonly string _senderName;

    public EmailService(ILogger<EmailService> logger, IConfiguration configuration)
    {
        _logger = logger;
        _configuration = configuration;
        _senderEmail = configuration["SendGrid:SenderEmail"];
        _senderName = configuration["SendGrid:SenderName"];
        _client = new SendGridClient(configuration["SendGrid:ApiKey"]);
    }

    public async Task SendWelcomeEmailAsync(string toEmail, string trainerName)
    {
        var subject = "Bienvenido al equipo :D";
        var htmlContent = $"<html><body><h1>Bienvenido {trainerName}!</h1><p>Gracias por unirte</p></body></html>";
        await SendEmail(toEmail, trainerName, subject, htmlContent);
    }

    private async Task SendEmail(string toEmail, string toName, string subject, string htmlContent)
    {
        var from = new SendGrid.Helpers.Mail.EmailAddress(_senderEmail, _senderName);
        var to = new SendGrid.Helpers.Mail.EmailAddress(toEmail, toName);
        var msg = SendGrid.Helpers.Mail.MailHelper.CreateSingleEmail(from, to, subject, 
        plainTextContent:"", htmlContent: htmlContent);

        try {
            var response = await _client.SendEmailAsync(msg);
            if (response.IsSuccessStatusCode)
            {
                _logger.LogInformation("Email sent succesfully to {Email}", toEmail);
            }
            else
            {
                _logger.LogError("Failed to send email to {Email}. {StatusCode}", toEmail, response.StatusCode);
            }
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Exception occurred while sending email to {Email}", toEmail);
        }
    }
}