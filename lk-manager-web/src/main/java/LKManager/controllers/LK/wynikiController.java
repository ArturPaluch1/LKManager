package LKManager.controllers.LK;

import LKManager.services.MatchService;
import LKManager.services.URLs;
import LKManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class wynikiController {
    private final UserService userService;
    private final MatchService matchService;
    private final URLs urLs;
    public wynikiController(UserService userService, MatchService matchService, URLs urLs) {
        this.userService = userService;
        this.matchService = matchService;

        this.urLs = urLs;
    }

    @GetMapping({"wyniki.html", "wyniki", "wyniki"} )
    public String index()  {




        return "LK/wyniki";
    }




}
