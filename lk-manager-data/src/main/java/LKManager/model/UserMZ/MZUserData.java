package LKManager.model.UserMZ;


import LKManager.model.account.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="mzusers")
@XmlRootElement(name = "UserData")
@XmlAccessorType(XmlAccessType.PROPERTY)
@AllArgsConstructor
@NoArgsConstructor
public class MZUserData implements Serializable{






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
        @Column(name = "MZuser_id",  nullable = false, updatable = false)
    private Long MZuser_id ;


    @OneToMany(mappedBy = "mzUser", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
        user.setMzUser(this);
    }
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


    // getters and setters





    @Column(name = "MZusername")
    private String username;





    @Transient
    private String countryShortname;
    @Transient
    private String userImage;





    public MZUserData getOpponentUser() {
        return opponentUser;
    }

    public void setOpponentUser(MZUserData opponentUser) {
        this.opponentUser = opponentUser;
    }

    @Transient
    private MZUserData opponentUser;

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



    public MZUserData(String username, Long MZId, String countryShortname, String userImage, List<Team> teamlist, String email, String password) {
        this.username = username;
        this.MZuser_id = MZId;
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

    public void setMZuser_id(Long setUser_Id) {
        this.MZuser_id = setUser_Id;
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

    @XmlAttribute(name = "userId")
    public Long getMZuser_id() {
        return MZuser_id;
    }



    @XmlAttribute
    public String getCountryShortname() {
        return countryShortname;
    }



    @XmlAttribute
    public String getUserImage() {
        return userImage;
    }




    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", userId=" + MZuser_id +
                ", countryShortname='" + countryShortname + '\'' +
                ", userImage='" + userImage + '\'' +
                ", teamlist=" + teamlist +
                '}';
    }



}
