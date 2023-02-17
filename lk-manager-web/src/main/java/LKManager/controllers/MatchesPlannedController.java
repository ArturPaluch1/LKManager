package LKManager.controllers;

import LKManager.services.TeamTM;
import LKManager.model.MatchesMz.Match;
import LKManager.services.MatchService;
import LKManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MatchesPlannedController {
    private final MatchService matchService;
    private final UserService userService;

    public MatchesPlannedController(MatchService matchService, UserService userService ) {
        this.matchService = matchService;
        this.userService=userService;
    }

    // @RequestMapping({"","/","index.html"})
    @RequestMapping({"matchesplanned.html","matchesplanned"})
    public String index(Model model) throws IOException, SAXException, ParserConfigurationException {

        List<Match> mecze1=new ArrayList<>();
      //  for (var item: new TeamTM(userService).LoadLKUPSGV()  ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        boolean licznikNieOpponent=true;
        for (var item: new TeamTM(userService).wczytajUPSGZXML().getSkladUPSG()){
            if(licznikNieOpponent)
            {
            try {

                List<Match> mecze = matchService.findPlannedByUsername (item.getUsername()).getMatches();
                mecze.stream().filter(a -> LocalDate.parse(a.getDate(), formatter).isAfter(LocalDate.now())).collect(Collectors.toList());
                for (Match mecz:mecze
                ) {
                    if(mecz.getType().equals("friendly")&&LocalDate.parse(mecz.getDate(), formatter).getDayOfWeek().equals(DayOfWeek.TUESDAY))
                    {
                        mecze1.add(mecz);
                    }


                }

                licznikNieOpponent=false;
                continue;


            } catch (JAXBException e) {
                e.printStackTrace();
            }

            }

            licznikNieOpponent=true;

        }
        model.addAttribute("matches", mecze1);

        return "matchesPlanned/index";
    }
}
