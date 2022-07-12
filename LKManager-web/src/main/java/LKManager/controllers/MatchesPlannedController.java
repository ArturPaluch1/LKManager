package LKManager.controllers;

import LKManager.model.MatchesMz.Match;
import LKManager.model.MatchesMz.Matches;
import LKManager.services.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MatchesPlannedController {
    private MatchService matchService;
    public MatchesPlannedController(MatchService matchService) {
        this.matchService = matchService;
    }

    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"matchesplanned.html","matchesplanned"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException {

        try {
            List<Match> mecze = matchService.findPlannedByUsername ("kingsajz").getMatches();
            List<Match> mecze1=new ArrayList<>();
            for (Match mecz:mecze
                 ) {
                if(mecz.getType().equals("friendly"))
                {
                    mecze1.add(mecz);
                }


            }

            model.addAttribute("matches", mecze1);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return "matchesPlanned/index";
    }
}
