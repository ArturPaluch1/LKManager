package LKManager.user;

import LKManager.Security.UserAuthenticationDetailsService;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.Schedule;
import LKManager.model.ScheduleStatus;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.account.User;
import LKManager.model.account.UserSettingsFormModel;
import LKManager.services.*;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Data
@SessionAttributes("model")
public class userSettingsController {
private  final UserService userService;
private final UserSettingsService userSettingsService;
private final ScheduleService scheduleService;
private  final MZUserService mzUserService;
private final EmailService emailService;
private final AccountService accountService;
private final UserAuthenticationDetailsService userAuthenticationDetailsService;
    @GetMapping("/user/settings")
public String getSettings( Model model) throws Exception {
//todo to przenieść do jakiegoś serwisu?
 //   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  //  User customUserDetails = (User)authentication.getPrincipal();  // Pobiera nazwę użytkownika (username)

        User customUserDetails =userAuthenticationDetailsService.getCustomUserDetails();


UserDataDTO userDataDTO=  userService.getUserById(customUserDetails.getId());
Schedule upcomingSchedule=scheduleService.getSchedules().stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst().orElse(null);
String userEmail=userService.getUsersEmail(customUserDetails.getId());





        //todo temp\/
        if (!model.containsAttribute("org.springframework.validation.BindingResult.model")) {
            UserSettingsFormModel.TMcity tMcity = new UserSettingsFormModel.TMcity(0L, "Adminowo");
            //  TMcity tMcity= new TMcity(0L,null);
            UserSettingsFormModel mainModel = new UserSettingsFormModel(userDataDTO, userEmail, tMcity, upcomingSchedule, "");
            model.addAttribute("model", mainModel);

        }

        return "user/settings/userSettings";
}
    @PostMapping("/userSettings/setMZUsername")
    public String setMZUsername( @ModelAttribute("model") UserSettingsFormModel formModel , @RequestParam("username") String mzUsername)
        {

userService.setMZUser(formModel.getUser().getUsername(),mzUsername);
    //    User userInDB= userService.getUserById(formModel.user.getUserId());

          //  userService.getMZUserDataByUsername(username);


            return"redirect:/user/settings";
        }
    @PostMapping("/userSettings/setPassword")
    public String setPassword(@ModelAttribute("model") @Valid UserSettingsFormModel formModel, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Jeśli są błędy walidacji, wróć do formularza
            //   redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.signUpForm", result);
            redirectAttributes.addFlashAttribute("model", formModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.model", result);

            return "redirect:/user/settings";
            // return"/public/signUp";
        } else {

            if (userService.setPassword(formModel.getUser(), formModel.getPassword())) {
                redirectAttributes.addFlashAttribute("passwordChangeSuccess", "Zmiana hasła powiodła się.");

                return "redirect:/user/settings";
            } else {
                redirectAttributes.addFlashAttribute("passwordChangeError", "Nie udało się zmienić hasła.");

                return "redirect:/user/settings";
            }
        }

    }

        @PostMapping("/userSettings/subscribeLeague")
    public String subscribeLeague(Model model, @ModelAttribute("model") UserSettingsFormModel formModel , @RequestParam("checkboxLeagueParticipation") String checkboxLeagueParticipation) {
        boolean success= false;
        if(checkboxLeagueParticipation.equals("on,SUBBED"))
        {
            success=userSettingsService.subscribeLeague(formModel,LeagueParticipation.SUBBED);
        }
   else {
            success=userSettingsService.subscribeLeague(formModel,LeagueParticipation.UNSIGNED);

        }
        if (success) {
            model.addAttribute("message", "You have successfully joined the league!");
        } else {
            model.addAttribute("error", "There was a problem joining the league.");
        }
        return"redirect:/user/settings";
    }
@PostMapping("/userSettings/joinLeague")
public String joinLeague(Model model, @ModelAttribute("model") UserSettingsFormModel userFormModel )
{
//scheduleService.planSchedule(LocalDate.now(),"blabla", ScheduleType.standardSchedule);
    boolean success= false;

            success=userSettingsService.joinLeague(userFormModel);





   if (success) {
       User customUserDetails =userAuthenticationDetailsService.getCustomUserDetails();

       model.addAttribute("message", "You have successfully joined the league!");
    } else {
        model.addAttribute("error", "There was a problem joining the league.");
    }
return"redirect:/user/settings";
}

/*@PostMapping("/userSettings/changeSubscriptionLeagueOption")
public  String changeSubscriptionLeagueOption(Model model, @ModelAttribute("model") MainModel formModel)
{
    boolean success=userSettingsService.joinLeague(formModel.getUser().getUserId());

    if (success) {
        model.addAttribute("message", "You have successfully subscribed the league!");
    } else {
        model.addAttribute("error", "There was a problem in subscribing the league.");
    }
    return"redirect:/user/settings";
}*/
@PostMapping("/userSettings/leaveLeague")
public String leaveLeague(Model model, @ModelAttribute("model") UserSettingsFormModel formModel)
{
    boolean success=userSettingsService.leaveLeague(formModel);

    if (success) {
        model.addAttribute("message", "You have successfully leaved the league!");
    } else {
        model.addAttribute("error", "There was a problem leaving the league.");
    }
    return"redirect:/user/settings";
}

public String subscribeLeague()
{
    return "";
}

public String leaveLeague()
{
    return "";
}


        @PostMapping(value = "/setEmail")
        public String setEmail(@ModelAttribute("model") UserSettingsFormModel formModel,RedirectAttributes redirectAttributes ) {



          try {
              //String activationLink = "http://localhost:8080/confirmEmail?token=" + accountService.generateActivationToken(formModel.getUser().getUserId().toString()) + "&email=" + formModel.getUser().getEmail();

              Long userId = formModel.getUser().getUserId();
              String email = formModel.getUserEmail();

                 if( emailService.sendActivationEmail(userId, email)==false)
                 {
                     redirectAttributes.addFlashAttribute("emailMessage", "Podałeś błędny mail");
                     return"redirect:/user/settings";
                 }






              redirectAttributes.addFlashAttribute("checkMailboxMessage", "Do dokończenia aktywacji wejdź w link w mailu. (Prawdopodobnie mail trafi do spamu...)");
          }
          catch (Exception e)
          {
              redirectAttributes.addFlashAttribute("emailErrorMessage", "Błąd w ustawianiu maila.");
          }
           finally {
              return"redirect:/user/settings";

          }





    }














}




