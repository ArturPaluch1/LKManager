package LKManager.Security;

import lombok.AllArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@AllArgsConstructor
public class EmailEncryption {
    private  final String SECRET_KEY ;
private final String INIT_VECTOR;





   // private  final String SECRET_KEY ;
  //  private  final String INIT_VECTOR ;
    public static String encrypt(String email, String SECRET_KEY, String INIT_VECTOR) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(email.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Funkcja odszyfrowywania
    public static String decrypt(String encryptedEmail, String SECRET_KEY, String INIT_VECTOR) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedEmail));
        return new String(original);
    }
}