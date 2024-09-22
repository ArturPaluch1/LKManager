package LKManager.model.UserMZ;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users", schema="lkm_dev")
@Getter @Setter
//@Where(clause = "DELETED = 0")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedUserFilter", condition = "deleted = :isDeleted")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE user_id=?")
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
    private Long userId=null;
 /*   @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "user_id", unique = true)
    private Integer userId;
*/
/*    @OneToOne(mappedBy = "opponentUser" )
    private Match meczOpponent;

    @OneToOne(mappedBy = "user")
private Match meczUser;*/
   /* public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
*/
// @Column(name = "DELETED",columnDefinition = "TINYINT")
    //prod \/

    @Enumerated(EnumType.ORDINAL)
     @Column(name = "role",columnDefinition = "TINYINT")
  /* @Column(name = "active",columnDefinition = "BIT")*/
   private Role role;//= false;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    // getters and setters
public Role getRole()
{
    return  this.role;
}
    public void setRole(Role role) {
        this.role = role;
    }




    @Column(name = "username")
    private String username;

@Column(name = "reliability")
private long reliability;



    @Transient
    private String countryShortname;
    @Transient
    private String userImage;

    public LeagueParticipation getLeagueParticipation() {
        return leagueParticipation;
    }

    public void setLeagueParticipation(LeagueParticipation leagueParticipation) {
        this.leagueParticipation = leagueParticipation;
    }

    @Column(name="league_participation", columnDefinition = "TINYINT")
    @Enumerated(EnumType.ORDINAL)
private LeagueParticipation leagueParticipation;

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
         //   orphanRemoval = true,

            fetch = FetchType.EAGER
    )
   // @Fetch(FetchMode.SUBSELECT)
  //@Where(clause = "DELETED = 0")
  //  @FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
  //  @Filter(name = "deletedUserFilter", condition = "deleted = :isDeleted")
  //  @SQLDelete(sql = "UPDATE teams SET deleted = true WHERE team_id=?")
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



    public UserData(String username, Long userId, String countryShortname, String userImage, List<Team> teamlist, String email,String password) {
        this.username = username;
        this.userId = userId;
        this.countryShortname = countryShortname;
        this.userImage = userImage;
        this.teamlist = teamlist;
this.email=email;
this.password=password;

    }



    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(Long setUserId) {
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
    public Long getUserId() {
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


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getReliability() {
        return reliability;
    }

    public void setReliability(long reliability) {
        this.reliability = reliability;
    }
}
