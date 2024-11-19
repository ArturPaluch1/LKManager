package LKManager.controllers.LK;

import LKManager.DAO_SQL.CustomUserDAOImpl;
import LKManager.DAO_SQL.MatchDAO;
import LKManager.DAO_SQL.RoundDAOImpl;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.model.RecordsAndDTO.*;
import LKManager.model.ScheduleStatus;
import LKManager.services.*;
import LKManager.services.FilesService_unused.PlikiService;
import LKManager.services.Scheduling.SwissScheduleResultsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller @RequiredArgsConstructor
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
    private final CustomUserDAOImpl userDAO;
    private final RoundDAOImpl rundaDAO;
    private final MatchDAO matchDAO;
    private final UserService userService;
    private final CookieManager cookieManager;


    //temp
    private final ResultsService resultsService;
    private final TableService tableService;
    ////////////////
private final SwissScheduleResultsService swissScheduleResultsService;


    /*  public Integer getNumerRundy() {
          return numerRundy;
      }

      public void setNumerRundy(Integer numerRundy) {
          this.numerRundy = numerRundy;
      }*/
   /* @Autowired
    private MZCache mzCache;
*/
@GetMapping("/admin/LK/schedule/scheduleError")
public String scheduleError(Model model )
{
    System.out.println("in admin/LK/schedule/scheduleError");
    var scheduleNames = scheduleService.getScheduleNamesOngoingOrFinished();
    model.addAttribute("schedules", scheduleNames);

    return"admin/LK/schedule/scheduleError";
}



/*
@GetMapping("/admin/LK/schedule/schedule")
public String getScheduleAdminTemp(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam(value = "roundNumber", required = false) String roundNumber, @RequestParam(value = "chosenSchedule", required = false) String chosenSchedule) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
    roundNumber = cookieManager.saveOrUpdateRoundNumberCookie(roundNumber, chosenSchedule);
    chosenSchedule = cookieManager.saveOrUpdateChosenScheduleCookie(chosenSchedule);

    System.out.println("w terminarze scheduleService.getSchedules()");
    var scheduleNames = scheduleService.getScheduleNames();


    ScheduleDTO schedule = null;
    if (chosenSchedule != null) {


        System.out.println("w mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(wybranyTerminarz);");
        //  schedule = mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(chosenSchedule);
        schedule = scheduleService.getSchedule_ByName(chosenSchedule);

        //nie znaleziono przekierowanie do tworzenia
        if (schedule == null) {
//todo przekierowanie że nie ma takiego terminarza
            //   return "redirect:/errorMessage";

            model.addAttribute("schedules", scheduleNames);
            //     return "LK/schedule/schedule";



            return "redirect:/admin/LK/scheduleError";
        }


    }
    //nie podano nazwy terminarza
    else {
//ostatni wg id


        //   mzCache.findLastScheduleByIdFromCacheOrDatabase();
        //todo tu musi byc: schedule=
        scheduleService.getSchedule_TheNewest();
        System.out.println("the newest");
        //nie ma żadnych terminarzy
        if (schedule == null) {
            return "redirect:/admin/LK/addSchedule";
        } else  //był taki terminarz
        {

        }
    }

    ScheduleDTO  result=null;

    result=   scheduleService.getSchedule_ByName("swiss23");
    if(result!=null)
    {
        Table  table=tableService.createSwissScheduleTable(scheduleService.getSchedule_ById(result.getId()));

        scheduleService.calculateNextRoundOfSwissSchedule(scheduleService.getSchedule_ById(result.getId()),table);
        //  resultsService.simulateResults(scheduleService.getSchedule_ById(result.getId()),2);


        //  resultsService.simulateResults(scheduleService.getSchedule_ById(result.getId()),0+1);
        for (int round = 1; round < result.getRounds().size(); round++) {
            //  resultsService.simulateResults(scheduleService.getSchedule_ById(result.getId()),round+1);


            //  tableService.createSwissScheduleTable(result);

            //    result=   scheduleService.getSchedule_ByName("swiss1");
        }


          */
