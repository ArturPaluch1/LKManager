package LKManager.controllers.LK;

import LK.Runda;
import LK.Terminarz;
import LKManager.Bootstrap.TeamTM;
import LKManager.controllers.Options;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import LKManager.services.MatchService;
import LKManager.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import javax.xml.datatype.Duration;
import java.util.*;

@Controller
//@RequestMapping({"terminarz.html", "/terminarz", "terminarz"})
public class terminarzController {
    private final UserService userService;
    private final MatchService matchService;
    //  private List<UserData> skladTM = new ArrayList<>();

    private Integer numerRundy=0;

    public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }

    public terminarzController(UserService userService, MatchService matchService) {
        this.userService = userService;
        this.matchService = matchService;

    }


    @GetMapping("/terminarz")
    public String index(Model model) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, ParseException {



        Terminarz terminarz =     jaxbXMLToObject("terminarz.xml");

if(terminarz!= null)
{

}
else
{
    ////////////     Ladowanie grajkow////////////////////////
    List<UserData> grajki;// = new ArrayList<>();
    grajki = new TeamTM(userService).LoadCalyUPSG();
    for (int i = 0; i < grajki.size() - 1; i++) {
        System.out.println(grajki.get(i).getUsername());
    }
    ////////////////////////////////////////////////////////
    dodajPauzeDlaParzystosci(grajki);
/////////////////podzial grajkow na pol  /////////////////
    var grajkiA = grajki.subList(0, (grajki.size()) / 2);
    var grajkiB = grajki.subList(grajki.size() / 2, grajki.size());

///////// data poczatku rozgrywek  /////////////////////////
    XMLGregorianCalendar data = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    data.setYear(2022);
    data.setMonth(10);
    data.setDay(25);
    Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);

    ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);

    List<Runda> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////
    for (int j = 1; j < grajki.size(); j++) {
        /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
        Runda runda;
        if (j != 1) {
            XMLGregorianCalendar tempData = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            tempData.setYear(calyTerminarz.get(calyTerminarz.size() - 1).getData().getYear());
            tempData.setMonth(calyTerminarz.get(calyTerminarz.size() - 1).getData().getMonth());
            tempData.setDay(calyTerminarz.get(calyTerminarz.size() - 1).getData().getDay());
            tempData.add(d);
            runda = new Runda(j, tempData);
        } else {
            runda = new Runda(j, data);
        }

        //////////ustalanie par /////////////////////////////
        for (int i = 0; i < grajkiA.size(); i++) {

            var para = listyGrajkow.getGrajkiA().get(i).getUsername() + " - " + listyGrajkow.getGrajkiB().get(i).getUsername();
            System.out.println(para);

            var tempMatch = new Match();
            tempMatch.setUserName(listyGrajkow.getGrajkiA().get(i).getUsername());
            tempMatch.setopponentUserName(listyGrajkow.getGrajkiB().get(i).getUsername());
            runda.getMecze().add(tempMatch);
        }

        calyTerminarz.add(runda);
        System.out.println("=======" + runda.getNr() + " === " + runda.getData());
        listyGrajkow.przesunListy();
        /////////////////////////////////////////////
    }



///////////////////////////////////////////zwykly plik
 /*       FileOutputStream fos2 = new FileOutputStream("terminarz.tmp");
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(calyTerminarz);
        oos2.close();

*/


    /////////////// zapis termnarza do xml    //////////////////////
     terminarz = new Terminarz(calyTerminarz);
    jaxbObjectToXML(terminarz);

/////////////////////////////////////////////
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
    numerRundy=0;
}
        model.addAttribute("runda", terminarz.getTerminarz().get(numerRundy));
        model.addAttribute("mecze", terminarz.getTerminarz().get(numerRundy).getMecze());
