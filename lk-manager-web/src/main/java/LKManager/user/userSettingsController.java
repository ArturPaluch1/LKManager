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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

UserSettingsFormModel.TMcity tMcity= new UserSettingsFormModel.TMcity(0L,"Adminowo");
  //  TMcity tMcity= new TMcity(0L,null);
UserSettingsFormModel mainModel= new UserSettingsFormModel(userDataDTO,userEmail,tMcity,upcomingSchedule);
model.addAttribute("model",mainModel);



    return "user/settings/userSettings";
}
    @PostMapping("/userSettings/setMZUsername")
    public String setMZUsername(Model model, @ModelAttribute("model") UserSettingsFormModel formModel , @RequestParam("username") String username)
        {

userService.setMZUser(formModel.getUser().getUsername(),username);
    //    User userInDB= userService.getUserById(formModel.user.getUserId());

          //  userService.getMZUserDataByUsername(username);


            return"redirect:/user/settings";
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

                 if( emailService.sendEmail(userId, email)==false)
                 {
                     redirectAttributes.addFlashAttribute("emailMessage", "Podałeś błędny mail");
                     return"redirect:/user/settings";
                 }






              redirectAttributes.addFlashAttribute("emailMessage", "Do dokończenia aktywacji wejdź w link w mailu (mz_helper@op.pl).<br>(Czasem mail może trafić do spamu.)");
          }
          catch (Exception e)
          {
              redirectAttributes.addFlashAttribute("emailMessage", "Błąd w ustawianiu maila.");
          }
           finally {
              return"redirect:/user/settings";

          }





    }














}




