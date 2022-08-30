package LKManager.controllers;

import LKManager.services.MatchService;
import LKManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Controller
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"user.html","user"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException {

        try {
            model.addAttribute("user", userService. findByUsername("kingsajz"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return "user/index";
    }
}
