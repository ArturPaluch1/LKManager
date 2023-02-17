package LKManager.controllers.Sparingi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SparingController {

    public SparingController() {


    }
    @RequestMapping("Sparingi.html")
    public String index ()
    {


        return "Sparingi/index";
    }



}
