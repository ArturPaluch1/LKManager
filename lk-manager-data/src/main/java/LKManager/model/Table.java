package LKManager.model;

import LKManager.LK.PlayerSummary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Repository
@Component
@XmlRootElement(name = "Tabela")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({PlayerSummary.class})
public class Table implements Serializable {

    private List<PlayerSummary> playerSummaries = new ArrayList<>();

    public Table() {




    }

    @XmlElement(name = "GraczPodsumowanie")
    public List<PlayerSummary> getPlayerSummaries() {
        return playerSummaries;
    }

    public void setPlayerSummaries(List<PlayerSummary> playerSummaries) {
        this.playerSummaries = playerSummaries;
    }
}
