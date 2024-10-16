package LKManager.Login;

import LKManager.DAO_SQL.UserDAO;
import LKManager.Security.EmailEncryption;
import LKManager.model.account.User;
import LKManager.services.RedisService.RedisUserService;
import LKManager.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@Data
public class emailConfirmationController {
private final UserService userService;
private final RedisUserService redisUserService;
    private final UserDAO userDAO;
private final String aesSecretKey;
private final String  aesInitVector;
    public emailConfirmationController(UserService userService, RedisUserService redisUserService, UserDAO userDAO, @Value("${encrypt.AES_SECRET_KEY}")  String aesSecretKey, @Value("${encrypt.AES_INIT_VECTOR}")  String aesInitVector) {
        this.userService = userService;
        this.redisUserService = redisUserService;
        this.userDAO = userDAO;
        this.aesSecretKey = aesSecretKey;
        this.aesInitVector = aesInitVector;
    }

    @Transactional
    @GetMapping(value = "/confirmEmail")
    public String confirmEmail(@RequestParam("token") String token,
                               @RequestParam(value = "email",required = false) String email, RedirectAttributes redirectAttributes, HttpSession session) throws Exception {




       Long userId= redisUserService.getActivationTokenUsername(token);
//UserDataDTO activatedUser=userService.activateUser(username);

        if (userId!=null&&email!=null) {

       /*     if(principal!=null) //user logged
            {*/
            User user=userDAO.getUserById(userId).get();
            session.setAttribute("user", user);
            UserDetails userDetails = user;
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            email= EmailEncryption.encrypt(email,aesSecretKey,aesInitVector);
            if( userService.setUsersEmail(userId,email))
               {
                   redirectAttributes.addFlashAttribute("message", "Email został ustawiony pomyślnie!");
                   return "redirect:/user/settings";
               }
               else
               {
                   redirectAttributes.addFlashAttribute("message",  "Błąd w ustawianiu Emaila");
                   return "redirect:/public/signUp";
               }
        //    }//user not logged in
      /*    else {
                if( userService.setUsersEmail(userId,email))
                {
                redirectAttributes.addFlashAttribute("message", "Email został ustawiony pomyślnie!");
                return "redirect:/public/signUp";
            }
                 else
                {
                    redirectAttributes.addFlashAttribute("message",  "Błąd w ustawianiu Emaila");
                    return "redirect:/public/signUp";
                }
            }*/
        } else {
            redirectAttributes.addFlashAttribute("message",  "Nieprawidłowy lub wygasły token.");

            return "redirect:/public/signUp";
        }
    }
}
