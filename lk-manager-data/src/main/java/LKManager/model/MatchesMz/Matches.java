package LKManager.model.MatchesMz;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "ManagerZone_MatchList")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Match.class, MatchTeam.class})
public class Matches implements Serializable {


    private final List<Match> matches=new ArrayList<>();
private Match match;
    public Matches() {
    }

    @XmlElement(name = "Match")
    public List<Match> getMatches() {
        return matches;
    }

    @XmlElement(name = "Match")
    public void setMatch(Match match) {
        this.matches.add(match);
    }
}