/*  while(result.getRounds().size()<2)

                     {

                       Table  table=tableService.createSwissScheduleTable(scheduleService.getSchedule_ById(result.getId()));
                         scheduleService.calculateNextRoundOfSwissSchedule(scheduleService.getSchedule_ById(result.getId()),table);


//resultsService.updateResults()
                resultsService.simulateResults(scheduleService.getSchedule_ById(result.getId()),);
              tableService.createSwissScheduleTable(result);

                         result=   scheduleService.getSchedule_ByName("swiss1");

            }*//*







    }


    model.addAttribute("chosenSchedule", chosenSchedule);
    model.addAttribute("schedule", schedule);


    //  List<Round> rounds = null;//new ArrayList<>(schedule.getRounds());
    //     rounds.sort(Comparator.comparing(Round::getNr));
    //    model.addAttribute("rounds", rounds);
    RoundDTO round;
    //zabezpieczenie przed błędem w cookies
    if(schedule.getRounds().size()<=Integer.parseInt(roundNumber) -1)
    {
        round=schedule.getRounds().get(0);
    }
    else
        round=schedule.getRounds().get(Integer.parseInt(roundNumber) - 1);
    model.addAttribute("round", round);

    //   List<Match> matches =round.getMatches();
    //    model.addAttribute("matches",schedule.getRounds().get(Integer.parseInt(roundNumber) - 1).getMatchesDTO());

    model.addAttribute("roundNumber", roundNumber);


    model.addAttribute("schedules", scheduleNames);
    return "admin/LK/schedule/schedule";

    */
/**
     *
     *
     model.addAttribute("chosenSchedule", chosenSchedule);


     List<Round> rounds = null;//new ArrayList<>(schedule.getRounds());
     rounds.sort(Comparator.comparing(Round::getNr));
     model.addAttribute("rounds", rounds);

     Round  round=rounds.get(Integer.parseInt(roundNumber) - 1);
     model.addAttribute("round", round);

     List<Match> matches =round.getMatches();
     model.addAttribute("matches", matches);

     model.addAttribute("roundNumber", roundNumber);


     model.addAttribute("schedules", scheduleNames);
     return "LK/schedule/schedule";
     *//*

}
*/

    @GetMapping({"/admin/LK/schedule/schedule"})
    public String getSchedule(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam(value = "roundNumber", required = false) String roundNumber, @RequestParam(value = "chosenSchedule", required = false) String chosenSchedule) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
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

        roundNumber = cookieManager.saveOrUpdateRoundNumberCookie(roundNumber, chosenSchedule);
        chosenSchedule = cookieManager.saveOrUpdateChosenScheduleCookie(chosenSchedule);
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

