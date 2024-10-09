package LKManager.model.RecordsAndDTO;

import LKManager.model.account.User;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.account.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @AllArgsConstructor @EqualsAndHashCode
public class UserDataDTO {
    private Long userId;
    private Long MZuserId;
    private String username;
    private String MZUsername;
    private Integer teamId;
    private String teamName;
//private  String email;
    private long reliability;
    private Role role;
    private LeagueParticipation leagueParticipation;




    public  UserDataDTO(User user)
    {
        this.userId = user.getId();
        this.username = user.getUsername();
       // this.email = user.getEmail();
        this.reliability = user.getReliability();
        this.role = user.getRole();
        this.leagueParticipation = user.getLeagueParticipation();

        if(user.getMzUser()!=null)
        {
            this.MZuserId = user.getMzUser().getMZuser_id();
            this.MZUsername = user.getMzUser().getUsername();
            this.teamId = user.getMzUser().getTeamlist().get(0).getTeamId();
            this.teamName = user.getMzUser().getTeamlist().get(0).getTeamName();
        }




    }


    public UserDataDTO mapUserToUserDataDTO(User user)
    {
        return new UserDataDTO(user.getId(),user.getMzUser().getMZuser_id(),user.getUsername(),user.getMzUser().getUsername(),user.getMzUser().getTeamlist().get(0).getTeamId(),user.getMzUser().getTeamlist().get(0).getTeamName(),user.getReliability(),user.getRole(),user.getLeagueParticipation());
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
