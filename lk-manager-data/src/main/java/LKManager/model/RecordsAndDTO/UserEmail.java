package LKManager.model.RecordsAndDTO;

import LKManager.Security.EmailEncryption;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserEmail {
    private  String email;
    private final EmailEncryption emailEncryption;


    public String getEmail()  {
        try {
            return emailEncryption.decrypt(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
