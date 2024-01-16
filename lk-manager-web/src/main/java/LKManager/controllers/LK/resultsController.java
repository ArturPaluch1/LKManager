package LKManager.controllers.LK;

import LKManager.DAO.MatchDAOImpl;
import LKManager.DAO.RoundDAOImpl;
import LKManager.DAO.ScheduleDAO;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.services.Cache.MZCache;
import LKManager.services.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Controller

@RequiredArgsConstructor

public class resultsController {
    private final LKManager.services.MZUserService MZUserService;
    private final MatchService matchService;
    private final ScheduleService scheduleService;
    private  final RoundDAOImpl rundaDAO;
    private  final MatchDAOImpl meczDAO;

    // private Integer numerRundy;
  //  private Terminarz terminarz;
    private Schedule schedule;
    private final PlikiService plikiService;
    private String poprzednioWybranyTerminarz;
    private  final ResultsService resultsService;

private final MZCache mzCache;
    private final CookieManager cookieManager;

  //  private List<Terminarz> terminarze;
  private File[] terminarzeFiles;
  private List<Schedule>schedules;


    @Autowired
    private SessionFactory sessionFactory;
    private final ScheduleDAO scheduleDAO;
   /* public resultsController(MZUserService MZUserService, LKManager.services.MZUserService mzUserService, MatchService matchService, URLs urLs, ScheduleService scheduleService, RoundDAOImpl rundaDAO, MatchDAOImpl meczDAO, PlikiService plikiService, ResultsService resultsService, CookieManager cookieManager, ScheduleDAO scheduleDAO) {
        this.MZUserService = mzUserService;
        this.rundaDAO = rundaDAO;
        this.meczDAO = meczDAO;
        this.cookieManager = cookieManager;
        this.scheduleDAO = scheduleDAO;
        MZUserService = mzUserService;
        this.matchService = matchService;
        this.scheduleService = scheduleService;
        this.plikiService = plikiService;
        this.resultsService = resultsService;
    }
*/
    /*          todo cookies info linki
    https://dzone.com/articles/how-to-use-cookies-in-spring-boot
https://teamtreehouse.com/community/if-the-username-contains-a-whitespace-character-such-as-i-get-a-500-internal-error-is-there-any-way-to-fix-this
    */
    public File[] listFilesForFolder (File folder){
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }


        return folder.listFiles();
    }
/*
    @PostMapping("/wybranyTerminarz")
    public String wyswietlTerminarz(Model model, @RequestParam (value="wybranyTerminarz", required = true)String wybranyTerminarz)
    {
        System.out.println("Initialized...");
        model.addAttribute("wybranyTerminarz",wybranyTerminarz);
        return "redirect:/terminarz";
    }
*/

    /*
    public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }
*/
    //, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz, @RequestParam (value="numerRundy", required = false)String nrRundy

    @GetMapping({"/results"} )
   // public String index(HttpServletResponse response, Model model, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz, @RequestParam (value="numerRundy", required = false)String nrRundy) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
        public String getResults(HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam (value="chosenSchedule", required = false)String chosenSchedule, @RequestParam (value="roundNumber", required = false)String roundNumber) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
//,@CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie,@CookieValue(value = "numerRundy", defaultValue = "1") String numerRundyCookie,
//decode cookie bo cookie nie mają spacji i trzeba zakodować

    //    Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
   //     Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter(a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);

   //     cookieManager.decodeCookie(wybranyTerminarzCookie.getValue());


  /*     String numerRundy=    cookieManager.saveOrUpdateNumerRundyCookie(response,nrRundy,request);
        wybranyTerminarz=  cookieManager.saveOrUpdateChosenScheduleCookie(response,wybranyTerminarz,request, terminarzDAO);
 */
        //todo wyzej bkp
 //CookieManager.checkCookies(response,request,nrRundy,wybranyTerminarz,terminarzDAO);
        roundNumber= CookieManager.saveOrUpdateRoundNumberCookie(Optional.ofNullable(roundNumber), Optional.ofNullable(chosenSchedule),response,request, scheduleDAO);
        chosenSchedule= CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(chosenSchedule),response,request, scheduleDAO);




     //   cookieManagement(response,numerRundyCookie,wybranyTerminarzCookie, wybranyTerminarz, nrRundy);




