package LKManager.user;

import LKManager.model.CustomUserDetails;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.Schedule;
import LKManager.model.ScheduleStatus;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.services.Adapters.UserAdapter;
import LKManager.services.ScheduleService;
import LKManager.services.UserService;
import LKManager.services.UserSettingsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@Data
@SessionAttributes("model")
public class userSettingsController {
private  final UserService userService;
private final UserSettingsService userSettingsService;
private final ScheduleService scheduleService;

    @GetMapping("/user/settings")
public String getSettings( Model model) throws IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();  // Pobiera nazwę użytkownika (username)


UserDataDTO userDataDTO= UserAdapter.convertUserDataToUserDataDTO( userService.getUserById(customUserDetails.getId()));
Schedule upcomingSchedule=scheduleService.getSchedules().stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst().orElse(null);
//todo temp\/

TMcity tMcity= new TMcity(0L,"Adminowo");
  //  TMcity tMcity= new TMcity(0L,null);
MainModel mainModel= new MainModel(userDataDTO,tMcity,upcomingSchedule);
model.addAttribute("model",mainModel);



    return "/user/settings/userSettings";
}

    @PostMapping("/userSettings/subscribeLeague")
    public String subscribeLeague(Model model, @ModelAttribute("model") MainModel formModel , @RequestParam("checkboxLeagueParticipation") String checkboxLeagueParticipation) {
        boolean success= false;
        if(checkboxLeagueParticipation.equals("on,SUBBED"))
        {
            success=userSettingsService.subscribeLeague(formModel.getUser().getUserId(),LeagueParticipation.SUBBED);
        }
   else {
            success=userSettingsService.subscribeLeague(formModel.getUser().getUserId(),LeagueParticipation.UNSIGNED);

        }
        if (success) {
            model.addAttribute("message", "You have successfully joined the league!");
        } else {
            model.addAttribute("error", "There was a problem joining the league.");
        }
        return"redirect:/user/settings";
    }
@PostMapping("/userSettings/joinLeague")
public String joinLeague(Model model, @ModelAttribute("model") MainModel formModel )
{
//scheduleService.planSchedule(LocalDate.now(),"blabla", ScheduleType.standardSchedule);
    boolean success= false;

            success=userSettingsService.joinLeague(formModel.getUser().getUserId());





   if (success) {
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
public String leaveLeague(Model model, @ModelAttribute("model") MainModel formModel)
{
    boolean success=userSettingsService.leaveLeague(formModel.getUser().getUserId());

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

    public String changeEmail()
    {
        return "";
    }














    //todo temp\/
@Data
@AllArgsConstructor
public class TMcity{
     private     Long Id;
        private String name;
}
@Data
@AllArgsConstructor
    public class MainModel {
        private UserDataDTO user;
        private TMcity tmCity;
        private Schedule upcomingSchedule;
    }


















}




