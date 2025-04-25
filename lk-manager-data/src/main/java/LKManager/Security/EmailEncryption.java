package LKManager.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class EmailEncryption {
    private  final String SECRET_KEY ;
private final String INIT_VECTOR;



    public EmailEncryption(@Value("${encrypt.AES_SECRET_KEY}") String SECRET_KEY,
                           @Value("${encrypt.AES_INIT_VECTOR}") String INIT_VECTOR) {
        this.SECRET_KEY = SECRET_KEY;
        this.INIT_VECTOR = INIT_VECTOR;
    }

   // private  final String SECRET_KEY ;
  //  private  final String INIT_VECTOR ;
    public  String encrypt(String email) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(email.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Funkcja odszyfrowywania
    public  String decrypt(String encryptedEmail) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedEmail));
        return new String(original);
    }
}