package LKManager.controllers.LK;

import LKManager.DAO.MeczDAOImpl;
import LKManager.DAO.RundaDAOImpl;
import LKManager.DAO.TerminarzDAOImpl;
import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.services.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class wynikiController {
    private final LKManager.services.MZUserService MZUserService;
    private final MatchService matchService;
    private final TerminarzService terminarzService;
    private  final RundaDAOImpl rundaDAO;
    private  final MeczDAOImpl meczDAO;

    // private Integer numerRundy;
  //  private Terminarz terminarz;
    private Terminarz terminarz1;
    private final PlikiService plikiService;
    private String poprzednioWybranyTerminarz;
    private  final WynikiService wynikiService;


    private final CookieManager cookieManager;

  //  private List<Terminarz> terminarze;
  private File[] terminarze;
  private List<Terminarz>terminarze1;

    @Autowired
    private SessionFactory sessionFactory;
    private final TerminarzDAOImpl terminarzDAO;
    public wynikiController(MZUserService MZUserService, LKManager.services.MZUserService mzUserService, MatchService matchService, URLs urLs, TerminarzService terminarzService, RundaDAOImpl rundaDAO, MeczDAOImpl meczDAO, PlikiService plikiService, WynikiService wynikiService, CookieManager cookieManager, TerminarzDAOImpl terminarzDAO) {
        this.MZUserService = mzUserService;
        this.rundaDAO = rundaDAO;
        this.meczDAO = meczDAO;
        this.cookieManager = cookieManager;
        this.terminarzDAO = terminarzDAO;
        MZUserService = mzUserService;
        this.matchService = matchService;
        this.terminarzService = terminarzService;
        this.plikiService = plikiService;
        this.wynikiService = wynikiService;
    }

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

    @PostMapping("/wybranyTerminarz")
    public String wyswietlTerminarz(Model model, @RequestParam (value="wybranyTerminarz", required = true)String wybranyTerminarz)
    {
        System.out.println("Initialized...");
        model.addAttribute("wybranyTerminarz",wybranyTerminarz);
        return "redirect:/terminarz";
    }


    /*
    public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }
*/
    //, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz, @RequestParam (value="numerRundy", required = false)String nrRundy

    @GetMapping({"/wyniki"} )
   // public String index(HttpServletResponse response, Model model, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz, @RequestParam (value="numerRundy", required = false)String nrRundy) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
        public String index(HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz, @RequestParam (value="numerRundy", required = false)String nrRundy) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
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
        nrRundy= CookieManager.saveOrUpdateNumerRundyCookie(nrRundy,wybranyTerminarz,response,request,terminarzDAO);
        wybranyTerminarz= CookieManager.saveOrUpdateChosenScheduleCookie(wybranyTerminarz,response,request,terminarzDAO);




     //   cookieManagement(response,numerRundyCookie,wybranyTerminarzCookie, wybranyTerminarz, nrRundy);




/////////////////////////////////////////////

       // wybranyTerminarzCookie=  URLDecoder.decode(wybranyTerminarzCookie, "utf-8");

        terminarze1=terminarzDAO.findAll();
        terminarz1= terminarzDAO.findByTerminarzName(wybranyTerminarz);

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


        var  runda=rundaDAO.findByTerminarzIdAndRundaId(terminarz1.getId(),Integer.parseInt(nrRundy)-1);
        var rundy = rundaDAO.findAllByTerminarzId(terminarz1.getId());
        List<Match> mecze=meczDAO.findAllByTerminarzIdAndRundaId(terminarz1.getId(),Integer.parseInt(nrRundy)-1);


        model.addAttribute("numerRundy", nrRundy);
        //   model.addAttribute("terminarz", terminarz1);



        model.addAttribute("mecze", mecze);
        model.addAttribute("terminarze", terminarze1);
        model.addAttribute("wybranyTerminarz", wybranyTerminarz);
        model.addAttribute("nrRundy", rundy);
        model.addAttribute("runda", runda);
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

        return "LK/wyniki";
    }

    @GetMapping({"/changeSchedule"})
    public String changeSchedule(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response,@RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz)
    {
        try{
            //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            CookieManager.saveOrUpdateNumerRundyCookie("1",wybranyTerminarz,response,request,terminarzDAO);
            CookieManager.saveOrUpdateChosenScheduleCookie(wybranyTerminarz,response,request,terminarzDAO);       }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // response.addCookie(new Cookie("numerRundy","1"));
        // response.addCookie(new Cookie("wybranyTerminarz",wybranyTerminarz));
        //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
        //   this.numerRundy=numerRundy;
        attributes.addAttribute("wybranyTerminarz", wybranyTerminarz);
        attributes.addAttribute("numerRundy", "1");

        return "redirect:/wyniki";
    }

    private void cookieManagement(HttpServletResponse response,String numerRundyCookie, String wybranyTerminarzCookie, String wybranyTerminarz ,String nrRundy) {



   //     wybranyTerminarz=null;

      Cookie cookieNumerRundy=null;
      //został podany nr rundy
        if(nrRundy !=null) {
        //sprawdzanie czy różny, żeby niepotrzebnie nie nadpisywać
            if(nrRundy!=numerRundyCookie)
            {
                cookieNumerRundy = new Cookie("numerRundy", nrRundy);
                cookieNumerRundy.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                cookieNumerRundy.setSecure(true);
                cookieNumerRundy.setHttpOnly(true);
                response.addCookie(cookieNumerRundy);
            }

        }


        Cookie cookieTerminarz=null;
        //nie podano terminara
        if(wybranyTerminarz ==null) {
            try{
//nie ma cookie terminarza
//tworzenie cookie wg najnowszego terminarza (wg id)
                if(wybranyTerminarzCookie.equals("null"))
                {
                    Session s = sessionFactory.openSession();
                    s.beginTransaction();


                    String hql =   " from Terminarz t where t.id=" +
                            "(select max(t.id) from Terminarz t)";

                    Query query = s.createQuery(hql);
                    Terminarz terminarz2= (Terminarz) query.getSingleResult();
                    s.getTransaction().commit();
s.close();
                    cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode(terminarz2.getName(),"utf-8") );
                    cookieTerminarz.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                    cookieTerminarz.setSecure(true);
                    cookieTerminarz.setHttpOnly(true);
                }
                else
                {
                    cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode(wybranyTerminarzCookie,"utf-8") );
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

            cookieTerminarz = new Cookie("wybranyTerminarz", wybranyTerminarz);


            cookieTerminarz.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            cookieTerminarz.setSecure(true);
            cookieTerminarz.setHttpOnly(true);
            //add cookie to response

            response.addCookie(cookieTerminarz);
        }



    }

    @PostMapping({"/edytujWyniki"} )
    public String edytujWyniki(Model model,
                               RedirectAttributes redirectAttributes,
                               @RequestParam(value = "wybranyTerminarz", required=true)String wybranyTerminarz,
                               //NUMER RUNDY NIE INDEX RUNDY czyli -1
                               @RequestParam(value = "numerRundy", required=true)Integer numerRundy,
                               @RequestParam(value = "matchId", required=true)List<Long> matchesId,
                               @RequestParam(value = "mecze", required=false ) List<Match> mecze,
                               @RequestParam(value = "user", required=true ) List<String> user,
                               @RequestParam(value = "opponentUser", required=true ) List<String> opponentUser,
                               @RequestParam(value = "UserMatchResult1", required=false ) List<String> userMatchResults1,
                               @RequestParam(value = "OpponentMatchResult1", required=false ) List<String> opponentMatchResults1,
                               @RequestParam(value = "UserMatchResult2", required=false ) List<String> userMatchResults2,
                               @RequestParam(value = "OpponentMatchResult2", required=false ) List<String> opponentMatchResults2,
                               @RequestParam(value = "bob", required=false ) List<String> bob) throws JAXBException {


        numerRundy--;
        if(numerRundy==-1)
        {
            numerRundy=0;
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
        redirectAttributes.addAttribute("numerRundy", numerRundy+1);
        redirectAttributes.addAttribute("wybranyTerminarz", wybranyTerminarz);

        return "redirect:/wyniki";
    }



    @PostMapping("/pokazRundeWyniki")
    public String pokazRunde(RedirectAttributes redirectAttributes, @RequestParam(value = "runda", required = true)Integer numerRundy, @RequestParam(value = "wybranyTerminarz", required = false)String wybranyTerminarz)
    {
        int oo=9;
        //     this.numerRundy=numerRundy;
        //  this.wybranyTerminarz=wybranyTerminarz;



        redirectAttributes.addAttribute("numerRundy", numerRundy);
        redirectAttributes.addAttribute("wybranyTerminarz", wybranyTerminarz);
        return "redirect:/wyniki";
//return "redirect:/LKManager.LK/terminarz";
    }




    @PostMapping ("/aktualizuj")
    public String aktualizujWyniki(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "numerRundy",required = false)Integer numerRundy,
            @RequestParam(value = "wybranyTerminarz", required = true)String wybranyTerminarz

    ) throws JAXBException, DatatypeConfigurationException, ParserConfigurationException, IOException, SAXException {
        if(numerRundy== null||numerRundy==0)
        {
            numerRundy=1;
        }

        Runda runda= rundaDAO.findRoundWitchMatches(wybranyTerminarz,numerRundy);

        if(runda!= null)
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
        if(runda.isPlayed()== true)
        {
            // TODO: 2022-11-23   zamienic na zapytanie, że czy na pewno chcesz zmienić rozegrana runde
            //rundy.get(numerRundy-1).setPlayed(true);
            wynikiService.aktualizujWyniki(numerRundy,runda,matchService, wybranyTerminarz);
            redirectAttributes.addAttribute("numerRundy", numerRundy);
            redirectAttributes.addAttribute("wybranyTerminarz",wybranyTerminarz );


            return "redirect:/wyniki";
        }
        else
        {
            runda.setPlayed(true);
            wynikiService.aktualizujWyniki(numerRundy,runda,matchService, wybranyTerminarz);
            redirectAttributes.addAttribute("numerRundy", numerRundy);
            redirectAttributes.addAttribute("wybranyTerminarz",wybranyTerminarz );
            return "redirect:/wyniki";
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
