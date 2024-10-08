package LKManager.services;

import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.SignUpForm;

public interface AccountService {

    MZUserData createAccount(SignUpForm signUpForm);

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

     String generateActivationToken(String userName);
    /*    boolean sendPasswordToUser(UserData user);*/
}
