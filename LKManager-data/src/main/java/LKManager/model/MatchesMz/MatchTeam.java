package LKManager.model.MatchesMz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="Team")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MatchTeam implements Serializable {

    private String field;
    private  byte goals;
    private  int teamId;
    private String teamName;
    private String countryShortname;

    public MatchTeam(String field, byte goals, int teamId, String teamName, String countryShortname) {
        this.field = field;
        this.goals = goals;
        this.teamId = teamId;
        this.teamName = teamName;
        this.countryShortname = countryShortname;
    }

    public MatchTeam() {
    }
    @XmlAttribute
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
    @XmlAttribute
    public byte getGoals() {
        return goals;
    }

    public void setGoals(byte goals) {
        this.goals = goals;
    }
    @XmlAttribute
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    @XmlAttribute
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    @XmlAttribute
    public String getCountryShortname() {
        return countryShortname;
    }

    public void setCountryShortname(String countryShortname) {
        this.countryShortname = countryShortname;
    }
}
