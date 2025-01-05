package LKManager.Login;

import LKManager.DAO_SQL.UserDAO;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.account.SignUpForm;
import LKManager.model.account.User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Data
public class signUpController {
    private final AccountService accountService;
private final EmailService emailService;
private final UserService userService;
private final PasswordEncoder passwordEncoder;

private final UserDAO userDAO;
    @GetMapping("/public/signUp")
    public String signUp(Model model)
    {

        if (!model.containsAttribute("signUpForm")) {
            // Jeśli nie ma formularza w modelu, stwórz nowy, pusty
            model.addAttribute("signUpForm", new SignUpForm(null, null, null));
        }

        return "public/signUp";
    }

    @PostMapping("/public/signUp")
    public String signUp(@ModelAttribute("signUpForm") @Valid SignUpForm signUpForm,  BindingResult result,RedirectAttributes redirectAttributes) throws Exception {
        if (result.hasErrors()) {
            // Jeśli są błędy walidacji, wróć do formularza
            //   redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.signUpForm", result);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.signUpForm", result);
            redirectAttributes.addFlashAttribute("signUpForm", signUpForm);
            return "redirect:/public/signUp";
            // return"/public/signUp";
        } else {
            // UserData user= accountService.createAccount(signUpForm);


            //todo error
            //    return "/public/signUp";

            String email = signUpForm.getEmail();


            //checking if chosen username already exists in database
            if (userService.getUserByUsername(signUpForm.getUsername()) == null) {
                String encodedPassword = passwordEncoder.encode(signUpForm.getPassword());
                signUpForm.setPassword(encodedPassword);
                signUpForm.setEmail(null);
                UserDataDTO user = userService.addUser(signUpForm);


                if (user != null) {
                    if (!email.equals("")) {


                        if (emailService.sendEmail(user.getUserId(), email) == false) {
                            userService.authenticateUser(userDAO.getUserById(user.getUserId()).get());
                            redirectAttributes.addFlashAttribute("message", "Konto zostało założone, ale podałeś błędny mail. Możesz ustawić go w opcjach konta.");
                            return "redirect:/user/settings";
                        } else {
                            userService.authenticateUser(userDAO.getUserById(user.getUserId()).get());
                            redirectAttributes.addFlashAttribute("message", "Konto zostało założone. Sprawdź email żeby potwierdzić maila.");
                            return "redirect:/user/settings";
                        }


                    }

                    Optional<User> userToAuthentication = userDAO.getUserById(user.getUserId());
                    if (userToAuthentication.isPresent()) {
                        userService.authenticateUser(userToAuthentication.get());
                        //     redirectAttributes.addFlashAttribute( "success","Zarejestrowano! Email możesz aktywować w opcjach.");
                        return "redirect:/user/settings";
                    } else {
                        redirectAttributes.addFlashAttribute("message", "wystąpił błąd z autentykacją");
                        return "redirect:/public/signUp";
                    }
                } else {
                    // nie zapisany user
                    redirectAttributes.addFlashAttribute("message", "Błąd w zapisywaniu usera");
                    return "redirect:/public/signUp";
                }


            } else {

                redirectAttributes.addFlashAttribute("message", "Taka nazwa jest już zajęta");
                return "redirect:/public/signUp";
            }


        }


    }

}
