package LKManager.model.UserMZ;



import LKManager.model.MatchesMz.Match;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@XmlRootElement(name = "UserData")
@XmlAccessorType(XmlAccessType.PROPERTY)
@AllArgsConstructor
public class UserData  implements Serializable{






/*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "u_id")
    private long id;

*/


/*@GeneratedValue
        (strategy = GenerationType.AUTO, generator = "native")
@GenericGenerator(name = "native", strategy = "native")*/
//(strategy = GenerationType.IDENTITY, generator = "native")
//@GenericGenerator(name = "native", strategy = "native")
//@Column(name = "user_id", unique = true)
  /*  @GeneratedValue(strategy = GenerationType.AUTO)
@GenericGenerator(name = "user_id", strategy = "user_id")*/
    //(strategy = GenerationType.IDENTITY, generator = "native")
    @Id
    @Column(name = "user_id",  nullable = false, insertable = true, updatable = false)
    private Integer userId=null;
 /*   @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "user_id", unique = true)
    private Integer userId;
*/
    @OneToOne(mappedBy = "opponentUser")
    private Match meczOpponent;

    @OneToOne(mappedBy = "user")
private Match meczUser;
   /* public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
*/
    @Column(name = "username")
    private String username;

    @Transient
    private String countryShortname;
    @Transient
    private String userImage;


    public UserData getOpponentUser() {
        return opponentUser;
    }

    public void setOpponentUser(UserData opponentUser) {
        this.opponentUser = opponentUser;
    }

    @Transient
    private UserData opponentUser;

   /* @OneToMany(
            mappedBy = "teamId",
            cascade = CascadeType.ALL,
            orphanRemoval = true)*/
   // @JoinColumn(name = "team_id")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
         //   , orphanRemoval = true)
private List<Team> teamlist= new ArrayList();


    @XmlElement(name = "Team")
    public List<Team> getTeamlist() {
        return teamlist;
    }

/*   @XmlElement(name = "Team")
    public List<LKManager.model.UserMZ.Team> getTeamlist() {
        return teamlist;
    }

    public void setTeamlist(Team team) {

        this.teamlist.add(team);
    }*/

    public UserData(String username, Integer userId, String countryShortname, String userImage, List<Team> teamlist) {
        this.username = username;
        this.userId = userId;
        this.countryShortname = countryShortname;
        this.userImage = userImage;
        this.teamlist = teamlist;


    }



    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(Integer setUserId) {
        this.userId = setUserId;
    }

    public void setCountryShortname(String countryShortname) {
        this.countryShortname = countryShortname;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setTeamlist(List<Team> teamlist) {
        this.teamlist = teamlist;
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
                ", userId=" + userId +
                ", countryShortname='" + countryShortname + '\'' +
                ", userImage='" + userImage + '\'' +
                ", teamlist=" + teamlist +
                '}';
    }
}
