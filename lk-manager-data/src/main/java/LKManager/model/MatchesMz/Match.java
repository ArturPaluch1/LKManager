package LKManager.model.MatchesMz;

import LKManager.LK.Runda;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mecze")
@Getter
@Setter
@XmlRootElement(name = "Match")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({MatchTeam.class})
public class Match   implements Serializable {

@ManyToOne( cascade = CascadeType.ALL)
@JoinColumn(name = "runda")
    private Runda runda;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "mecz_id")
    private Long id;

    @XmlAttribute(name = "date")
    public String getDate() {
        return date;
    }

    @Transient
    private String date;


    @Transient
    private String status;
    @Transient
    private String type;
    @Transient
    private String typeName;
    @Transient
    private int typeId;
    @Transient
    private MatchTeam team;

    @Column(name = "user_wynik1")
private String userMatchResult1;
    @Column(name = "user_wynik2")
    private String userMatchResult2;
    @Column(name = "przeciwnik_wynik1")
    private String opponentMatchResult1;
    @Column(name = "przeciwnik_wynik2")
    private String opponentMatchResult2;

  //  @Column(name = "user")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "przeciwnik")//, updatable = false, insertable = false)
    private UserData opponentUser;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user")//, updatable = false, insertable = false)
    private UserData user;


    @Transient
    private final List<MatchTeam> teamlist= new ArrayList();

    public Match(Runda runda, Long id, String date, String status, String type, String typeName, int typeId, UserData user) {
        this.runda = runda;
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
        this.typeName = typeName;
        this.typeId = typeId;
        this.user = user;

    }
    @XmlAttribute
    public String getUserMatchResult1() {
        return userMatchResult1;
    }

    public void setUserMatchResult1(String userMatchResult1) {
        this.userMatchResult1 = userMatchResult1;
    }
    @XmlAttribute
    public String getOpponentMatchResult1() {
        return opponentMatchResult1;
    }

    public void setOpponentMatchResult1(String opponentMatchResult1) {
        this.opponentMatchResult1 = opponentMatchResult1;
    }

    @XmlAttribute
    public String getUserMatchResult2() {
        return userMatchResult2;
    }

    public void setUserMatchResult2(String userMatchResult2) {
        this.userMatchResult2 = userMatchResult2;
    }

    @XmlAttribute
    public String getOpponentMatchResult2() {
        return opponentMatchResult2;
    }

    public void setOpponentMatchResult2(String opponentMatchResult2) {
        this.opponentMatchResult2 = opponentMatchResult2;
    }

    public Match() {
    }
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setDateDB(String date) {
      if(date!=null)
      {





          this.dateDB= stringToDate(date);

          int y=0;
      }

    }

protected Date stringToDate(String date)
    {
        Date tempDate= new Date();
        tempDate.setYear(Integer.parseInt(date.trim().split("-")[0]));
        tempDate.setMonth(Integer.parseInt(date.split("-")[1]));
        tempDate.setDate(Integer.parseInt(date.trim().split("-")[2].split(" ")[0]));
        return tempDate;
    }

    /* public XMLGregorianCalendar getDate() {
                return date;
            }*/
   //@Transient
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
   private Date dateDB;



    public Date getDateDB() {

        int i=0;

        return  stringToDate(this.date);
        //new Date(this.date.getYear(), this.date.getMonth(), this.date.getDay());
    }


    public void setDate(String date) {
        this.date = date;
       this. setDateDB(date);
    }
    @XmlAttribute
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @XmlAttribute
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    @XmlAttribute
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }



    public void setTeam(MatchTeam team) {
        this.team = team;
        teamlist.add(team);
    }
    @XmlElement(name = "Team")
    public MatchTeam getTeam() {
        return team;
    }

    public List<MatchTeam> getTeamlist() {
        return teamlist;
    }

    @XmlElement
    public UserData getopponentUser() {
        return opponentUser;
    }

    public void setopponentUser(UserData opponentUser) {
        this.opponentUser = opponentUser;
    }

    @XmlElement
    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
