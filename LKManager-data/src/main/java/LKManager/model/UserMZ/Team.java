package LKManager.model.UserMZ;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Setter
@XmlRootElement(name = "Team")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Team implements Serializable {

    private String sport;
    private String teamName;
    private String nameShort;
    private Integer teamId;
    private String seriesName;
    private Integer seriesId;
    private XMLGregorianCalendar startDate;
    private String sponsor;
    private Integer rankPos;
    private Integer rankPoints;

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
