package LKManager.model.UserMZ;



import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "ManagerZone_UserData")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({UserData.class, Team.class})
public class ManagerZone_UserData  implements Serializable  {


private UserData UserData;




    @XmlElement(name = "UserData")
    public UserData getUserData() {
        return UserData;
    }

    public void setUserData(UserData userData) {
        this.UserData = userData;
    }



    public ManagerZone_UserData(UserData userData) {
        this.UserData = userData;

    }

    public ManagerZone_UserData() {
    }

    @Override
    public String toString() {
        return "ManagerZone_UserData{" +
                "userData=" + UserData + '}';
    }
}