if(chosenSchedule!=null) {
    System.out.println("w terminarze scheduleService.getSchedules()");
    //  var schedules = mzCache.getSchedulesFromCacheOrDatabase();
    var scheduleNames = scheduleService.getScheduleNamesOngoingOrFinished();
    //   terminarz= terminarzDAO.findByTerminarzId(106);
    //   var terminarz11=     terminarzDAO.findByTerminarzName("ja i kyo");

    //podano nazwe terminarza


    ScheduleDTO schedule = null;
    if (chosenSchedule != null) {


        //sprawdzanie czy jest taki terminarz

        //   terminarz = terminarzService.wczytajTerminarz(wybranyTerminarz);


        //            terminarz=     terminarzDAO.findByTerminarzName(wybranyTerminarz);

        System.out.println("w mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(wybranyTerminarz);");
        //  schedule = mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(chosenSchedule);
        schedule = scheduleService.getSchedule_ByName(chosenSchedule);

        //nie znaleziono przekierowanie do tworzenia
        if (schedule == null) {
//todo przekierowanie że nie ma takiego terminarza
            //   return "redirect:/errorMessage";

            model.addAttribute("schedules", scheduleNames);
            //     return "LK/schedule/schedule";


            return "redirect:/admin/LK/schedule/scheduleError";
        }


    }
    //nie podano nazwy terminarza
    else {
//ostatni wg id


        //   mzCache.findLastScheduleByIdFromCacheOrDatabase();
        //todo tu musi byc: schedule=
        schedule = scheduleService.getSchedule_TheNewestOngoingOrFinished();
        System.out.println("the newest");
        //nie ma żadnych terminarzy
        if (schedule == null) {
            return "redirect:admin/LK/addSchedule";
        }
    }


    //    List<Match> mecze = meczDAO.findAllById(Collections.singleton(terminarz.getId()));

    //  mecze=mzCache.getTerminarze().get(0).getRundy().get(0).getMecze();
    // model.addAttribute("wybranyTerminarz", terminarze.get(0));
//mecze=terminarze.get(0).getRundy().get(0).getMecze();

    //  System.out.println("---cache:  ");




/*result=   scheduleService.getSchedule_ByName("swiss2");
if(result==null)
{
    List<UserDataDTO> tempusers = userService.findAllUsers(false, false).stream().limit(40).collect(Collectors.toList());
     result= ScheduleAdapter.adapt(scheduleService.createSwissScheduleWithPlayerData(LocalDate.now(), tempusers, "swiss1",2).schedule());


    resultsService.simulateResults(scheduleService.getSchedule_ById(result.getId()),1);

    Table    table1=    tableService.createSwissScheduleTable(scheduleService.getSchedule_ById(result.getId()));

}

     else //  if (result.getName().contains("swiss2")) ;
        {*/




    model.addAttribute("chosenSchedule", chosenSchedule);
    model.addAttribute("schedule", schedule);


    if(!schedule.getRounds().isEmpty()) //jeśli schedule jest ongoing  czasem może nie byc rund
    {
    //  List<Round> rounds = null;//new ArrayList<>(schedule.getRounds());
    //     rounds.sort(Comparator.comparing(Round::getNr));
    //    model.addAttribute("rounds", rounds);
    RoundDTO round;
    //zabezpieczenie przed błędem w cookies
    if (schedule.getRounds().size() <= Integer.parseInt(roundNumber) - 1) {
        round = schedule.getRounds().get(0);
    } else
        round = schedule.getRounds().get(Integer.parseInt(roundNumber) - 1);
    model.addAttribute("round", round);

    //   List<Match> matches =round.getMatches();
    //    model.addAttribute("matches",schedule.getRounds().get(Integer.parseInt(roundNumber) - 1).getMatchesDTO());

    model.addAttribute("roundNumber", roundNumber);
}

    model.addAttribute("schedules", scheduleNames);
    return "admin/LK/schedule/schedule";
}
 else  return "admin/LK/schedule/schedule";

        /**
         *
         *
         model.addAttribute("chosenSchedule", chosenSchedule);


         List<Round> rounds = null;//new ArrayList<>(schedule.getRounds());
         rounds.sort(Comparator.comparing(Round::getNr));
         model.addAttribute("rounds", rounds);

         Round  round=rounds.get(Integer.parseInt(roundNumber) - 1);
         model.addAttribute("round", round);

         List<Match> matches =round.getMatches();
         model.addAttribute("matches", matches);

         model.addAttribute("roundNumber", roundNumber);


         model.addAttribute("schedules", scheduleNames);
         return "LK/schedule/schedule";
         */
    }


    @GetMapping(value = "/admin/LK/schedule/schedule/addSchedule")
    public String addSchedule(Model model) throws JAXBException, IOException, ParserConfigurationException, SAXException {





        //lkUserService.wczytajGraczyZXML();
      //  List<UserData> players = userService.findUsers_NotDeletedWithPause();//userDAO.findUsersFromCache_NotDeletedWithPause();
        List<UserDataDTO> players = userService.findAllMZUsers(true,true);//userDAO.findUsersFromCache_NotDeletedWithPause();
     /*   players = players.stream().filter(p->p.getMZUsername()!=null).sorted(
                (o1, o2) -> o1.getMZUsername().compareToIgnoreCase(o2.getMZUsername())
        ).distinct().collect(Collectors.toList());*/

       players = players.stream().filter(p->p.getMZUsername()!=null)
                .sorted(
                        (o1, o2) -> o1.getMZUsername().compareToIgnoreCase(o2.getMZUsername()))
                .collect(Collectors.toMap(UserDataDTO::getMZUsername, player -> player, (existing, replacement) -> existing)) // Wybierz pierwszy wystąpienie
                .values()
                .stream()
               .sorted(Comparator.comparing(UserDataDTO::getMZUsername))
                .collect(Collectors.toList());
 /*
List<graczOpakowanie>graczeOpakowani = new ArrayList<>();

//to jest niepotrzebne//////////////////////// todo
    for (var gracz: gracze

         ) {
        graczeOpakowani.add(new graczOpakowanie(false,gracz));
    }

*/
        AddScheduleDTO addScheduleCommand= (AddScheduleDTO) model.getAttribute("schedule");
 if(addScheduleCommand ==null) {
     addScheduleCommand = new AddScheduleDTO();
     addScheduleCommand.setPlayersList( new ArrayList<String>());

 }
        //  Bob b= new Bob();




    /*Map<String,String>wybraniGracze1 = new HashMap<>();
    wybraniGracze1.put("bob","sam");
    wybraniGracze1.put("bob1","sam1");

    terminarzCommand.mapa=wybraniGracze1;*/


        //terminarzCommand.bob=b;
      //  model.addAttribute("userListWrapper", terminarzCommand);


  /*      if(addScheduleCommand.getScheduleType()==null)
            addScheduleCommand.setScheduleType(ScheduleType.standardSchedule);//ScheduleType.standardSchedule.name());

*/
        if(addScheduleCommand.getScheduleType()!=null)
            addScheduleCommand.setScheduleType(null);//ScheduleType.standardSchedule.name());


        model.addAttribute("players", players);
        model.addAttribute("schedule", addScheduleCommand);
List<String> playerNames = new ArrayList<>();
        players.forEach(p-> playerNames.add(p.getMZUsername()));
      playerNames.stream().distinct().sorted().collect(Collectors.toList());
        model.addAttribute("playerNames", playerNames);






        if ( model.getAttribute("errorMessages")!=null) {
//model.getAttribute("scheduleType");
            model.addAttribute("errorMessages", model.getAttribute("errorMessages"));
        }


        return "admin/LK/schedule/addSchedule";
    }


    @GetMapping(value = "/admin/LK/schedule/chose")
    public String pickSchedule() {
        return "admin/LK/schedule/choseSchedule";
    }


    @GetMapping("/admin/LK/schedule/schedule/deleteSchedule")
    public String deleteSchedule(Model model)//@RequestParam (value = "wybranyTerminarz", required = true)String terminarzDoUsuniecia)
    {
      //  List<Schedule> schedules = mzCache.getSchedulesFromCacheOrDatabase();
        List<ScheduleNameDTO> schedules = scheduleService.getScheduleNamesOngoingOrFinished();
        //plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);

//this.wybranyTerminarz=null;
        model.addAttribute("schedules", schedules);
        return "admin/LK/schedule/deleteSchedule";


    }

    @PostMapping("/admin/LK/schedule/schedule/deleteSchedule")
    public String deleteSchedule(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "chosenSchedule", required = true) String scheduleToDelete)//, @CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie)
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
         boolean result=   scheduleService.deleteSchedule(scheduleToDelete);





         //   Schedule schedule1 = scheduleDAO.findLastById();
            try {
                ScheduleDTO schedule1=scheduleService.getSchedule_TheNewestOngoingOrFinished();
                //       Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
                //        Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter( a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);

                cookieManager.saveOrUpdateChosenScheduleCookie(schedule1.getName());
                cookieManager.saveOrUpdateRoundNumberCookie("1",schedule1.getName());

                //todo wyzej bkp


                // CookieManager.checkCookies(response,request,"1",terminarz1.getName(),terminarzDAO);

            } catch (Exception e) {

            }
            if(result==true)
            {
                //todo make another html file for success
                attributes.addAttribute("errorMessage", "Usuwanie terminarza: "+scheduleToDelete+"\nZakończyło się sukcesem.");

                return "redirect:/public/LK/error";
            }
            else
            {
                attributes.addAttribute("errorMessage", "błąd w usuwaniu");

                return "redirect:/public/LK/error";
            }
        /*    CookieManager.saveOrUpdateRoundNumberCookie(Optional.of("1"), Optional.ofNullable(schedule1.getName()), response, request, scheduleDAO);
            CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(schedule1.getName()), response, request, scheduleDAO);
            attributes.addAttribute("chosenSchedule", schedule1.getName());
            attributes.addAttribute("roundNumber", "1");
            return "redirect:/schedule";*/
        } catch (Exception e) {
            attributes.addAttribute("errorMessage", "błąd w usuwaniu");
            System.out.println(e);
            return "redirect:/public/LK/error";
        }

    }

    @PostMapping("/admin/LK/schedule/schedule/addSchedule")
    public String addSchedule(HttpServletResponse response, HttpServletRequest request,Model model, RedirectAttributes attributes, @ModelAttribute @Valid AddScheduleDTO addScheduleDTO, BindingResult bindingResult, @RequestParam(value = "chosenPlayers", required = false) List<String> chosenPlayers) throws DatatypeConfigurationException, JsonProcessingException {
//,@CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie,@CookieValue(value = "numerRundy", defaultValue = "1") String numerRundyCookie



        if (bindingResult.hasErrors()) {
            List<String> errorMessages1 = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());






            Map<String, String> errorMessages = bindingResult.getAllErrors().stream()
                    .collect(Collectors.toMap(
                            error -> {
                                if (error instanceof FieldError) {
                                    return ((FieldError) error).getField();
                                } else if (error instanceof ObjectError) {
                                    return ((ObjectError) error).getObjectName();
                                }else return null;

                            },
                            error -> error.getDefaultMessage()
                    ));

            attributes.addFlashAttribute("errorMessages", errorMessages);

         // attributes.addFlashAttribute("org.springframework.validation.BindingResult.addScheduleDTO", bindingResult);


            attributes.addFlashAttribute("schedule", addScheduleDTO);
//model.addAttribute("org.springframework.validation.BindingResult.addScheduleDTO", bindingResult);
            return "redirect:/admin/LK/schedule/schedule/addSchedule"; // Nazwa widoku formularza
        }
 /*       if(command.getPlayersList().size()==0&&command.getScheduleType().equals(ScheduleType.oneDaySchedule))
        {
            attributes.addAttribute("errorMessage","nie zaplanowałeś meczy");
            return "redirect:/public/LK/error";
        }*/
        //todo walidacja  te same nazwy poprawić to
        ScheduleDTO scheduleInDB=scheduleService.getSchedule_ByName(addScheduleDTO.getName().trim());
        if(scheduleInDB!=null)