/////////////////////////////////////////////

       // wybranyTerminarzCookie=  URLDecoder.decode(wybranyTerminarzCookie, "utf-8");



        /*************************
        schedules= scheduleDAO.findAll();
        schedule = scheduleDAO.findByScheduleName(chosenSchedule);
***************************************/


        schedules= mzCache.getSchedulesFromCacheOrDatabase();
        schedule = mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(chosenSchedule);









  /*      if(wybranyTerminarz==""&&wybranyTerminarzCookie!="")wybranyTerminarz=wybranyTerminarzCookie;


        if(wybranyTerminarz=="")
        {
            return "redirect:/temp";
        }
*/
////ok
    /*
        model.addAttribute("numerRundy", nrRundy);
        //   model.addAttribute("terminarz", terminarz1);
        model.addAttribute("mecze", terminarz1.getRundy().get(Integer.parseInt(nrRundy)-1).getMecze());
        model.addAttribute("terminarze", terminarze1);
        model.addAttribute("wybranyTerminarz", wybranyTerminarz);
        model.addAttribute("nrRundy", terminarz1.getRundy());
        model.addAttribute("runda", terminarz1.getRundy().get(Integer.parseInt(nrRundy)-1));

        int u=0;
        //////////////////////////////////////////////////////////

        if(terminarz1.getRundy().size()==0)
        {
            terminarz1  =terminarzDAO.findAll().get(0);
        }

        if(nrRundy==null)
        {
            nrRundy="1";
        }

        //folder z terminarzami
        terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);



        terminarz= terminarzService.wczytajTerminarz(wybranyTerminarz);


        ///ok
*/


        Round  round=rundaDAO.findByScheduleIdAndRoundId(schedule.getId(),Integer.parseInt(roundNumber)-1);
        List<Round> roundNumbers = rundaDAO.findAllByScheduleId(schedule.getId());
        List<Match> matches=meczDAO.findAllByScheduleIdAndRoundId(schedule.getId(),Integer.parseInt(roundNumber)-1);


        model.addAttribute("roundNumber", roundNumber);
        //   model.addAttribute("terminarz", terminarz1);



        model.addAttribute("matches", matches);
        model.addAttribute("schedules", schedules);
        model.addAttribute("chosenSchedule", chosenSchedule);
        model.addAttribute("roundNumbers", roundNumbers);
        model.addAttribute("round", round);
       /* model.addAttribute("mecze", terminarz1.getRundy().get(Integer.parseInt(nrRundy)-1).getMecze());
        model.addAttribute("terminarze", terminarze1);
        model.addAttribute("wybranyTerminarz", wybranyTerminarz);
        model.addAttribute("nrRundy", terminarz1.getRundy());
        model.addAttribute("runda", terminarz1.getRundy().get(Integer.parseInt(nrRundy)-1));*/



