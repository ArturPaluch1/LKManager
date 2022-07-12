package LKManager.model.MatchesMz;

import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Match")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({MatchTeam.class})
public class Match   implements Serializable {

    private int id;
    private String date;
    private String status;
    private String type;
    private String typeName;
    private int typeId;
    private MatchTeam team;

    private List<MatchTeam> teamlist= new ArrayList();

    public Match(int id, String date, String status, String type, String typeName, int typeId) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
        this.typeName = typeName;
        this.typeId = typeId;
    }

    public Match() {
    }
    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @XmlAttribute
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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


@XmlElement(name = "Team")
    public void setTeam(MatchTeam team) {
        this.team = team;
        teamlist.add(team);
    }

    public List<MatchTeam> getTeamlist() {
        return teamlist;
    }
}
