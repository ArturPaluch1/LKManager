package LKManager.Login;

import LKManager.model.account.LogInForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class logInController {

    @GetMapping("/public/logIn")
  public String  logIn(@RequestParam(value = "error", required = false) String error, Model model)
    {

        if (error != null) {
            model.addAttribute("errorMessage", "Niepoprawna nazwa użytkownika lub hasło.");

        }

            model.addAttribute("logInForm", new LogInForm());
            System.out.println("Wywołano metodę logIn");

            return "/public/logIn";


    }


    /*@PostMapping("/public/logIn")
    public String  logIn(@ModelAttribute("logInForm") LogInForm logInForm)
    {
        return "redirect:/public/LK/schedule/schedule";
       // return "redirect:LK/logIn.html";
    }*/

}
