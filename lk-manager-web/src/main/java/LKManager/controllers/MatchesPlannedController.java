package LKManager.controllers;

import LKManager.Bootstrap.TeamTM;
import LKManager.model.MatchesMz.Match;
import LKManager.model.MatchesMz.Matches;
import LKManager.services.MatchService;
import LKManager.services.UserService;
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
    private UserService userService;

    public MatchesPlannedController(MatchService matchService, UserService userService ) {
        this.matchService = matchService;
        this.userService=userService;
    }

    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"matchesplanned.html","matchesplanned"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException {

        List<Match> mecze1=new ArrayList<>();
        for (var item: new TeamTM(userService).LoadLKUPSGV()
             ) {
            try {
                List<Match> mecze = matchService.findPlannedByUsername (item.getUsername()).getMatches();

                for (Match mecz:mecze
                ) {
                    if(mecz.getType().equals("friendly"))
                    {
                        mecze1.add(mecz);
                    }


                }



            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("matches", mecze1);

        return "matchesPlanned/index";
    }
}
