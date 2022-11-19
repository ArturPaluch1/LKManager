package LKManager.LK;

import LKManager.model.MatchesMz.MatchTeam;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement (name="terminarz")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Runda.class, MatchTeam.class})
public class Terminarz implements Serializable {


   private List<Runda> terminarz = new ArrayList<>();

    public Terminarz(List<Runda> terminarz) {
        this.terminarz = terminarz;
    }

    public Terminarz() {
    }
  //  @XmlElementWrapper(name="terminarz")
    @XmlElement(name = "Runda")
    public List<Runda> getTerminarz() {
        return terminarz;
    }

    public void setTerminarz(List<Runda> terminarz) {
        this.terminarz = terminarz;
    }
}
