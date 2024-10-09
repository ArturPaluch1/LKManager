package LKManager.Security;

import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EmailEncryption {


    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("AES_SECRET_KEY");
    private static final String INIT_VECTOR = dotenv.get("AES_INIT_VECTOR");
    public static String encrypt(String email) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(email.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Funkcja odszyfrowywania
    public static String decrypt(String encryptedEmail) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedEmail));
        return new String(original);
    }
}