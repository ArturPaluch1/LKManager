package LKManager.controllers.LK;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class tempController {


    public tempController() {
    }

    @RequestMapping("/temp")
    public String index()
    {
        return "LK/temp";
    }
}
