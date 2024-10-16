package LKManager.model.RecordsAndDTO;

import LKManager.Security.EmailEncryption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;

@Data
@EqualsAndHashCode
public class UserEmail {
    private  String email;
    private final String aesSecretKey;
    private final String  aesInitVector;
    public UserEmail(String email, @Value("${encrypt.AES_SECRET_KEY}")  String aesSecretKey, @Value("${encrypt.AES_INIT_VECTOR}")  String aesInitVector) {
        this.email = email;
        this.aesSecretKey = aesSecretKey;
        this.aesInitVector = aesInitVector;
    }

    public String getEmail()  {
        try {
            return EmailEncryption.decrypt(email,aesSecretKey,aesInitVector);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
