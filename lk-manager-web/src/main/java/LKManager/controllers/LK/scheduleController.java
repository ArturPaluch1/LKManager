package LKManager.controllers.LK;

import LKManager.DAO.MatchDAO;
import LKManager.DAO.RoundDAOImpl;
import LKManager.DAO.ScheduleDAO;
import LKManager.DAO.UserDAOImpl;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import LKManager.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
//@Validated
//@RequestMapping({"schedule.html", "/terminarz", "terminarz"})
public class scheduleController {
    private final MZUserService MZUserService;
    private final MatchDAO matchDAOInterface;

    // private Integer numerRundy;
    private final LKUserService lkUserService;
    private final ScheduleService scheduleService;
    private final PlikiService plikiService;
    private final ScheduleDAO scheduleDAO;
    //   private String wybranyTerminarz;
    private final UserDAOImpl userDAO;
    private final RoundDAOImpl rundaDAO;
    private final MatchDAO matchDAO;
    private final CookieManager cookieManager;
    /*  public Integer getNumerRundy() {
          return numerRundy;
      }

      public void setNumerRundy(Integer numerRundy) {
          this.numerRundy = numerRundy;
      }*/
    @Autowired
    private MZCache mzCache;

    public scheduleController(MZUserService MZUserService, MatchDAO matchDAOInterface, LKUserService lkUserService, ScheduleService scheduleService, PlikiService plikiService, ScheduleDAO scheduleDAO, UserDAOImpl userDAO, RoundDAOImpl rundaDAO, MatchDAO matchDAO, CookieManager cookieManager) {
        this.MZUserService = MZUserService;
        this.matchDAOInterface = matchDAOInterface;

        this.lkUserService = lkUserService;

        this.scheduleService = scheduleService;
        this.plikiService = plikiService;
        this.scheduleDAO = scheduleDAO;
        this.userDAO = userDAO;
        this.rundaDAO = rundaDAO;
        this.matchDAO = matchDAO;
        this.cookieManager = cookieManager;
    }


    @GetMapping("/schedule")
    public String getSchedule(HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam(value = "roundNumber", required = false) String roundNumber, @RequestParam(value = "chosenSchedule", required = false) String chosenSchedule) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, ParseException, URISyntaxException {
        //, @CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie,@CookieValue(value = "numerRundy", defaultValue = "1") String numerRundyCookie

        //   Arrays.stream(request.getCookies()).forEach(a-> System.out.println(a.getValue()));
//wybranyTerminarz="ja i kyo";
        //      wybranyTerminarzCookie= null;
//wybranyTerminarz= "ja i kyo";
        // var terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);
        //    Cookie numerRundyCookie = null;
        //    Cookie wybranyTerminarzCookie=null;

        // Optional<Cookie> numerRundyCookieOptional = Optional.ofNullable(Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("numerRundy")).findFirst().orElse(null));
        //       Optional<Cookie> wybranyTerminarzCookieOptional = Optional.ofNullable(Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("wybranyTerminarz")).findFirst().orElse(null));

        //    if(numerRundyCookieOptional.isPresent())
        //     {


        ///////////////////////////
        //        nrRundy=    cookieManager.saveOrUpdateNumerRundyCookie(response,request,nrRundy, wybranyTerminarz);
        //////////////////////
        //  nrRundy="1";

        roundNumber = CookieManager.saveOrUpdateRoundNumberCookie(Optional.ofNullable(roundNumber), Optional.ofNullable(chosenSchedule), response, request, scheduleDAO);
        chosenSchedule = CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(chosenSchedule), response, request, scheduleDAO);
        //    }
        //    else
        //    {

        //   }
        //  if(wybranyTerminarzCookieOptional.isPresent())
        //   {

        //         wybranyTerminarz=  cookieManager.saveOrUpdateChosenScheduleCookie(response,wybranyTerminarz,request);

        // }
        //    else
        //   {

        //   }












        /*wybranyTerminarzCookie.setMaxAge(0);
        response.addCookie(wybranyTerminarzCookie);*/
        //=  Arrays.stream(request.getCookies()).anyMatch(a->a.getName().equals("wybranyTerminarz"));

//wybranyTerminarz="ja i kyo";


        System.out.println("w terminarze =mzCache.getSchedulesFromCacheOrDatabase()");
        var schedules = mzCache.getSchedulesFromCacheOrDatabase();

