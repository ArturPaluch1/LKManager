package LKManager.services;

import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.UserMZ.UserData;
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
boolean joinLeague()
{
//scheduleService.addToLeagueListOfParticipants();

return true;
}

@Transactional
    public boolean joinLeague(Long userId) {

    UserData changedUser=null;
    boolean success=false;

         changedUser =userService.changeUserLeagueParticipation(userId,LeagueParticipation.SIGNED);
           success=  scheduleService.addLeagueParticipant(userId);






if(changedUser!=null)
{
    //todo tu moznaby dodac add user to redis, ale nie jest używane to aktualizuje tylko listy userów
redisUserService.editUserInUserLists(changedUser);
    return success;
}
else
    return false;

    }

    @Transactional
    public boolean subscribeLeague(Long userId,LeagueParticipation leagueParticipation) {

        UserData changedUser=null;
        boolean success=false;
        switch (leagueParticipation)
        {

            case SUBBED:
            {
                changedUser =userService.changeUserLeagueParticipation(userId,LeagueParticipation.SUBBED);
                UserData finalChangedUser = changedUser;
                if(scheduleService.getLeagueParticipants().stream().filter(lp->lp.getId().equals(finalChangedUser.getUserId())).findFirst().orElse(null)==null)
                {  scheduleService.addLeagueParticipant(userId);}
                success=true;
                break;
            }
            case UNSIGNED:
            {
                changedUser =userService.changeUserLeagueParticipation(userId,LeagueParticipation.UNSIGNED);
                success=  scheduleService.removeLeagueParticipant(userId);
                break;
            }
        }


        if(changedUser!=null)
        {
            //todo tu moznaby dodac add user to redis, ale nie jest używane to aktualizuje tylko listy userów
            redisUserService.editUserInUserLists(changedUser);
            return success;
        }
        else
            return false;

    }


    public boolean leaveLeague(Long userId)
    {
        boolean success=  scheduleService.removeLeagueParticipant(userId);
        UserData changedUser=userService.changeUserLeagueParticipation(userId,LeagueParticipation.UNSIGNED);
        if(changedUser!=null)
        {
            //todo tu moznaby dodac add user to redis, ale nie jest używane to aktualizuje tylko listy userów
            redisUserService.editUserInUserLists(changedUser);
            return success;
        }
        else
            return false;

    }
}
