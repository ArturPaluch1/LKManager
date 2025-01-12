package LKManager.services;

import LKManager.DAO_SQL.LeagueParticipantsDAO;
import LKManager.Security.UserAuthenticationDetailsService;
import LKManager.model.account.User;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.account.UserSettingsFormModel;
import LKManager.services.RedisService.RedisUserService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class UserSettingsService {
private final ScheduleService scheduleService;
private final UserService userService;
private final RedisUserService redisUserService;
private final LeagueParticipantsDAO leagueParticipantsDAO;
private final UserAuthenticationDetailsService userAuthenticationDetailsService;
boolean joinLeague()
{
//scheduleService.addToLeagueListOfParticipants();

return true;
}

@Transactional
    public boolean joinLeague(UserSettingsFormModel userSettingsFormModel) {

    User changedUser=null;


         changedUser =userService.changeUserLeagueParticipation(userSettingsFormModel,LeagueParticipation.SIGNED);


         //  success=  scheduleService.addLeagueParticipant(username);






if(changedUser!=null)
{

    //todo tu moznaby dodac add user to redis, ale nie jest używane to aktualizuje tylko listy userów
redisUserService.saveOrUpdateUserInUserLists(changedUser);
userAuthenticationDetailsService.updateAuthentication(changedUser);
    return true;
}
else
    return false;

    }

    @Transactional
    public boolean subscribeLeague(UserSettingsFormModel userSettingsFormModel,LeagueParticipation leagueParticipation) {

        User changedUser=null;
        boolean success=false;
        switch (leagueParticipation)
        {
            case SIGNED:
            {
                changedUser =userService.changeUserLeagueParticipation(userSettingsFormModel,LeagueParticipation.SIGNED);

           //     success=  scheduleService.addLeagueParticipant(userSettingsFormModel.getUser());
                break;
            }
            case SUBBED:
            {
                changedUser =userService.changeUserLeagueParticipation(userSettingsFormModel,LeagueParticipation.SUBBED);
               /* User finalChangedUser = changedUser;
                if(leagueParticipantsDAO.findAll().stream().filter(lp->lp.getUser().getMzUser().equals(finalChangedUser.getMzUser())).findFirst().orElse(null)==null)
                {  scheduleService.addLeagueParticipant(username);}
                success=true;*/
                break;
            }
            case UNSIGNED:
            {
                changedUser =userService.changeUserLeagueParticipation(userSettingsFormModel,LeagueParticipation.UNSIGNED);
             //   success=  userService.removeLeagueParticipant(username);
                break;
            }
        }


        if(changedUser!=null)
        {
            //todo tu moznaby dodac add user to redis, ale nie jest używane to aktualizuje tylko listy userów

            redisUserService.saveOrUpdateUserInUserLists(changedUser);
            userAuthenticationDetailsService.updateAuthentication(changedUser);
            return success;
        }
        else
            return false;

    }

@Transactional
    public boolean leaveLeague(UserSettingsFormModel userSettingsFormModel)
    {

      //  boolean success=  scheduleService.removeLeagueParticipant(userSettingsFormModel);
        User changedUser=userService.changeUserLeagueParticipation(userSettingsFormModel,LeagueParticipation.UNSIGNED);

        if(changedUser!=null)
        {
            //todo tu moznaby dodac add user to redis, ale nie jest używane to aktualizuje tylko listy userów
            redisUserService.saveOrUpdateUserInUserLists(changedUser);
            userAuthenticationDetailsService.updateAuthentication(changedUser);
            return true;
        }
        else
            return false;

    }
}