/*
//terminarzDAO.findAll();
        //  Terminarz     terminarz = new Terminarz();

        if(wybranyTerminarz!=null)
        {
            //sprawdzanie czy jest taki terminarz

            terminarz = terminarzService.wczytajTerminarz(wybranyTerminarz);
     //   terminarzDAO.findByTerminarzName(wybranyTerminarz);
            if(terminarz==null)
            {
//todo przekierowanie że nie ma takiego terminarza
                return "redirect:/temp";
            }



            //nastapila zmiana terminarza ->nr rundy=1
            if(wybranyTerminarz.equals(this.poprzednioWybranyTerminarz)||this.poprzednioWybranyTerminarz==null)
            {


            //    terminarzDAO.findByTerminarzName(wybranyTerminarz);
                model.addAttribute("wybranyTerminarz", wybranyTerminarz);
                model.addAttribute("numerRundy", nrRundy);

                model.addAttribute("runda", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1));


                model.addAttribute("runda", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1));


                model.addAttribute("mecze", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1).getMecze());
   //             model.addAttribute("mecze", terminarz1.getRundy().get(Integer.parseInt(nrRundy)-1).getMecze());

            }
            else
            {
                //wybrany terminarz
                model.addAttribute("numerRundy", 0);
                this.poprzednioWybranyTerminarz =wybranyTerminarz;
                terminarz= //terminarzService.wczytajTerminarz(wybranyTerminarz);
                        terminarzDAO.findByTerminarzName(wybranyTerminarz);
                model.addAttribute("wybranyTerminarz", wybranyTerminarz);
                model.addAttribute("runda", terminarz.getRundy().get(0));

                model.addAttribute("mecze",terminarz.getRundy().get(0).getMecze() );
            }



        }
        if(wybranyTerminarz==null) {


            // nie ma terminarzy -> przekierowanie do tworzenia
            if(terminarze.length==0)
            {
                return "redirect:/dodajTerminarz";
            }
            else {
                //wybieranie najnowszego moyfikowanego
              var najbardziejNiedawnoZmodyfikowanyTerminarz=       Arrays.stream(terminarze).toList().stream().max(Comparator.comparing(File::lastModified));
                terminarz= terminarzService.wczytajTerminarz(najbardziejNiedawnoZmodyfikowanyTerminarz.get().getName());

          //todo najbardziejNiedawnoZmodyfikowanyTerminarz
              //  terminarz=terminarze.get(0);
                //   numerRundy=1;

 //               this.poprzednioWybranyTerminarz =najbardziejNiedawnoZmodyfikowanyTerminarz.orElseThrow(NullPointerException::new).getName();
                model.addAttribute("wybranyTerminarz", terminarz.getName());
                model.addAttribute("numerRundy", 0);
                model.addAttribute("runda", terminarz.getRundy().get(0));


                model.addAttribute("mecze",terminarz.getRundy().get(0).getMecze());
        var mecze=        terminarz.getRundy().get(0).getMecze();
       var mecz= mecze.get(0);
        mecz.getUser().getUsername();
                model.addAttribute("mecze1",terminarz1.getRundy().get(0).getMecze() );
                var mecze1=        terminarz1.getRundy().get(0).getMecze();
                var mecz1= mecze.get(0);
                mecz1.getUser().getUsername();
          int t=9;
            }

        }




        //  TeamTM teamy = new TeamTM(MZUserService);

        //   teamy.zapiszUPSGDoXML();
        //    var grajki=    teamy.wczytajUPSGZXML();
        //  =  teamy.LoadCalyUPSG();

        // terminarz=  wynikiService.wyswietlWyniki(numerRundy);



//        if(numerRundy== null)
 //       {
   //         numerRundy=1;
  //      }
        model.addAttribute("terminarz", terminarz);
        model.addAttribute("terminarze", terminarze);

        model.addAttribute("nrRundy", terminarz.getRundy());



*/

        return "LK/results";
    }

    @GetMapping({"/results/changeSchedule"})
    public String changeSchedule(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response,@RequestParam (value="chosenSchedule", required = false)String chosenSchedule)
    {
        try{
            //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            CookieManager.saveOrUpdateRoundNumberCookie(Optional.of("1"), Optional.ofNullable(chosenSchedule),response,request, scheduleDAO);
            CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(chosenSchedule),response,request, scheduleDAO);       }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // response.addCookie(new Cookie("numerRundy","1"));
        // response.addCookie(new Cookie("wybranyTerminarz",wybranyTerminarz));
        //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
        //   this.numerRundy=numerRundy;
        attributes.addAttribute("chosenSchedule", chosenSchedule);
        attributes.addAttribute("roundNumber", "1");

        return "redirect:/results";
    }
