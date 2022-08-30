package LKManager.controllers;

import LKManager.Bootstrap.TeamTM;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import LKManager.services.MatchService;
import LKManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
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
public class LKController {
    private final UserService userService;
    private final MatchService matchService;
  //  private List<UserData> skladTM = new ArrayList<>();

    public LKController(UserService userService, MatchService matchService) {
        this.userService = userService;
        this.matchService = matchService;
    }


    @RequestMapping({"LK.html", "LK", "lk", "lk.html", "html"} )
    public String index()  {





        return "LK/index";
    }
    @RequestMapping(value="/wyswietlRozegrane")
public String wyswietlRozegrane(Model model)throws IOException, SAXException, ParserConfigurationException, JAXBException
    {
        List<Match> tempMatches = new ArrayList<>();

        String opponentName = "";

        TeamTM rzeszow= new TeamTM(userService);



        for (UserData user : rzeszow.LoadLKUPSGV()
        ) {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<Match> matches = matchService.findPlayedByUsername(user.getUsername()).getMatches().stream().filter(a -> LocalDate.parse(a.getDate(), formatter).isAfter(LocalDate.now().minusDays(7))).collect(Collectors.toList());
            for (Match match : matches) {

                LocalDate ld = LocalDate.parse(match.getDate(), formatter);
                if (ld.getDayOfWeek().equals(DayOfWeek.FRIDAY) && match.getType().equals("friendly")) {

                    if (match.getTeamlist().get(0).getTeamName().equals(user.getTeamlist().get(0).getTeamName())) {
                        opponentName = userService.findById(match.getTeamlist().get(1).getTeamId()).getUsername();
                        match.setopponentUserName(opponentName);
                        match.setUserName(user.getUsername());
                        tempMatches.add(match);

                    } else {
                        opponentName = userService.findById(match.getTeamlist().get(0).getTeamId()).getUsername();
                        match.setopponentUserName(opponentName);
                        String opponentTeamName = match.getTeamlist().get(0).getTeamName();
                        match.setUserName(user.getUsername());
                        byte opponentScore = match.getTeamlist().get(0).getGoals();


                        match.getTeamlist().get(0).setTeamName(user.getTeamlist().get(0).getTeamName());
                        match.getTeamlist().get(0).setGoals(match.getTeamlist().get(1).getGoals());

                        match.getTeamlist().get(1).setTeamName(opponentTeamName);
                        match.getTeamlist().get(1).setGoals(opponentScore);

                        tempMatches.add(match);

                    }
                    //   System.out.println("====="+user+" : "+match.getDate()+ " - "+match.getTeamlist().get(0).getTeamName()+ " - "+ match.getTeamlist().get(1).getTeamName()+"\n");
                }
            }
        }

        model.addAttribute("Results", tempMatches);


return "LK/index";
    }
    @RequestMapping(value="/wyswietlNierozegrane")
  public String  wyswietlNierozegrane()
    {



        return "LK/index";
    }
}
