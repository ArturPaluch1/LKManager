package LKManager.controllers.LK;

import LKManager.DAO.MeczDAOImpl;
import LKManager.DAO.RundaDAOImpl;
import LKManager.DAO.TerminarzDAOImpl;
import LKManager.DAO.UserDAOImpl;
import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.services.*;
import LKManager.model.UserMZ.UserData;

import LKManager.services.Cache.MZCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import javax.xml.datatype.DatatypeFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
//@Validated
//@RequestMapping({"terminarz.html", "/terminarz", "terminarz"})
public class terminarzController {
    private final MZUserService MZUserService;


   // private Integer numerRundy;
private final LKUserService lkUserService;
  /*  public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }*/
    @Autowired
    private MZCache mzCache;
private final TerminarzService terminarzService;
    private final PlikiService plikiService;
 //   private String wybranyTerminarz;

    private final TerminarzDAOImpl terminarzDAO;
    private final UserDAOImpl userDAO;
    private  final RundaDAOImpl rundaDAO;
    private  final MeczDAOImpl meczDAO;
   private final CookieManager cookieManager;
    public terminarzController(MZUserService MZUserService, LKUserService lkUserService, TerminarzService terminarzService, PlikiService plikiService, TerminarzDAOImpl terminarzDAO, UserDAOImpl userDAO, RundaDAOImpl rundaDAO, MeczDAOImpl meczDAO, CookieManager cookieManager) {
        this.MZUserService = MZUserService;

        this.lkUserService = lkUserService;

        this.terminarzService = terminarzService;
        this.plikiService = plikiService;
        this.terminarzDAO = terminarzDAO;
        this.userDAO = userDAO;
        this.rundaDAO = rundaDAO;
        this.meczDAO = meczDAO;
        this.cookieManager = cookieManager;
    }


