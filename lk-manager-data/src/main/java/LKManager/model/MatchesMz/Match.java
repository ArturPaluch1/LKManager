package LKManager.model.MatchesMz;

import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;
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
private String userMatchResult1="";
    private String userMatchResult2="";
    private String opponentMatchResult1="";
    private String opponentMatchResult2="";

private UserData opponentUser;
    private UserData user;

    private List<MatchTeam> teamlist= new ArrayList();

    public Match(int id, String date, String status, String type, String typeName, int typeId, UserData user) {
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
