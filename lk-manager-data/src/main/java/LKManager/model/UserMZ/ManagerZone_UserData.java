package LKManager.model.UserMZ;



import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@AllArgsConstructor
@XmlRootElement(name = "ManagerZone_UserData")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({MZUserData.class, Team.class})
public class ManagerZone_UserData  implements Serializable  {


private MZUserData MZUserData;




    @XmlElement(name = "UserData")
    public MZUserData getMZUserData() {
        return MZUserData;
    }

    public void setMZUserData(MZUserData MZUserData) {
        this.MZUserData = MZUserData;
    }





    public ManagerZone_UserData() {
    }

    @Override
    public String toString() {
        return "ManagerZone_UserData{" +
                "userData=" + MZUserData + '}';
    }
}

