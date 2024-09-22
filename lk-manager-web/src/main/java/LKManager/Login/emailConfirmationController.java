package LKManager.Login;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.services.RedisService.RedisUserService;
import LKManager.services.UserService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Data
public class emailConfirmationController {
private final UserService userService;
private final RedisUserService redisUserService;
    @GetMapping(value = "/confirmEmail")
    public String confirmEmail(@RequestParam("token") String token) {
       String username= redisUserService.getActivationTokenUsername(token);
UserDataDTO activatedUser=userService.activateUser(username);

        if (activatedUser!=null) {
            return "Konto zostało aktywowane pomyślnie!";
        } else {
            return "Nieprawidłowy lub wygasły token.";
        }
    }
}
