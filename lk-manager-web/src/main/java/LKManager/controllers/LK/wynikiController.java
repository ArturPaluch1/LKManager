package LKManager.controllers.LK;

import LKManager.LK.Terminarz;
import LKManager.LKManagerApplication;
import LKManager.services.TeamTM;
import LKManager.services.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class wynikiController {
    private final UserService userService;
    private final MatchService matchService;
   private final TerminarzService terminarzService;
    private Integer numerRundy;
    private  Terminarz terminarz;
private  final WynikiService wynikiService;

    public wynikiController(UserService userService, MatchService matchService, URLs urLs, TerminarzService terminarzService, WynikiService wynikiService) {
        this.userService = userService;
        this.matchService = matchService;


        this.terminarzService = terminarzService;

        this.wynikiService = wynikiService;
    }

    public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }

    @GetMapping({"wyniki.html", "wyniki", "wyniki"} )
    public String index(Model model) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {



        terminarz=terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");

        if(terminarz!= null)
        {
        // terminarz= terminarzService.wczytajTerminarz("static.resources/terminarz.xml");
        }
        else
        {
            //todo
            //dodac error zrob najpierw terminarz
        }


        if(numerRundy== null)
        {
            numerRundy=1;
        }

        TeamTM teamy = new TeamTM(userService);

        //   teamy.zapiszUPSGDoXML();
        var grajki=    teamy.wczytajUPSGZXML();
        //  =  teamy.LoadCalyUPSG();

     // terminarz=  wynikiService.wyswietlWyniki(numerRundy);



        if(numerRundy== null)
        {
            numerRundy=1;
        }


        model.addAttribute("nrRundy", terminarz.getTerminarz());

        model.addAttribute("runda", terminarz.getTerminarz().get(numerRundy-1));
        model.addAttribute("mecze", terminarz.getTerminarz().get(numerRundy-1).getMecze());
        model.addAttribute("numerRundy", numerRundy);
        return "LK/wyniki";
    }

    @PostMapping("/pokazRunde1")
    public String pokazRunde(@RequestParam(value = "participant", required = true)Integer numerRundy)
    {
        int oo=9;
        this.numerRundy=numerRundy;
        return "redirect:/wyniki";
//return "redirect:/LKManager.LK/terminarz";
    }

    @PostMapping("/aktualizuj")
    public String aktualizujWyniki() throws ParserConfigurationException, JAXBException, SAXException, IOException, DatatypeConfigurationException, URISyntaxException {
        if(numerRundy== null)
        {
            numerRundy=1;
        }



        if(terminarz!= null)
        {
            // terminarz= terminarzService.wczytajTerminarz("terminarz.xml");
        }
        else
        {
            terminarz=terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
            if(terminarz== null)
            {
                //todo
                //dodac error zrob najpierw terminarz
            }

        }

        wynikiService.aktualizujWyniki(numerRundy,terminarz,matchService);
        return "redirect:/wyniki";


  /*//////////////////////////////////////////////////////////
      var data=  terminarz.getTerminarz().get(getNumerRundy()).getData();

 //       searchLoop:
 //       break searchLoop;

      //mecze danej rundy
      for (var item :terminarz.getTerminarz().get(getNumerRundy()).getMecze()
             )
      {
             var grajek1= item.getUser();
             var meczeGrajka1=  matchService.findPlayedByUsername(grajek1.getUsername());
var idDruzynyPrzeciwnika =item.getopponentUser().getTeamlist().get(0).getTeamId();
          List<Match> znalezione= new ArrayList<>();
          //mecze danego grajka
            for (var mecz:meczeGrajka1.getMatches()
                 ) {
                    if (mecz.getDate().contains(terminarz.getTerminarz().get(getNumerRundy()).getData().toString()))
                    {
                        if (mecz.getType().equals("friendly")) {
                            var oponent = item.getopponentUser().getTeamlist().get(0).getTeamId();

                            if ( idDruzynyPrzeciwnika
                                    ==    mecz.getTeamlist().get(0).getTeamId()
                                    ||  idDruzynyPrzeciwnika== mecz.getTeamlist().get(1).getTeamId())

                                    {
                                znalezione.add(mecz);

                            }

                        }
                    }



            }

            if(znalezione.size()!=0)
            {
int y=9;
             //  znaleziony.getTeamlist().get(0).

            }
            else
            {
                int j=0;
                //todo wyswetlic ze nie znaleziono meczu ->wpisac nr
            }

        }/////////////////////////////////////////////////////////////*/
    }

}
