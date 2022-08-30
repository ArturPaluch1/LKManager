package LKManager.model.UserMZ;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@XmlRootElement(name = "UserData")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserData  implements Serializable{

    private String username;
    private Integer userId;
    private String countryShortname;
    private String userImage;
    private Team Team;

private List<Team> teamlist= new ArrayList();

    @XmlElement(name = "Team")
    public List<LKManager.model.UserMZ.Team> getTeamlist() {
        return teamlist;
    }

    public void setTeamlist(Team team) {

        this.teamlist.add(team);
    }




   /* public Team getTeam() {
        return Team;
    }*/
    @XmlElement(name = "Team")
    public void setTeam(Team team) {
        this.Team = team;
        this.teamlist.add(team);
    }

    @XmlAttribute
    public String getUsername() {
        return username;
    }



    @XmlAttribute
    public Integer getUserId() {
        return userId;
    }



    @XmlAttribute
    public String getCountryShortname() {
        return countryShortname;
    }



    @XmlAttribute
    public String getUserImage() {
        return userImage;
    }



    public UserData() {
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", id=" + userId +
                ", country='" + countryShortname + '\'' +
                ", userImage='" + userImage + '\'' +
                ", team1='" + teamlist.get(0) + '\'' +
                ", team2='" + teamlist.get(1) + '\'' +
                '}';
    }
}
