package LKManager.model.UserMZ;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;


@Entity
@Table(name = "teams", schema="dbo")
/*@FilterDef(name = "deletedTeamFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedTeamFilter", condition = "deleted = :isDeleted")*/
//@Where(clause = "DELETED = 0")


/*@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")*/
@SQLDelete(sql = "UPDATE teams SET deleted = true WHERE team_id=?")
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "Team")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Team implements Serializable {



    @Transient
    private String sport;

    @Column(name = "team_name")
    private String teamName;
    @Transient
    private String nameShort;


/*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;*/

   // @JoinColumn(name="u_id")



   @ManyToOne//( cascade = CascadeType.ALL)//(fetch = FetchType.LAZY)
    @JoinColumn(name="MZuser_id")
    private MZUserData user;

    public MZUserData getUser() {
        return user;
    }

    public void setUser(MZUserData user) {
        this.user = user;
    }

    @Id
    @Column(name = "MZteam_id")
    private Integer teamId;
    @Transient
    private String seriesName;
    @Transient
    private Integer seriesId;
    @Transient
    private XMLGregorianCalendar startDate;
    @Transient
    private String sponsor;
    @Transient
    private Integer rankPos;
    @Transient
    private Integer rankPoints;

  //@Column(name = "DELETED",columnDefinition = "TINYINT")
    //prod \/
  /* @Column(name = "DELETED",columnDefinition = "BIT")
    private boolean deleted = false;*/

    // getters and setters
  /*  public boolean getDeleted()
    {
        return  this.deleted;
    }
    public void setDeleted() {
        this.deleted = true;
    }
*/
    @XmlAttribute
    public String getSport() {
        return sport;
    }

    @XmlAttribute
    public String getTeamName() {
        return teamName;
    }

    @XmlAttribute
    public String getNameShort() {
        return nameShort;
    }

    @XmlAttribute
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @XmlAttribute
    public String getSeriesName() {
        return seriesName;
    }

    @XmlAttribute
    public Integer getSeriesId() {
        return seriesId;
    }

    @XmlAttribute
    public XMLGregorianCalendar  getStartDate() {
        return startDate;
    }

    @XmlAttribute
    public String getSponsor() {
        return sponsor;
    }

    @XmlAttribute
    public Integer getRankPos() {
        return rankPos;
    }

    @XmlAttribute
    public Integer getRankPoints() {
        return rankPoints;
    }



    @Override
    public String toString() {
        return "Team{" +
                "sport='" + sport + '\'' +
                ", teamName='" + teamName + '\'' +
                ", nameShort='" + nameShort + '\'' +
                ", teamId=" + teamId +
                ", league='" + seriesName + '\'' +
                ", leaagueId=" + seriesId +
                ", startDate=" + startDate +
                ", sponsor='" + sponsor + '\'' +
                ", rankPos=" + rankPos +
                ", rankPoints=" + rankPoints +
                '}';
    }

    public Team() {
    }
}