        //   terminarz= terminarzDAO.findByTerminarzId(106);
        //   var terminarz11=     terminarzDAO.findByTerminarzName("ja i kyo");

        //podano nazwe terminarza


        Schedule schedule = null;
        if (chosenSchedule != null) {


            //sprawdzanie czy jest taki terminarz

            //   terminarz = terminarzService.wczytajTerminarz(wybranyTerminarz);


            //            terminarz=     terminarzDAO.findByTerminarzName(wybranyTerminarz);

            System.out.println("w mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(wybranyTerminarz);");
            schedule = mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(chosenSchedule);


            //nie znaleziono przekierowanie do tworzenia
            if (schedule == null) {
//todo przekierowanie że nie ma takiego terminarza
                return "redirect:/errorMessage";
            }


        }
        //nie podano nazwy terminarza
        else {
//ostatni wg id
            //  terminarz=     terminarzDAO.findLastById();
            mzCache.findLastScheduleByIdFromCacheOrDatabase();
            //nie ma żadnych terminarzy
            if (schedule == null) {
                return "redirect:/addSchedule";
            } else  //był taki terminarz
            {

            }
        }

        Round round = rundaDAO.findByScheduleIdAndRoundId(schedule.getId(), Integer.parseInt(roundNumber) - 1);
        List<Round> rounds = rundaDAO.findAllByScheduleId(schedule.getId());
    //    List<Match> mecze = meczDAO.findAllById(Collections.singleton(terminarz.getId()));
        List<Match> matches = matchDAO.findAllByScheduleIdAndRoundId(schedule.getId(), Integer.parseInt(roundNumber) - 1);
        //  mecze=mzCache.getTerminarze().get(0).getRundy().get(0).getMecze();
        // model.addAttribute("wybranyTerminarz", terminarze.get(0));
//mecze=terminarze.get(0).getRundy().get(0).getMecze();

        System.out.println("---cache:  ");

        model.addAttribute("chosenSchedule", chosenSchedule);
        model.addAttribute("rounds", rounds);
        //   model.addAttribute("nrRundy",terminarz.getRundy());
        //   model.addAttribute("runda", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1));
        model.addAttribute("round", round);
        //   model.addAttribute("mecze", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1).getMecze()  );
        model.addAttribute("matches", matches);

        model.addAttribute("roundNumber", roundNumber);


        model.addAttribute("schedules", schedules);


