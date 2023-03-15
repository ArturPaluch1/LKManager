package LKManager.LK;

import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



    @XmlRootElement(name="Gracze")
    @XmlAccessorType(XmlAccessType.PROPERTY)
    @XmlSeeAlso({UserData.class, Team.class})
    public class Gracze   implements Serializable {
        List<UserData> gracze = new ArrayList<>();

        public Gracze() {
        }

        public Gracze(List<UserData> gracze) {
            this.gracze = gracze;
        }
        @XmlElement(name = "UserData")
        public List<UserData> getGracze() {
            return gracze;
        }

        public void setGracze(List<UserData> gracze) {
            this.gracze = gracze;
        }
    }