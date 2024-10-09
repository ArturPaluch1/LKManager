package LKManager.model.MatchesMz;

import LKManager.model.Round;
import LKManager.model.UserMZ.MZUserData;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mecze", schema="lkm_dev")
@Getter
@Setter
@XmlRootElement(name = "Match")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({MatchTeam.class})

public class Match implements Serializable {

    @Transient
    private final List<MatchTeam> teamlist = new ArrayList();
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "runda")
    private Round round;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "mecz_id")
    private Long id;

      @Transient
/*    @Column(name = "data", updatable = false, insertable = false)*/

    private String date;

    //@Transient
    @Column(name = "data")
    // @Temporal(TemporalType.DATE)
    private LocalDate dateDB;

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
    private Byte userMatchResult1;
    @Column(name = "user_wynik2")
    private Byte userMatchResult2;
    @Column(name = "przeciwnik_wynik1")
    private Byte opponentMatchResult1;
    @Column(name = "przeciwnik_wynik2")
    private Byte opponentMatchResult2;

    //  @Column(name = "user")
  //  @Fetch(FetchMode.JOIN)
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "przeciwnik")//, referencedColumnName= "user_id")//, updatable = false, insertable = false)
    private MZUserData opponentMZUserData;

    // @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
   // @Fetch(FetchMode.JOIN)
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "gracz")//, referencedColumnName= "user_id")//, updatable = false, insertable = false)
    private MZUserData MZUserData;
    /* public XMLGregorianCalendar getDate() {
                return date;
            }*/


    public Match(Round round, Long id, String date, String status, String type, String typeName, int typeId, MZUserData MZUserData) {
        this.round = round;
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
        this.typeName = typeName;
        this.typeId = typeId;
        this.MZUserData = MZUserData;

    }

    public Match() {
    }



    @XmlAttribute
    public Byte getUserMatchResult1() {
        return userMatchResult1;
    }

    public void setUserMatchResult1(Byte userMatchResult1) {
        this.userMatchResult1 = userMatchResult1;
    }

    @XmlAttribute
    public Byte getOpponentMatchResult1() {
        return opponentMatchResult1;
    }

    public void setOpponentMatchResult1(Byte opponentMatchResult1) {
        this.opponentMatchResult1 = opponentMatchResult1;
    }

    @XmlAttribute
    public Byte getUserMatchResult2() {
        return userMatchResult2;
    }

    public void setUserMatchResult2(Byte userMatchResult2) {
        this.userMatchResult2 = userMatchResult2;
    }

    @XmlAttribute
    public Byte getOpponentMatchResult2() {
        return opponentMatchResult2;
    }

    public void setOpponentMatchResult2(Byte opponentMatchResult2) {
        this.opponentMatchResult2 = opponentMatchResult2;
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected LocalDate stringToLocalDate(String date) {

  int year= Integer.parseInt(date.trim().split("-")[0]);
        int month= Integer.parseInt(date.split("-")[1]);
        int day= Integer.parseInt(date.trim().split("-")[2].split(" ")[0]);
        LocalDate tempDate = LocalDate.of(year,month,day);
        return tempDate;


    }
    //////////////////////////////////////////////
    @XmlAttribute(name = "date")
    public String getDate() {

   /*     if(this.date==null)
            return this.dateDB;
        else*/
        return date;
    }

    public void setDate(String date) {
        this.date = date;

      //    this. setDateDB(stringToLocalDate(date));
    }

    //////////////////////////////////////////////
    public LocalDate getDateDB() {

        int i = 0;

        return dateDB;
        //stringToDate(this.date);
        //new Date(this.date.getYear(), this.date.getMonth(), this.date.getDay());
    }

    public void setDateDB(LocalDate date) {
      /*  if (date != null) {


            //    this.dateDB= stringToDate(date);

            int y = 0;
        }*/
        this.setDate(date.toString());

dateDB=date;
    }
    //////////////////////////////////////////////
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

    @XmlElement(name = "Team")
    public MatchTeam getTeam() {
        return team;
    }

    public void setTeam(MatchTeam team) {
        this.team = team;
        teamlist.add(team);
    }

    public List<MatchTeam> getTeamlist() {
        return teamlist;
    }


/*    public UserData getopponentUser() {
        return opponentUserData;
    }*/
    @XmlElement
    public MZUserData getOpponentMZUserData() {
        return opponentMZUserData;
    }

    public void setOpponentMZUserData(MZUserData opponentMZUserData) {
        this.opponentMZUserData = opponentMZUserData;
    }

/*
    public void setopponentUser(UserData opponentUser) {
        this.opponentUserData = opponentUser;
    }
*/

    @XmlElement
    public MZUserData getMZUserData() {
        return MZUserData;
    }

    public void setMZUserData(MZUserData MZUserData) {
        this.MZUserData = MZUserData;
    }
}