        return "LK/schedule/schedule";
    }


    @GetMapping(value = "/addSchedule")
    public String dodajTerminarz(Model model) throws JAXBException, IOException, ParserConfigurationException, SAXException {

        //lkUserService.wczytajGraczyZXML();
        List<UserData> players = userDAO.findAll(false);

        players = players.stream().sorted(
                (o1, o2) -> o1.getUsername().compareToIgnoreCase(o2.getUsername())
        ).collect(Collectors.toList());

 /*
List<graczOpakowanie>graczeOpakowani = new ArrayList<>();

//to jest niepotrzebne//////////////////////// todo
    for (var gracz: gracze

         ) {
        graczeOpakowani.add(new graczOpakowanie(false,gracz));
    }

*/


        var terminarzCommand = new TerminarzCommand();



        //  Bob b= new Bob();




    /*Map<String,String>wybraniGracze1 = new HashMap<>();
    wybraniGracze1.put("bob","sam");
    wybraniGracze1.put("bob1","sam1");

    terminarzCommand.mapa=wybraniGracze1;*/
        terminarzCommand.playersList = new ArrayList<String>();

        //terminarzCommand.bob=b;
      //  model.addAttribute("userListWrapper", terminarzCommand);
        model.addAttribute("players", players);
        model.addAttribute("schedule", terminarzCommand);
List<String> playerNames = new ArrayList<String>();
        players.forEach(p-> playerNames.add(p.getUsername()));
        model.addAttribute("playerNames", playerNames);

        return "LK/schedule/addSchedule";
    }


    @GetMapping(value = "/schedule/chose")
    public String wybierzTerminarz() {
        return "LK/schedule/choseSchedule";
    }


    @GetMapping("/deleteSchedule")
    public String usuwanieTerminarza(Model model)//@RequestParam (value = "wybranyTerminarz", required = true)String terminarzDoUsuniecia)
    {
        List<Schedule> schedules = mzCache.getSchedulesFromCacheOrDatabase();
        //plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);

//this.wybranyTerminarz=null;
        model.addAttribute("schedules", schedules);
        return "LK/schedule/deleteSchedule";


    }

    @PostMapping("/deleteSchedule")
    public String usunTerminarz(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "chosenSchedule", required = true) String scheduleToDelete)//, @CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie)
    {


        try {/*
            File plikDoUsuniecia= new File("Data/terminarze/"+terminarzDoUsuniecia);
            plikDoUsuniecia.delete();
            */

       /*     if(wybranyTerminarzCookie.equals("null")||wybranyTerminarzCookie.equals(terminarzDoUsuniecia))
            { Cookie cookieNumerRundy=null;
                cookieNumerRundy = new Cookie("numerRundy", "1");
                cookieNumerRundy.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                cookieNumerRundy.setSecure(true);
                cookieNumerRundy.setHttpOnly(true);
                response.addCookie(cookieNumerRundy);

                Terminarz terminarz1= terminarzDAO.findLastById();

                Cookie cookieTerminarz=null;
                cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode(terminarz1.getName(),"utf-8") );
                cookieTerminarz.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                cookieTerminarz.setSecure(true);
                cookieTerminarz.setHttpOnly(true);

            }*/
            scheduleDAO.deleteByName(scheduleToDelete);

            mzCache.setSchedules(scheduleDAO.findAll());


            Schedule schedule1 = scheduleDAO.findLastById();
            try {

                //       Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
                //        Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter( a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);
/*
                cookieManager.saveOrUpdateChosenScheduleCookie(response,terminarz1.getName(),request, terminarzDAO);
                cookieManager.saveOrUpdateNumerRundyCookie(response,"1",request);
       */
                //todo wyzej bkp


                // CookieManager.checkCookies(response,request,"1",terminarz1.getName(),terminarzDAO);

            } catch (Exception e) {

            }
            CookieManager.saveOrUpdateRoundNumberCookie(Optional.of(""), Optional.ofNullable(schedule1.getName()), response, request, scheduleDAO);
            CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(schedule1.getName()), response, request, scheduleDAO);
            attributes.addAttribute("chosenSchedule", schedule1.getName());
            attributes.addAttribute("roundNumber", "1");
            return "redirect:/schedule";
        } catch (Exception e) {
            attributes.addAttribute("errorMessage", "błąd w usuwaniu");
            System.out.println(e);
            return "redirect:/errorMessage";
        }

    }

    @PostMapping("/addSchedule")
    public String stworzTerminarz(HttpServletResponse response, HttpServletRequest request, RedirectAttributes attributes, @ModelAttribute @Valid TerminarzCommand command, @RequestParam(value = "chosenPlayers", required = false) List<String> chosenPlayers) throws DatatypeConfigurationException, JsonProcessingException {
//,@CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie,@CookieValue(value = "numerRundy", defaultValue = "1") String numerRundyCookie


        //todo walidacja  te same nazwy
/*var terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);
if(Arrays.stream(terminarze).anyMatch(a->a.getName().trim().equals(command.getNazwa().trim()+".xml")))
{

   int y=0;
}*/

        Schedule schedule = null;


   /*     XMLGregorianCalendar data = DatatypeFactory.newInstance().newXMLGregorianCalendar();

        data.setYear(Integer.parseInt(command.data.trim().split("-")[0]));
        data.setMonth(Integer.parseInt(command.data.split("-")[1]));
        data.setDay(Integer.parseInt(command.data.split("-")[2]));
*/
        LocalDate date = LocalDate.parse(command.getDate().trim(), DateTimeFormatter.ofPattern("yyyy-M-d"));
        //    LocalDateTime data = LocalDateTime.of(command.data.trim().split("-")[0],
        //           command.data.split("-")[1]))
////////////////////////////////////////


        List<UserData> players = userDAO.findAll(false);

        //nie wybrano graczy do wielodniowego terminarza
        if (chosenPlayers == null) {
            if (command.getPlayersList().size() == 0) {
                //todo nie wybrano tez pojedynczych meczy
                int tt = 0;
            } else {
                if (command.getPlayersList().size() % 2 != 0) {
                    //todo brakuje pary dla grajka  ewentuanie w js wybierzgrajka-> pauza
                } else {
                    List<UserData> templist = new ArrayList<>();
                    for (int i = 0; i < command.playersList.size(); i++
                    ) {


                        for (int j = 0; j < players.size(); j++) {


                            if (players.get(j).getUsername().equals(command.playersList.get(i))) {
                                templist.add(players.get(j));
                                j = 0;
                                break;
                            }
                        }
                    }
//////////////////////////////
                    schedule = scheduleService.utworzTerminarzJednodniowy(date, templist, command.name);

                }

            }
        } else {
            List<UserData> templist = new ArrayList<>();
            for (var player : players
            ) {
                for (int i = 0; i < chosenPlayers.size(); i++
                ) {
                    if (player.getUsername().equals(chosenPlayers.get(i))) {
                        templist.add(player);
                        i = 0;
                        break;
                    }
                }
            }
//////////////////////////////
            schedule = scheduleService.utworzTerminarzWielodniowy(date, templist, command.name);
        }

        try {
            //    Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
  //          scheduleDAO.save(terminarz);
            if (mzCache.getSchedules().size() != 0)
                mzCache.getSchedules().add(schedule);
/*
 Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter( a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);
            cookieManager.saveOrUpdateChosenScheduleCookie(response, terminarz.getName(), request, terminarzDAO);
       */
            //todo wyzej bkp
            //   CookieManager.checkCookies(response,request,"1",terminarz.getName(),terminarzDAO);
            CookieManager.saveOrUpdateRoundNumberCookie(Optional.of("1"), Optional.ofNullable(schedule.getName()), response, request, scheduleDAO);
            CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(schedule.getName()), response, request, scheduleDAO);
            attributes.addAttribute("chosenSchedule", schedule.getName());
            attributes.addAttribute("roundNumber", "1");
            return "redirect:/schedule";
        } catch (Exception e) {
            System.out.println("error in addSchedule - post");
            attributes.addAttribute("errorMessage","error in addSchedule - post");
            return "redirect:/errorMessage";
        }
    }


    @PostMapping("/showRound")
    public String pokazRunde(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "roundNumber", required = true) Integer roundNumber, @RequestParam(value = "chosenSchedule", required = true) String chosenSchedule) {
//cookieManager.saveOrUpdateNumerRundyCookie(response, numerRundy.toString(),request);
        //todo wyzej bpk
        try {
            //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            CookieManager.saveOrUpdateRoundNumberCookie(Optional.of(roundNumber.toString()), Optional.ofNullable(chosenSchedule), response, request, scheduleDAO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.addCookie(new Cookie("roundNumber", roundNumber.toString()));
        //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
        //   this.numerRundy=numerRundy;
        attributes.addAttribute("chosenSchedule", Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("chosenSchedule")).findAny().orElse(null).getValue());
        attributes.addAttribute("roundNumber", roundNumber.toString());

        return "redirect:/schedule";

    }

    @GetMapping(value = "/schedule/changeSchedule")
    public String changeSchedule(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "chosenSchedule", required = false) String chosenSchedule) {

        try {
            //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            CookieManager.saveOrUpdateRoundNumberCookie(Optional.of("1"), Optional.ofNullable(chosenSchedule), response, request, scheduleDAO);
            CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(chosenSchedule), response, request, scheduleDAO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // response.addCookie(new Cookie("numerRundy","1"));
        // response.addCookie(new Cookie("wybranyTerminarz",wybranyTerminarz));
        //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
        //   this.numerRundy=numerRundy;
        attributes.addAttribute("chosenSchedule", chosenSchedule);
        attributes.addAttribute("roundNumber", "1");

        return "redirect:/schedule";
    }

   @Getter
    @Setter
    @NoArgsConstructor
    public class TerminarzCommand {
/*todo walidacja
https://blog.mloza.pl/java-bean-validation-spring-boot-sprawdzanie-poprawnosci-danych-w-spring-boocie/

*/

        @NotNull
        private String date;
        @NotBlank
        private String name;
        private List<String> playersList;



      public void setListaGraczy() {

        }


    }


/*

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound()
    {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("temp");
        return  mav;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class NotFoundException extends  RuntimeException{
        public NotFoundException() {
            super();
        }

        public NotFoundException(String message) {
            super(message);
        }

        public NotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
*/
}
