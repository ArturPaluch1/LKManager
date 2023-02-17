package LKManager.controllers.LK;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class graczeController {

    public graczeController() {
    }

    @RequestMapping(value="/gracze")
public String wyswietlGraczy()
{
    return "LK/gracze";
}










}
