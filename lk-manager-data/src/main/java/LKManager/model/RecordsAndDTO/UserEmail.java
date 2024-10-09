package LKManager.model.RecordsAndDTO;

import LKManager.Security.EmailEncryption;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserEmail {
    private  String email;

    public UserEmail(String email) {
        this.email = email;
    }

    public String getEmail()  {
        try {
            return EmailEncryption.decrypt(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
