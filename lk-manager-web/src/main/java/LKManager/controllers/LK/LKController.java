package LKManager.controllers.LK;

import LKManager.LK.Terminarz;
import LKManager.services.TeamTM;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import LKManager.services.MatchService;
import LKManager.services.TerminarzService;
import LKManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Controller
public class LKController {
    private final UserService userService;
    private final MatchService matchService;
    private  final TerminarzService terminarzService;
    private  final TeamTM teamTM;
  //  private List<UserData> skladTM = new ArrayList<>();

    public LKController(UserService userService, MatchService matchService, TerminarzService terminarzService, TeamTM teamTM) {
        this.userService = userService;
        this.matchService = matchService;
        this.terminarzService = terminarzService;

        this.teamTM = teamTM;



    }


    @RequestMapping({"LKManager.html", "LKManager/LK", "lk", "lk.html", "html"} )
    public String index() throws JAXBException, IOException, ParserConfigurationException, SAXException, DatatypeConfigurationException {
///////////////////////
// tu testuje files
/*


      //  Scanner scan= new Scanner(new File("foo.txt")); //doesn't work
        File file = new File("foo.txt");
        Scanner scan1= new Scanner(new FileReader("foo.txt")); //does;

        System.out.println("=====================================");
        System.out.println(file.exists());
           System.out.println(file.canRead());
        FileReader input = new FileReader("foo.txt");
        System.out.println(file.getPath());
        FileReader fr = new FileReader(file);
        System.out.println( fr.read());

        char[] array = new char[100];
        input.read(array);
        System.out.println("Data in the file: ");
        System.out.println(array);

        // Closes the reader
        input.close();

      fr.close();

//scan.close();
scan1.close();*/
/*
       var team= teamTM.LoadLigaPSG();
        XMLGregorianCalendar data= DatatypeFactory.newInstance().newXMLGregorianCalendar();
        data.setDay(1);
        data.setMonth(1);
        data.setYear(2000);
      terminarzService.utworzTerminarz(data, team);





        //Create JAXB Context
      JAXBContext jaxbContext = JAXBContext.newInstance(Terminarz.class);

        //Create Marshaller
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        //Required formatting??
       jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        //Store XML to File


      //InputStream in = this.getClass().getResourceAsStream("/Data/terminarz.xml");

      //    File targetFile = new File(in.toString());

        // var targetFile =  this.getClass().getResourceAsStream("/Data/terminarz.xml");
        //OutputStream outStream = new FileOutputStream(targetFile);

      //  jaxbMarshaller.marshal(terminarzService.wczytajTerminarz(),file);





        String path = new String("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");


        Terminarz terminarz1= null;

        JAXBContext ctx = JAXBContext.newInstance(Terminarz.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        File file = new File(path);

     //   if (file.exists()) {

            terminarz1 = (Terminarz) unmarshaller.unmarshal(file);

*/




    /*    }
        else
        {
            System.out.println("null");
        }
*/


///////////////////


        return "LK/index";
    }
    @RequestMapping(value="/wyswietlRozegrane")
public String wyswietlRozegrane(Model model)throws IOException, SAXException, ParserConfigurationException, JAXBException
    {
        List<Match> tempMatches = new ArrayList<>();

        UserData opponent = new UserData();

        TeamTM rzeszow= new TeamTM(userService);



        for (UserData user : rzeszow.LoadLKUPSGV()
        ) {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<Match> matches = matchService.findPlayedByUsername(user.getUsername()).getMatches().stream().filter(a -> LocalDate.parse(a.getDate(), formatter).isAfter(LocalDate.now().minusDays(7))).collect(Collectors.toList());
            for (Match match : matches) {

                LocalDate ld = LocalDate.parse(match.getDate(), formatter);
                if (ld.getDayOfWeek().equals(DayOfWeek.FRIDAY) && match.getType().equals("friendly")) {

                    if (match.getTeamlist().get(0).getTeamName().equals(user.getTeamlist().get(0).getTeamName())) {
                        opponent = userService.findById(match.getTeamlist().get(1).getTeamId());
                        match.setopponentUser(opponent);
                        match.setUser(user);
                        tempMatches.add(match);

                    } else {
                        opponent = userService.findById(match.getTeamlist().get(0).getTeamId());
                        match.setopponentUser(opponent);
                        String opponentTeamName = match.getTeamlist().get(0).getTeamName();
                        match.setUser(user);
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


return "LKManager/LK/index";
    }
    @RequestMapping(value="/wyswietlNierozegrane")
  public String  wyswietlNierozegrane(Model model) throws ParserConfigurationException, JAXBException, SAXException, IOException {

        List<Match> tempMatches = new ArrayList<>();

        UserData opponent =new UserData();

        TeamTM rzeszow= new TeamTM(userService);



        for (UserData user : rzeszow.LoadLKUPSGV()
        ) {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<Match> matches = matchService.findPlannedByUsername(user.getUsername()).getMatches().stream().filter(a -> LocalDate.parse(a.getDate(), formatter).isAfter(LocalDate.now())).collect(Collectors.toList());
            for (Match match : matches) {

                LocalDate ld = LocalDate.parse(match.getDate(), formatter);
                if (ld.getDayOfWeek().equals(DayOfWeek.FRIDAY) && match.getType().equals("friendly")) {

                    if (match.getTeamlist().get(0).getTeamName().equals(user.getTeamlist().get(0).getTeamName())) {
                        opponent = userService.findById(match.getTeamlist().get(1).getTeamId());
                        match.setopponentUser(opponent);
                        match.setUser(user);
                        tempMatches.add(match);

                    } else {
                        opponent = userService.findById(match.getTeamlist().get(0).getTeamId());
                        match.setopponentUser(opponent);
                        String opponentTeamName = match.getTeamlist().get(0).getTeamName();
                        match.setUser(user);
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

        return "LKManager/LK/index";
    }
}
