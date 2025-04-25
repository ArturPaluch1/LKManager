package LKManager.services;

import LKManager.services.RedisService.RedisUserService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service


public class EmailService {

    private final String applicationServer;

    private final JavaMailSender mailSender;

   // private static final Dotenv dotenv = Dotenv.load();
private final String serviceEmail;
private final RedisUserService redisUserService;
    private final String mailjetApiKey;


    private final String mailjetSecretKey;
    public EmailService(@Value("${spring.application.server}")String applicationServer, JavaMailSender mailSender, @Value("${spring.application.email}")String serviceEmail, RedisUserService redisUserService, @Value("${mailjet.api-key}") String mailjetApiKey, @Value("${mailjet.apiSecretKey}") String mailjetSecretKey){
        this.applicationServer = applicationServer;
        this.mailSender = mailSender;

        this.serviceEmail = serviceEmail;
        this.redisUserService = redisUserService;
        this.mailjetApiKey=mailjetApiKey;
        this.mailjetSecretKey=mailjetSecretKey;
    }

    private String generateToken(String user) {
        String token = UUID.randomUUID().toString();
        redisUserService.setActivationToken(token,user);

        return token;
    }

    public boolean sendActivationEmail(Long userId, String email) throws MailjetException {
        String token = generateToken(userId.toString());
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
                                .put(Emailv31.Message.HTMLPART, "<h3>Kliknij w poniższy link, aby aktywować maila, lub skopiuj go do przeglądarki:</h3><br /><a href=\"" + activationLink + "\">"+activationLink +"</a>")));


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

    public boolean sendPasswordChangeEmail(String username, String email) throws MailjetException {
        String token = generateToken(username);
        String activationLink = applicationServer+"/passwordChangeConfirmEmail?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
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
                                            .put("Name", "Potwierdzenie emaila przy zmianie hasła"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", email)
                                                    .put("Name", "passenger 1")))
                                    .put(Emailv31.Message.SUBJECT, "Potwierdzenie emaila przy zmianie hasła")
                                    //.put(Emailv31.Message.TEXTPART, "Aktywacja Email ligi MZ<br>Kliknij w poniższy link, aby aktywować swoje konto: " + activationLink)
                                    .put(Emailv31.Message.HTMLPART, "<h3>Kliknij w poniższy link, aby aktywować maila, lub skopiuj go do przeglądarki aby zmienić hasło:</h3><br /><a href=\"" + activationLink + "\">" +activationLink+"</a>")));


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