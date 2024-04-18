package LKManager.model.RecordsAndDTO;

import LKManager.model.UserMZ.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UserDataDTO {
    private Integer userId;
    private String username;
    private Integer teamId;
    private String teamName;

    private boolean deleted;
    public UserDataDTO mapUserDataToUserDataDTO(UserData user)
    {
        return new UserDataDTO(user.getUserId(),user.getUsername(),user.getTeamlist().get(0).getTeamId(),user.getTeamlist().get(0).getTeamName(),user.getDeleted());
    }


    /* public UserDataDTO(UserData userData) {
        this.userId=userData.getUserId();
        this.username=userData.getUsername();
        if(userData.getTeamlist().size()!=0)//pause do not have a team
        {
            this.teamId=userData.getTeamlist().get(0).getTeamId();
            this.teamName=userData.getTeamlist().get(0).getTeamName();
        }

        else  {
            this.teamId=null;
            this.teamName=null;
        }
        this.deleted=userData.getDeleted();
    }*/

  /*  public UserDataDTO UserDataToDTO(UserData userData)
    {
        this.userId=userData.getUserId();
        this.username=userData.getUsername();
        if(userData.getTeamlist().size()!=0)//pause do not have a team
        {
            this.teamId=userData.getTeamlist().get(0).getTeamId();
            this.teamName=userData.getTeamlist().get(0).getTeamName();
        }

        else  {
            this.teamId=null;
            this.teamName=null;
        }
        this.deleted=userData.getDeleted();

        return this;
    }*/
}
