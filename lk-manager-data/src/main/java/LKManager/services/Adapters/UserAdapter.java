package LKManager.services.Adapters;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.RecordsAndDTO.UserMzDTO;
import LKManager.model.account.User;
import LKManager.model.UserMZ.MZUserData;

public class UserAdapter {

    public static UserDataDTO convertUserToUserDataDTO(User user)
    {
        Integer teamId;
        String teamName;

        if(user.getMzUser()!=null)
        {
            if(user.getMzUser().getTeamlist().size()!=0)//pause do not have a team
            {
                teamId= user.getMzUser().getTeamlist().get(0).getTeamId();
                teamName= user.getMzUser().getTeamlist().get(0).getTeamName();

            }

            else  {
                teamId=null;
                teamName=null;
            }
        }


        UserDataDTO userDTO = new UserDataDTO(user);



        return userDTO;
    }
    public static UserMzDTO convertMZUserDataToUserMzDTO(MZUserData MZUserData)
    {
        Integer teamId;
        String teamName;

if(MZUserData==null)
{
return null;
}
      else  if(MZUserData.getTeamlist().size()!=0)//pause do not have a team
        {
            teamId= MZUserData.getTeamlist().get(0).getTeamId();
            teamName= MZUserData.getTeamlist().get(0).getTeamName();

        }

        else  {
            teamId=null;
            teamName=null;
        }

        UserMzDTO userDTO = new UserMzDTO(MZUserData.getMZuser_id(), MZUserData.getUsername(),teamId,teamName);



        return userDTO;
    }


}
