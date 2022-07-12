package LKManager.model.UserMZ;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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

    public void setSport(String sport) {
        this.sport = sport;
    }

    @XmlAttribute
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @XmlAttribute
    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
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

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    @XmlAttribute
    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    @XmlAttribute
    public XMLGregorianCalendar  getStartDate() {
        return startDate;
    }

    public void setStartDate(XMLGregorianCalendar  startDate) {
        this.startDate = startDate;
    }

    @XmlAttribute
    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    @XmlAttribute
    public Integer getRankPos() {
        return rankPos;
    }

    public void setRankPos(Integer rankPos) {
        this.rankPos = rankPos;
    }

    @XmlAttribute
    public Integer getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(Integer rankPoints) {
        this.rankPoints = rankPoints;
    }

    public Team(String sport, String teamName, String nameShort, Integer teamId, String seriesName, Integer seriesId, XMLGregorianCalendar  startDate, String sponsor, Integer rankPos, Integer rankPoints) {
        this.sport = sport;
        this.teamName = teamName;
        this.nameShort = nameShort;
        this.teamId = teamId;
        this.seriesName = seriesName;
        this.seriesId = seriesId;
        this.startDate = startDate;
        this.sponsor = sponsor;
        this.rankPos = rankPos;
        this.rankPoints = rankPoints;
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
