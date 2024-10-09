package LKManager.services;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;
   private final AccountService accountService;
    private static final Dotenv dotenv = Dotenv.load();

    public boolean sendActivationEmail(String to, String activationLink) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);

      //  message.setFrom("MZ_Activation@hotmail.com");
        message.setFrom(dotenv.get("email-email"));
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
        String activationLink = "http://localhost:8080/confirmEmail?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);


       return( this.sendActivationEmail( email, activationLink))==true?true:false;
    }

}