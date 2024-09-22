package LKManager.Login;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.account.SignUpForm;
import LKManager.services.AccountService;
import LKManager.services.EmailService;
import LKManager.services.UserService;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Data
public class signUpController {
    private final AccountService accountService;
private final EmailService emailService;
private final UserService userService;
private final PasswordEncoder passwordEncoder;
    @GetMapping("/public/signUp")
    public String signUp(Model model)
    {
        model.addAttribute("signUpForm",new SignUpForm(null,null,null));
        return "/public/signUp";
    }

    @PostMapping("/public/signUp")
    public String signUp(@ModelAttribute("signUpForm") @Valid SignUpForm signUpForm,   BindingResult result)
    {
        if (result.hasErrors()) {
            // Jeśli są błędy walidacji, wróć do formularza
            return "/public/signUp";
        }
        else
        {
           // UserData user= accountService.createAccount(signUpForm);
            String encodedPassword = passwordEncoder.encode(signUpForm.getPassword());
            signUpForm.setPassword(encodedPassword);
UserDataDTO user=userService.addUser(signUpForm);
            if(user==null)
            {
                //todo error
                return "/public/signUp";
            }
            else
            {
                String activationLink = "http://localhost:8080/confirmEmail?token=" + accountService.generateActivationToken(user.getUsername());

                emailService.sendActivationEmail(signUpForm.getEmail(),activationLink);
                //todo tutaj przeniesc do strony "konto założone. Sprawdź swój e-mail, aby go aktywować. Jest wymagany do odzyskiwania konta."
                return "Zarejestrowano! Sprawdź swój e-mail, aby aktywować konto.";
            }
        }


    }
}