model.addAttribute("numerRundy", numerRundy);


        return "LK/terminarz";
    }



    @PostMapping("/pokazRunde")
    public String pokazRunde(@RequestParam(value = "participant", required = true)Integer numerRundy)
    {
int oo=9;
     this.numerRundy=numerRundy-1;
        return "redirect:/terminarz";
//return "redirect:/LK/terminarz";
    }

    protected void dodajPauzeDlaParzystosci(List<UserData> grajki) {
        if (grajki.size() % 2 != 0) {
            UserData tempuser = new UserData();
            tempuser.setUsername("pauza");
            grajki.add(tempuser);
        }
    }

    public void jaxbObjectToXML(Terminarz calyTerminarz) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Terminarz.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Store XML to File
            File file = new File("terminarz.xml");

            //Writes XML file to file-system
            jaxbMarshaller.marshal(calyTerminarz, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
    public Terminarz jaxbXMLToObject(String terminarz) {
        Terminarz terminarz1= null;
        try {
            JAXBContext ctx = JAXBContext.newInstance(Terminarz.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            File file = new File(terminarz);

            if (file.exists()) {

                terminarz1 = (Terminarz) unmarshaller.unmarshal(file);




            System.out.println(terminarz1.getTerminarz());
            return terminarz1;
        }
            else
            {
                return null;
            }

        } catch (JAXBException e) {
           return null;
        }


    }

    class ListyGrajkow {
        private List<UserData> grajkiPrzesunieteA;//= new ArrayList<>();
        private List<UserData> grajkiPrzesunieteB;//= new ArrayList<>();
        private List<UserData> grajkiA;
        private List<UserData> grajkiB;

        public List<UserData> getGrajkiPrzesunieteA() {
            return grajkiPrzesunieteA;
        }

        public void setGrajkiPrzesunieteA(List<UserData> grajkiPrzesunieteA) {
            this.grajkiPrzesunieteA = grajkiPrzesunieteA;
        }

        public List<UserData> getGrajkiPrzesunieteB() {
            return grajkiPrzesunieteB;
        }

        public void setGrajkiPrzesunieteB(List<UserData> grajkiPrzesunieteB) {
            this.grajkiPrzesunieteB = grajkiPrzesunieteB;
        }

        public List<UserData> getGrajkiA() {
            return grajkiA;
        }


        public List<UserData> getGrajkiB() {
            return grajkiB;
        }


        public ListyGrajkow(List<UserData> grajkiA, List<UserData> grajkiB) {
            this.grajkiA = grajkiA;
            this.grajkiB = grajkiB;
            grajkiPrzesunieteA = new ArrayList<>();
            grajkiPrzesunieteB = new ArrayList<>();
        }

        public void przesunListy() {
            //przesuwanie
            List<UserData> grajkiPrzesunieteA = new ArrayList<>();
            List<UserData> grajkiPrzesunieteB = new ArrayList<>();

            //A
            for (int i = 0; i < grajkiA.size(); i++) {
                if (i != 0 && i != 1) {
                    grajkiPrzesunieteA.add(grajkiA.get(i - 1));
                } else {
                    if (i == 0) {
                        grajkiPrzesunieteA.add(grajkiA.get(0));
                    } else {
                        grajkiPrzesunieteA.add(grajkiB.get(0));
                    }
                }
            }
//B
            for (int i = 0; i < grajkiB.size(); i++) {
                if (i != 0) {
                    grajkiPrzesunieteB.add(grajkiB.get(i));
                }
            }
            grajkiPrzesunieteB.add(grajkiA.get(grajkiA.size() - 1));

            //////////////

            // grajkiA.clear();
            //   grajkiB.clear();
//            for (int i = 0; i < grajkiPrzesunieteA.size(); i++) {
//                grajkiA.set(i,grajkiPrzesunieteA.get(i));
//                grajkiB.set(i,grajkiPrzesunieteB.get(i));
//            }

            grajkiA = grajkiPrzesunieteA;
            grajkiB = grajkiPrzesunieteB;
            //    grajkiPrzesunieteA.clear();
            //    grajkiPrzesunieteB.clear();


            //    return new listyGrajkow();
        }
    }
}