/*
    private void cookieManagement(HttpServletResponse response,String roundNumberCookie, String chosenScheduleCookie, String chosenSchedule ,String roundNumber) {



   //     wybranyTerminarz=null;

      Cookie cookieRoundNumber=null;
      //został podany nr rundy
        if(roundNumber !=null) {
        //sprawdzanie czy różny, żeby niepotrzebnie nie nadpisywać
            if(roundNumber!=roundNumberCookie)
            {
                cookieRoundNumber = new Cookie("numerRundy", roundNumber);
                cookieRoundNumber.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                cookieRoundNumber.setSecure(true);
                cookieRoundNumber.setHttpOnly(true);
                response.addCookie(cookieRoundNumber);
            }

        }


        Cookie cookieTerminarz=null;
        //nie podano terminara
        if(chosenSchedule ==null) {
            try{
//nie ma cookie terminarza
//tworzenie cookie wg najnowszego terminarza (wg id)
                if(chosenScheduleCookie.equals("null"))
                {
                    Session s = sessionFactory.openSession();
                    s.beginTransaction();


                    String hql =   " from Terminarz t where t.id=" +
                            "(select max(t.id) from Terminarz t)";

                    Query query = s.createQuery(hql);
                    Schedule schedule2 = (Schedule) query.getSingleResult();
                    s.getTransaction().commit();
s.close();
                    cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode(schedule2.getName(),"utf-8") );
                    cookieTerminarz.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                    cookieTerminarz.setSecure(true);
                    cookieTerminarz.setHttpOnly(true);
                }
                else
                {
                    cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode(chosenScheduleCookie, StandardCharsets.UTF_8) );
              //      cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode("null","utf-8") );
                    cookieTerminarz.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                    cookieTerminarz.setSecure(true);
                    cookieTerminarz.setHttpOnly(true);
                }

                //add cookie to response

                response.addCookie(cookieTerminarz);
                }

            catch (Exception e)
            {

        int te=1;
            }

        }
        //został podany terminarz
        else
        {

            cookieTerminarz = new Cookie("wybranyTerminarz", chosenSchedule);


            cookieTerminarz.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            cookieTerminarz.setSecure(true);
            cookieTerminarz.setHttpOnly(true);
            //add cookie to response

            response.addCookie(cookieTerminarz);
        }



    }
*/
    @PostMapping({"/editResults"} )
    public String editResults(Model model,
                               RedirectAttributes redirectAttributes,
                               @RequestParam(value = "chosenSchedule", required=true)String chosenSchedule,
                               //NUMER RUNDY NIE INDEX RUNDY czyli -1
                               @RequestParam(value = "roundNumber", required=true)Integer roundNumber,
                               @RequestParam(value = "matchId", required=true)List<Long> matchesId,
                               @RequestParam(value = "matches", required=false ) List<Match> matches,
                               @RequestParam(value = "user", required=true ) List<String> user,
                               @RequestParam(value = "opponentUser", required=true ) List<String> opponentUser,
                               @RequestParam(value = "UserMatchResult1", required=false ) List<String> userMatchResults1,
                               @RequestParam(value = "OpponentMatchResult1", required=false ) List<String> opponentMatchResults1,
                               @RequestParam(value = "UserMatchResult2", required=false ) List<String> userMatchResults2,
                               @RequestParam(value = "OpponentMatchResult2", required=false ) List<String> opponentMatchResults2,
                               @RequestParam(value = "bob", required=false ) List<String> bob) throws JAXBException {


        roundNumber--;
        if(roundNumber==-1)
        {
            roundNumber=0;
        }
        meczDAO.updateMatchesResults(matchesId,userMatchResults1,userMatchResults2,opponentMatchResults1,opponentMatchResults2);

        //folder z terminarzami
       // terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);



        // zmiana nazwy numeru rundy na indeks


    /*    var        terminarz= //terminarzService.wczytajTerminarz(wybranyTerminarz);
terminarzDAO.findByTerminarzName(wybranyTerminarz);


        for (int i = 0; i < terminarz.getRundy().get(numerRundy).getMecze().size(); i++) {

            terminarz.getRundy().get(numerRundy).getMecze().get(i).setUserMatchResult1(
                    UserMatchResult1.get(i) );
            terminarz.getRundy().get(numerRundy).getMecze().get(i).setOpponentMatchResult1(
                    OpponentMatchResult1.get(i) );
            terminarz.getRundy().get(numerRundy).getMecze().get(i).setUserMatchResult2(
                    UserMatchResult2.get(i) );
            terminarz.getRundy().get(numerRundy).getMecze().get(i).setOpponentMatchResult2(
                    OpponentMatchResult2.get(i) );




        }


        terminarzService.aktualizujTerminarz(terminarz,wybranyTerminarz);
*/
        redirectAttributes.addAttribute("roundNumber", roundNumber+1);
        redirectAttributes.addAttribute("chosenSchedule", chosenSchedule);

        return "redirect:/results";
    }



    @GetMapping ("/getRoundResults")
    public String getRoundResults(RedirectAttributes redirectAttributes, @RequestParam(value = "round", required = true)Integer roundNumber, @RequestParam(value = "chosenSchedule", required = false)String chosenSchedule)
    {
        int oo=9;
        //     this.numerRundy=numerRundy;
        //  this.wybranyTerminarz=wybranyTerminarz;



        redirectAttributes.addAttribute("roundNumber", roundNumber);
        redirectAttributes.addAttribute("chosenSchedule", chosenSchedule);
        return "redirect:/results";
//return "redirect:/LKManager.LK/terminarz";
    }




    @PostMapping ("/update")
    public String aktualizujWyniki(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "roundNumber",required = false)Integer roundNumber,
            @RequestParam(value = "chosenSchedule", required = true)String chosenSchedule

    ) throws JAXBException, DatatypeConfigurationException, ParserConfigurationException, IOException, SAXException {
        if(roundNumber== null||roundNumber==0)
        {
            roundNumber=1;
        }

        Round round = rundaDAO.findRoundWitchMatches(chosenSchedule,roundNumber);

        if(round != null)
        {
            // terminarz= terminarzService.wczytajTerminarz("terminarz.xml");
        }
        else
        {
            //    terminarz=terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
     //       terminarz=terminarzService.wczytajTerminarz(wybranyTerminarz);
          //  terminarz=terminarzDAO.findByTerminarzName(wybranyTerminarz);


                //todo zablokowac robienie 2 z ta samą nazwą?



                //todo nie ma takiej rundy
                return "redirect:/dodajTerminarz";


        }

// runda -1 bo rundy od 1 a indeksy w liscie od 0
  //      if(terminarz.getRundy().get(numerRundy-1).getStatus()== Runda.status.rozegrana)
        if(round.isPlayed())
        {
            // TODO: 2022-11-23   zamienic na zapytanie, że czy na pewno chcesz zmienić rozegrana runde
            //rundy.get(numerRundy-1).setPlayed(true);
            resultsService.updateResults(roundNumber, round,matchService, chosenSchedule);
            redirectAttributes.addAttribute("numerRundy", roundNumber);
            redirectAttributes.addAttribute("chosenSchedule",chosenSchedule );


            return "redirect:/results";
        }
        else
        {
            round.setPlayed(true);
            resultsService.updateResults(roundNumber, round,matchService, chosenSchedule);
            redirectAttributes.addAttribute("roundNumber", roundNumber);
            redirectAttributes.addAttribute("chosenSchedule",chosenSchedule );
            return "redirect:/results";
        }




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
