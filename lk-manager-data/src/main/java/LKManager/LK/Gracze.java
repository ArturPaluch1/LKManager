package LKManager.LK;

import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.MZUserData;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



    @XmlRootElement(name="Gracze")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    @XmlSeeAlso({MZUserData.class, Team.class})
    public class Gracze   implements Serializable {
        List<MZUserData> gracze = new ArrayList<>();

        public Gracze() {
        }

        public Gracze(List<MZUserData> gracze) {
            this.gracze = gracze;
        }
        @XmlElement(name = "UserData")
        public List<MZUserData> getGracze() {
            return gracze;
        }

        public void setGracze(List<MZUserData> gracze) {
            this.gracze = gracze;
        }
    }