package LKManager.services;

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
private final String email;
    public EmailService(@Value("${spring.application.server}")String applicationServer, JavaMailSender mailSender, AccountService accountService, @Value("${spring.application.email}")String email) {
        this.applicationServer = applicationServer;
        this.mailSender = mailSender;
        this.accountService = accountService;
        this.email = email;
    }

    public boolean sendActivationEmail(String to, String activationLink) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);

      //  message.setFrom("MZ_Activation@hotmail.com");
      //  message.setFrom(dotenv.get("email-email"));
        message.setFrom(email);
        message.setSubject("Aktywacja konta TM");
        message.setText("Kliknij w poniższy link, aby aktywować swoje konto: " + activationLink);
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean sendEmail(Long userId, String email) {
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

}