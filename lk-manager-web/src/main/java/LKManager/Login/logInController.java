package LKManager.Login;

import LKManager.model.account.ForgotPasswordForm;
import LKManager.model.account.LogInForm;
import LKManager.services.AccountService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Data
public class logInController {

    private final AccountService accountService;
    @Value("${spring.security.rememberme.key}")
    private  String rememberMeKey;
    @GetMapping("/public/logIn")
  public String  logIn(@RequestParam(value = "error", required = false) String error,
                       Model model)
    {

        if (error != null) {
            model.addAttribute("errorMessage", "Niepoprawna nazwa użytkownika lub hasło.");

        }

            model.addAttribute("logInForm", new LogInForm());
            System.out.println("Wywołano metodę logIn");

            return "public/logIn";


    }


    /*@PostMapping("/public/logIn")
    public String  logIn(@ModelAttribute("logInForm") LogInForm logInForm)
    {
        return "redirect:/public/LK/schedule/schedule";
       // return "redirect:LK/logIn.html";
    }*/


    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordForm", new ForgotPasswordForm("",""));
        return "public/forgot-password";
    }
    @PostMapping("/public/forgot-password")
    public String changePasswordResult(@ModelAttribute("forgotPasswordForm") ForgotPasswordForm forgotPasswordForm, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (accountService.checkUserAndSendConfirmationEmail(forgotPasswordForm.username(), forgotPasswordForm.email())) {


            return "/public/forgot-password-resultPage";
        } else {
            model.addAttribute("message","Ten email nie pasuje do usera.");
            model.addAttribute("logInForm", new LogInForm());
            return "public/forgot-password";

        }
    }
}
