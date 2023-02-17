package LKManager.controllers.LK;

import LKManager.LK.Terminarz;
import LKManager.LKManagerApplication;
import LKManager.services.TeamTM;
import LKManager.model.UserMZ.UserData;
import LKManager.services.MatchService;
import LKManager.services.TerminarzService;
import LKManager.services.UserService;

import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.net.URISyntaxException;
import java.util.*;

@Controller
//@RequestMapping({"terminarz.html", "/terminarz", "terminarz"})
public class terminarzController {
    private final UserService userService;
    private final MatchService matchService;
    //  private List<UserData> skladTM = new ArrayList<>();

    private Integer numerRundy;

    public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }
private final TerminarzService terminarzService;

    public terminarzController(UserService userService, MatchService matchService, TerminarzService terminarzService) {
        this.userService = userService;
        this.matchService = matchService;

        this.terminarzService = terminarzService;
    }


    @GetMapping("/terminarz")
    public String index(Model model) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, ParseException, URISyntaxException {



      //  Terminarz terminarz=terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
        Terminarz terminarz=terminarzService.wczytajTerminarz("Data/terminarz.xml");



if(terminarz!= null)
{
   // terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
    terminarzService.wczytajTerminarz("Data/terminarz.xml");
}
else
{
    ///////// data poczatku rozgrywek  /////////////////////////
    XMLGregorianCalendar data = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    data.setYear(2023);
    data.setMonth(2);
    data.setDay(21);

    ////////////     Ladowanie grajkow////////////////////////
    List<UserData> grajki;// = new ArrayList<>();
  //  grajki = new TeamTM(userService).LoadCalyUPSG();

   //grajki=new TeamTM(userService).LoadCalyUPSG();

    new TeamTM(userService).zapiszUPSGDoXML();
    grajki=  new TeamTM(userService).wczytajUPSGZXML().getSkladUPSG();
    for (int i = 0; i < grajki.size() - 1; i++) {
        System.out.println(grajki.get(i).getUsername());
    }

    terminarzService.utworzTerminarz(data, grajki);

    //terminarz= terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
    terminarz= terminarzService.wczytajTerminarz("Data/terminarz.xml");




}



    /*    for (int i = 0; i <grajkiA.size()-1; i++) {
            var para=grajkiA.get(i).getUsername()+" - "+grajkiB.get(i).getUsername();
            System.out.println(para);

        }*/

  /*      var grajek1= grajkiA.get(0);
        grajkiPrzesunieteA.add(grajek1);
        grajkiPrzesunieteA.add(grajkiB.get(0));
        grajkiB.remove(0);
        grajkiPrzesunieteA.addAll(grajkiA);

       grajkiPrzesunieteA.remove(grajkiPrzesunieteA.size()-1);

        grajkiPrzesunieteB = grajkiB;
        grajkiPrzesunieteB.remove(0);
        grajkiPrzesunieteB.add(grajkiA.get(grajkiA.size()-1));
*/


//       calyTerminarz.get(0).getMecze().get(0).
        model.addAttribute("nrRundy", terminarz.getTerminarz());
if(numerRundy== null)
{
    numerRundy=1;
}




        model.addAttribute("runda", terminarz.getTerminarz().get(numerRundy-1));
        model.addAttribute("mecze", terminarz.getTerminarz().get(numerRundy-1).getMecze()
        );




model.addAttribute("numerRundy", numerRundy);


        return "LK/terminarz/terminarz";
    }



@RequestMapping("/dodajTerminarz")
public String dodajTerminarz(Model model)
    {
       // model.addAttribute(userService.findAll())
        return "LK/terminarz/dodajTerminarz";
    }


    @PostMapping("/pokazRunde")
    public String pokazRunde(@RequestParam(value = "participant", required = true)Integer numerRundy)
    {
int oo=9;
     this.numerRundy=numerRundy;
        return "redirect:/terminarz";
//return "redirect:/LKManager.LK/terminarz";
    }


}
