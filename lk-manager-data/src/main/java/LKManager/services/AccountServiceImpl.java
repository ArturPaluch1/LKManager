package LKManager.services;

import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.SignUpForm;
import LKManager.services.RedisService.RedisUserService;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
public class AccountServiceImpl implements AccountService{
   private final UserService userService ;
   private final PasswordEncoder passwordEncoder;
private final RedisUserService redisUserService;
    @Override
    public MZUserData createAccount(SignUpForm signUpForm) {
        MZUserData user = userService.getMZUserDataByUsername(signUpForm.getUsername());
        if (user == null) {

            userService.addUser(signUpForm);

            return null;

        } else {
            return null;
        }
    }


    public String generateActivationToken(String user) {
        String token = UUID.randomUUID().toString();
        redisUserService.setActivationToken(token,user);

        return token;
    }
 /*   private String generatePassword()
    {
        final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:',.<>?/";
        final String ALL_CHARACTERS = UPPER + LOWER + DIGITS + SPECIAL;

        int passwordLength = (new Random().nextInt(8)+4);

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(passwordLength);
        List<Integer> charType = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < passwordLength; i++) {
            if(i<8)
                charType.add( rand.nextInt(4));
            else
            {
                if(!charType.contains(0)) charType.add(0);
                else if(!charType.contains(1)) charType.add(1);
                else if(!charType.contains(2)) charType.add(2);
                else  if(!charType.contains(3)) charType.add(3);
                else
                    charType.add( rand.nextInt(4));

            }
        }

        for (int i = 0; i < charType.size(); i++) {
            switch (i)
            {
                case 0:
                {
                    int index = random.nextInt(UPPER.length());
                    password.append(UPPER.charAt(index));
                    break;
                }
                case 1:
                {
                    int index = random.nextInt(LOWER.length());
                    password.append(LOWER.charAt(index));
                    break;
                }
                case 2:
                {
                    int index = random.nextInt(DIGITS.length());
                    password.append(DIGITS.charAt(index));
                    break;
                }
                case 3:
                {
                    int index = random.nextInt(SPECIAL.length());
                    password.append(SPECIAL.charAt(index));
                    break;
                }
            }
        }

        String hashedPassword  = passwordEncoder.encode(password);
        return hashedPassword;
    }*/



}
