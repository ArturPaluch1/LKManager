package LKManager.controllers;

import LKManager.services.MZUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Controller
public class UserController {

    private final MZUserService MZUserService;
    public UserController(MZUserService MZUserService) {
        this.MZUserService = MZUserService;
    }

    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"user.html","user"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException {

        try {
            model.addAttribute("user", MZUserService. findByUsername("kingsajz"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return "user/index";
    }
}
