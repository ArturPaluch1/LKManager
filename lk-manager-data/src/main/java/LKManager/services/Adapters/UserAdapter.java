package LKManager.services.Adapters;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.UserMZ.UserData;

public class UserAdapter {
    public static UserDataDTO convertUserDataToUserDataDTO(UserData userData)
    {
        if(userData==null)
        {
            boolean temp=false;
        }
        Integer teamId;
        String teamName;
        if(userData.getTeamlist().size()!=0)//pause do not have a team
        {
            teamId=userData.getTeamlist().get(0).getTeamId();
            teamName=userData.getTeamlist().get(0).getTeamName();

        }

        else  {
            teamId=null;
            teamName=null;
        }

        UserDataDTO userDTO = new UserDataDTO(userData.getUserId(),userData.getUsername(),teamId,teamName, userData.getEmail(), userData.getReliability(), userData.getRole(),userData.getLeagueParticipation());


        userDTO.setRole(userData.getRole());
        return userDTO;
    }


}