//if( scheduleService.getScheduleNames().contains(addScheduleDTO.getName().trim()))
{
    System.out.println("error in addSchedule - post  - schedule name already exists");
    attributes.addAttribute("errorMessage","error in addSchedule - post - schedule name already exists");
    return "redirect:/public/LK/error";
}
/*var terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);
if(Arrays.stream(terminarze).anyMatch(a->a.getName().trim().equals(command.getNazwa().trim()+".xml")))
{

   int y=0;
}*/

        ScheduleDTO schedule = null;


   /*     XMLGregorianCalendar data = DatatypeFactory.newInstance().newXMLGregorianCalendar();

        data.setYear(Integer.parseInt(command.data.trim().split("-")[0]));
        data.setMonth(Integer.parseInt(command.data.split("-")[1]));
        data.setDay(Integer.parseInt(command.data.split("-")[2]));
*/

  String[] dateParts=  addScheduleDTO.getDate().trim().split("-");
        for (int i = 0; i < dateParts.length; i++) {
            if (dateParts[i].length() == 1) {
                dateParts[i] = "0" + dateParts[i];
            }
        }
  LocalDate date= LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));


     //   LocalDate date= null;
  /*      try {
            date    = LocalDate.parse(command.getDate().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Użycie parsowanej daty
        } catch (DateTimeParseException e) {
            // Obsługa wyjątku parsowania daty
            System.out.println(e.getMessage());
        }*/

        //    LocalDateTime data = LocalDateTime.of(command.data.trim().split("-")[0],
        //           command.data.split("-")[1]))
