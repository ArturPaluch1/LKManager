package LKManager.controllers;

import LKManager.services.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Controller
public class MatchesPlayedController {
    private MatchService matchService;
    public MatchesPlayedController(MatchService matchService) {
        this.matchService = matchService;
    }

    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"matchesplayed.html","matchesplayed"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException {

        try {
            model.addAttribute("matches", matchService. findPlayedByUsername("kingsajz").getMatches());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return "matchesPlayed/index";
    }
}