    @GetMapping("/terminarz")
    public String index(HttpServletResponse response,HttpServletRequest request, Model model, @RequestParam (value="numerRundy", required = false)String nrRundy,@RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, ParseException, URISyntaxException {
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

 nrRundy= CookieManager.saveOrUpdateNumerRundyCookie(nrRundy,wybranyTerminarz,response,request,terminarzDAO);
wybranyTerminarz= CookieManager.saveOrUpdateChosenScheduleCookie(wybranyTerminarz,response,request,terminarzDAO);
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





        var terminarze =mzCache.getSchedulesFromCacheOrDatabase();

        //   terminarz= terminarzDAO.findByTerminarzId(106);
     //   var terminarz11=     terminarzDAO.findByTerminarzName("ja i kyo");

        //podano nazwe terminarza


        Terminarz terminarz= null;
        if(wybranyTerminarz!=null)
        {




            //sprawdzanie czy jest taki terminarz

           //   terminarz = terminarzService.wczytajTerminarz(wybranyTerminarz);





 //            terminarz=     terminarzDAO.findByTerminarzName(wybranyTerminarz);


terminarz=mzCache.findChosenScheduleByScheduleNameFromCacheOrDatabase(wybranyTerminarz);



             //nie znaleziono przekierowanie do tworzenia
         if(terminarz==null)
         {
//todo przekierowanie że nie ma takiego terminarza
              return "redirect:/temp";
          }





        }
        //nie podano nazwy terminarza
        else
        {
//ostatni wg id
          //  terminarz=     terminarzDAO.findLastById();
            mzCache.findLastScheduleByIdFromCacheOrDatabase();
            //nie ma żadnych terminarzy
            if(terminarz==null)
            {
                return "redirect:/dodajTerminarz";
            }
            else  //był taki terminarz
            {

            }
        }

      var  runda=rundaDAO.findByTerminarzIdAndRundaId(terminarz.getId(),Integer.parseInt(nrRundy)-1);
        var rundy = rundaDAO.findAllByTerminarzId(terminarz.getId());
        List<Match> mecze=meczDAO.findAllByTerminarzIdAndRundaId(terminarz.getId(),Integer.parseInt(nrRundy)-1);
       // model.addAttribute("wybranyTerminarz", terminarze.get(0));


        model.addAttribute("wybranyTerminarz", wybranyTerminarz);
        model.addAttribute("rundy",rundy);
     //   model.addAttribute("nrRundy",terminarz.getRundy());
     //   model.addAttribute("runda", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1));
        model.addAttribute("runda", runda);
     //   model.addAttribute("mecze", terminarz.getRundy().get(Integer.parseInt(nrRundy)-1).getMecze()  );
        model.addAttribute("mecze", mecze  );

        model.addAttribute("numerRundy", nrRundy);






        model.addAttribute("terminarze", terminarze);



        return "LK/terminarz/terminarz";
    }





    @GetMapping(value = "/dodajTerminarz" )
public String dodajTerminarz(Model model) throws JAXBException, IOException, ParserConfigurationException, SAXException
{

 //lkUserService.wczytajGraczyZXML();
    List<UserData> gracze = userDAO.findAll(false);

    gracze= gracze.stream().sorted(
          (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
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

   model.addAttribute("gracze",gracze);

    //  Bob b= new Bob();




    /*Map<String,String>wybraniGracze1 = new HashMap<>();
    wybraniGracze1.put("bob","sam");
    wybraniGracze1.put("bob1","sam1");

    terminarzCommand.mapa=wybraniGracze1;*/
terminarzCommand.lista= new ArrayList<String>();

    //terminarzCommand.bob=b;
    model.addAttribute("userListWrapper",terminarzCommand);

    model.addAttribute("terminarz",terminarzCommand);
        return "LK/terminarz/dodajTerminarz";
    }




    @GetMapping(value = "/terminarz/wybierz")
    public String wybierzTerminarz()
    {
        return "LK/terminarz/wybierzTerminarz";
    }



    @GetMapping("/usunTerminarz")
    public String usuwanieTerminarza(Model model)//@RequestParam (value = "wybranyTerminarz", required = true)String terminarzDoUsuniecia)
    {
        List<Terminarz> terminarze=mzCache.getSchedulesFromCacheOrDatabase();
      //plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);

//this.wybranyTerminarz=null;
        model.addAttribute("terminarze", terminarze);
           return "LK/terminarz/usunTerminarz";


    }

    @PostMapping("/usunTerminarz")
    public String usunTerminarz(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, @RequestParam (value = "wybranyTerminarz", required = true)String terminarzDoUsuniecia)//, @CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie)
    {



           try
        {/*
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
            terminarzDAO.deleteByName(terminarzDoUsuniecia);

            mzCache.setTerminarze(terminarzDAO.findAll());




            Terminarz terminarz1 =terminarzDAO.findLastById();
            try{

         //       Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
        //        Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter( a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);
/*
                cookieManager.saveOrUpdateChosenScheduleCookie(response,terminarz1.getName(),request, terminarzDAO);
                cookieManager.saveOrUpdateNumerRundyCookie(response,"1",request);
       */
                //todo wyzej bkp


               // CookieManager.checkCookies(response,request,"1",terminarz1.getName(),terminarzDAO);

            }
            catch (Exception e)
            {

            }
            CookieManager.saveOrUpdateNumerRundyCookie("",terminarz1.getName(),response,request,terminarzDAO);
CookieManager.saveOrUpdateChosenScheduleCookie(terminarz1.getName(),response,request,terminarzDAO);
            attributes.addAttribute("wybranyTerminarz", terminarz1.getName());
            attributes.addAttribute("numerRundy", "1");
            return "redirect:/terminarz";
        }
        catch(Exception e)
        {
         //   model.addAttribute("error", "błąd w usuwaniu");
            return "LK/temp";
        }

    }

    @PostMapping("/terminarz")
    public String stworzTerminarz(HttpServletResponse response,HttpServletRequest request,RedirectAttributes attributes, @ModelAttribute @Valid TerminarzCommand command, @RequestParam(value = "wybraniGracze" , required = false) List<String> wybraniGracze) throws DatatypeConfigurationException, JsonProcessingException {
//,@CookieValue(value = "wybranyTerminarz", defaultValue = "null") String wybranyTerminarzCookie,@CookieValue(value = "numerRundy", defaultValue = "1") String numerRundyCookie


        //todo walidacja  te same nazwy
/*var terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);
if(Arrays.stream(terminarze).anyMatch(a->a.getName().trim().equals(command.getNazwa().trim()+".xml")))
{

   int y=0;
}*/

        Terminarz terminarz = null;


   /*     XMLGregorianCalendar data = DatatypeFactory.newInstance().newXMLGregorianCalendar();

        data.setYear(Integer.parseInt(command.data.trim().split("-")[0]));
        data.setMonth(Integer.parseInt(command.data.split("-")[1]));
        data.setDay(Integer.parseInt(command.data.split("-")[2]));
*/
        LocalDate data=LocalDate.parse(command.getData().trim(), DateTimeFormatter.ofPattern("yyyy-MM-d"));
        //    LocalDateTime data = LocalDateTime.of(command.data.trim().split("-")[0],
        //           command.data.split("-")[1]))
////////////////////////////////////////


        List<UserData> gracze = userDAO.findAll(false);

        //nie wybrano graczy do wielodniowego terminarza
        if (wybraniGracze == null) {
            if (command.getLista().size() == 0) {
                //todo nie wybrano tez pojedynczych meczy
                int tt = 0;
            } else {
                if (command.getLista().size() % 2 != 0) {
                    //todo brakuje pary dla grajka  ewentuanie w js wybierzgrajka-> pauza
                } else {
                    List<UserData> templist = new ArrayList<>();
                    for (int i = 0; i < command.lista.size(); i++
                    ) {


                        for (int j = 0; j < gracze.size(); j++) {


                            if (gracze.get(j).getUsername().equals(command.lista.get(i))) {
                                templist.add(gracze.get(j));
                                j = 0;
                                break;
                            }
                        }
                    }
//////////////////////////////
                    terminarz = terminarzService.utworzTerminarzJednodniowy(data, templist, command.nazwa);

                }

            }
        } else {
            List<UserData> templist = new ArrayList<>();
            for (var gracz : gracze
            ) {
                for (int i = 0; i < wybraniGracze.size(); i++
                ) {
                    if (gracz.getUsername().equals(wybraniGracze.get(i))) {
                        templist.add(gracz);
                        i = 0;
                        break;
                    }
                }
            }
//////////////////////////////
            terminarz = terminarzService.utworzTerminarzWielodniowy(data, templist, command.nazwa);
        }

        try {
        //    Cookie numerRundyCookie= Arrays.stream(request.getCookies()).filter( a->a.getName().equals("numerRundy")).findFirst().orElse(null);
        terminarzDAO.save(terminarz);
        if(mzCache.getTerminarze().size()!=0)
            mzCache.getTerminarze().add(terminarz);
/*
 Cookie wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter( a->a.getName().equals("wybranyTerminarz")).findFirst().orElse(null);
            cookieManager.saveOrUpdateChosenScheduleCookie(response, terminarz.getName(), request, terminarzDAO);
       */
       //todo wyzej bkp
         //   CookieManager.checkCookies(response,request,"1",terminarz.getName(),terminarzDAO);
CookieManager.saveOrUpdateNumerRundyCookie("1",terminarz.getName(),response,request,terminarzDAO);
CookieManager.saveOrUpdateChosenScheduleCookie(terminarz.getName(),response,request,terminarzDAO);
            attributes.addAttribute("wybranyTerminarz", terminarz.getName());
            attributes.addAttribute("numerRundy", "1");
            return "redirect:/terminarz";
        } catch (Exception e) {
            return "redirect:/temp";
        }
    }



    @PostMapping("/pokazRunde")
    public String pokazRunde(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "participant", required = true)Integer numerRundy,@RequestParam (value="wybranyTerminarz", required = true)String wybranyTerminarz)
    {
//cookieManager.saveOrUpdateNumerRundyCookie(response, numerRundy.toString(),request);
        //todo wyzej bpk
        try{
       //     CookieManager.checkCookies(response,request,numerRundy.toString(),null,terminarzDAO);
            CookieManager.saveOrUpdateNumerRundyCookie(numerRundy.toString(),wybranyTerminarz,response,request,terminarzDAO);
        }
           catch (Exception e)
           {
               e.printStackTrace();
           }

        response.addCookie(new Cookie("numerRundy",numerRundy.toString()));
    //    response.addCookie(new Cookie("wybranyTerminarz","wielo"));
  //   this.numerRundy=numerRundy;
        attributes.addAttribute("wybranyTerminarz", Arrays.stream(request.getCookies()).filter(a->a.getName().equals("wybranyTerminarz")).findAny().orElse(null).getValue());
        attributes.addAttribute("numerRundy", numerRundy.toString());

        return "redirect:/terminarz";

    }

    @GetMapping(value = "/zmienTerminarz")
    public String zmienTerminarz(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response,@RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz)

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

        return "redirect:/terminarz";
    }

    @Getter
    @Setter
    @NoArgsConstructor
public class TerminarzCommand
{
/*todo walidacja
https://blog.mloza.pl/java-bean-validation-spring-boot-sprawdzanie-poprawnosci-danych-w-spring-boocie/

*/

    @NotNull
 private String data;
    @NotBlank
 private String nazwa;
 private List<String> lista;


    public void setListaGraczy(){

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