////////////////////////////////////////



        CreateScheduleResult createScheduleResult=null;


        //todo sprawdzic czy może być null
        switch (addScheduleDTO.getScheduleType())
        {
            case oneDaySchedule:
            {
                if (addScheduleDTO.getPlayersList().size() == 0) {
                    //todo nie wybrano tez pojedynczych meczy
                    int tt = 0;
                } else {
                    if (addScheduleDTO.getPlayersList().size() % 2 != 0) {
                        //todo brakuje pary dla grajka  ewentuanie w js wybierzgrajka-> pauza
                    } else {

//////////////////////////////

                        createScheduleResult= scheduleService.createOneDayShedule(date, addScheduleDTO.getPlayersList(), addScheduleDTO.getName(),addScheduleDTO.getScheduleType(),ScheduleStatus.ONGOING);


                    }

                }
                break;
            }
            case standardSchedule:
            {
                createScheduleResult=  scheduleService.createMultiDaySchedule(date, chosenPlayers, addScheduleDTO.getName(),addScheduleDTO.getScheduleType(),ScheduleStatus.ONGOING);

                break;
            }
            case  swissSchedule:
            {
                createScheduleResult=  scheduleService.createSwissScheduleWithPlayerNames(date, chosenPlayers, addScheduleDTO.getName(),addScheduleDTO.getRoundsNumber(),addScheduleDTO.getScheduleType(),ScheduleStatus.ONGOING);
                //creating first round of swiss schedule
         //       swissScheduleResultsService.calculateNewRound();
                break;
            }
        }











        if(createScheduleResult.schedule()!=null)
        {
            try {
                //    Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
                //          scheduleDAO.save(terminarz);
                /** ****************************
                 * todo uncomment if need to use cache

                 if (mzCache.getSchedules().size() != 0)
                 mzCache.getSchedules().add(schedule);
                 */

/*
 Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter( a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);
            cookieManager.saveOrUpdateChosenScheduleCookie(response, terminarz.getName(), request, terminarzDAO);
       */
                //todo wyzej bkp
                //   CookieManager.checkCookies(response,request,"1",terminarz.getName(),terminarzDAO);


if(!createScheduleResult.schedule().getScheduleStatus().equals(ScheduleStatus.PLANNED))
{

    cookieManager.saveOrUpdateRoundNumberCookie("1",createScheduleResult.schedule().getName());
    cookieManager.saveOrUpdateChosenScheduleCookie(createScheduleResult.schedule().getName());
    attributes.addAttribute("chosenSchedule", createScheduleResult.schedule().getName());
    attributes.addAttribute("roundNumber", "1");
    return "redirect:/admin/LK/schedule/schedule";
}
else
{
    attributes.addAttribute("chosenSchedule", createScheduleResult.schedule().getName());
    attributes.addAttribute("roundNumber", "1");
    return "redirect:/admin/LK/schedule/schedule";
}

            } catch (Exception e) {
                System.out.println("error in addSchedule - post");
                attributes.addAttribute("errorMessage","error in addSchedule - post");
                return "redirect:/public/LK/error";
            }
        }
       else
        {
            String errorMessage="Schedule creation failed. Those players do not exist in MZ:\n"+createScheduleResult.playersNotInMZ().toString();
            attributes.addAttribute("errorMessage",errorMessage);
            return "redirect:/public/LK/error";
        }
    }


    @GetMapping("/admin/LK/schedule/schedule/showRound")
    public String showRound(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "roundNumber", required = true) Integer roundNumber, @RequestParam(value = "chosenSchedule", required = true) String chosenSchedule) {
//cookieManager.saveOrUpdateNumerRundyCookie(response, numerRundy.toString(),request);
        //todo wyzej bpk
        try {
            //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            cookieManager.saveOrUpdateRoundNumberCookie(roundNumber.toString(), chosenSchedule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.addCookie(new Cookie("roundNumber", roundNumber.toString()));
        //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
        //   this.numerRundy=numerRundy;
        attributes.addAttribute("chosenSchedule", Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("chosenSchedule")).findAny().orElse(null).getValue());
        attributes.addAttribute("roundNumber", roundNumber.toString());

        return "redirect:/admin/LK/schedule/schedule";

    }

    @GetMapping(value = "/admin/LK/schedule/schedule/changeSchedule")
    public String changeSchedule(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "chosenSchedule", required = false) String chosenSchedule) {

        try {
            //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            cookieManager.saveOrUpdateRoundNumberCookie("1", chosenSchedule);
            cookieManager.saveOrUpdateChosenScheduleCookie(chosenSchedule);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // response.addCookie(new Cookie("numerRundy","1"));
        // response.addCookie(new Cookie("wybranyTerminarz",wybranyTerminarz));
        //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
        //   this.numerRundy=numerRundy;
        attributes.addAttribute("chosenSchedule", chosenSchedule);
        attributes.addAttribute("roundNumber", "1");

        return "redirect:/admin/LK/schedule/schedule";
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
