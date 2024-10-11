package LKManager.services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service


public class EmailService {

    private final String applicationServer;

    private final JavaMailSender mailSender;
   private final AccountService accountService;
   // private static final Dotenv dotenv = Dotenv.load();
private final String serviceEmail;

    private final String mailjetApiKey;


    private final String mailjetSecretKey;
    public EmailService(@Value("${spring.application.server}")String applicationServer, JavaMailSender mailSender, AccountService accountService, @Value("${spring.application.email}")String serviceEmail, @Value("${mailjet.api-key}") String mailjetApiKey, @Value("${mailjet.apiSecretKey}") String mailjetSecretKey){
        this.applicationServer = applicationServer;
        this.mailSender = mailSender;
        this.accountService = accountService;
        this.serviceEmail = serviceEmail;
        this.mailjetApiKey=mailjetApiKey;
        this.mailjetSecretKey=mailjetSecretKey;
    }

    public boolean sendActivationEmail(String to, String activationLink) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);

      //  message.setFrom("MZ_Activation@hotmail.com");
      //  message.setFrom(dotenv.get("email-email"));
        message.setFrom(serviceEmail);
        message.setSubject("Aktywacja konta TM");
        message.setText("Kliknij w poniższy link, aby aktywować swoje konto: " + activationLink);
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean sendEmail_SimpleMailMessage(Long userId, String email) {
        // Generowanie linku aktywacyjnego


        String token = accountService.generateActivationToken(userId.toString());

// Kodowanie wartości URL
    /*    String activationLink = "http://localhost:8080/confirmEmail?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
*/
        String activationLink = applicationServer+"/confirmEmail?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

        boolean result= this.sendActivationEmail( email, activationLink);
       return result;
    }

    public boolean sendEmail(Long userId, String email) throws MailjetException {
        String token = accountService.generateActivationToken(userId.toString());
        String activationLink = applicationServer+"/confirmEmail?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

      try{  MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        ClientOptions options = ClientOptions.builder()
                .apiKey(mailjetApiKey)
                .apiSecretKey(mailjetSecretKey)
                .build();

         client = new MailjetClient( options);
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", serviceEmail)
                                        .put("Name", "Aktywacja emaila ligi MZ"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", "passenger 1")))
                                .put(Emailv31.Message.SUBJECT, "Aktywacja emaila ligi MZ")
                                //.put(Emailv31.Message.TEXTPART, "Aktywacja Email ligi MZ<br>Kliknij w poniższy link, aby aktywować swoje konto: " + activationLink)
                                .put(Emailv31.Message.HTMLPART, "<h3>Kliknij w poniższy link, aby aktywować maila:</h3><br /><a href=\"" + activationLink + "\">Aktywuj maila</a>")));

          response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
        return true;
    }
      catch(Exception e)
      {
          return false;
      }
    }


}