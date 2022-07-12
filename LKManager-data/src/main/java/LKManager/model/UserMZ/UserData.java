package LKManager.model.UserMZ;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public UserData(String username, Integer id, String countryShortname, String userImage, Team team) {
        this.username = username;
        this.userId = id;
        this.countryShortname = countryShortname;
        this.userImage = userImage;
        this.Team = team;
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

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlAttribute
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @XmlAttribute
    public String getCountryShortname() {
        return countryShortname;
    }

    public void setCountryShortname(String countryShortname) {
        this.countryShortname = countryShortname;
    }

    @XmlAttribute
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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
