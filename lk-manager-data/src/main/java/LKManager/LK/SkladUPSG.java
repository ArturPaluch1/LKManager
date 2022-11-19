package LKManager.LK;


import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="SkladUPSG")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({UserData.class, Team.class})
public class SkladUPSG   implements Serializable {
    List<UserData> skladUPSG= new ArrayList<>();

    public SkladUPSG() {
    }

    public SkladUPSG(List<UserData> skladUPSG) {
        this.skladUPSG = skladUPSG;
    }
    @XmlElement(name = "UserData")
    public List<UserData> getSkladUPSG() {
        return skladUPSG;
    }

    public void setSkladUPSG(List<UserData> skladUPSG) {
        this.skladUPSG = skladUPSG;
    }
}
