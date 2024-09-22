package LKManager.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {


    private JavaMailSender mailSender;

    public void sendActivationEmail(String to, String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("MZ_Activation@hotmail.com");
        message.setSubject("Aktywacja konta TM");
        message.setText("Kliknij w poniższy link, aby aktywować swoje konto: " + activationLink);

        mailSender.send(message);
    }
}