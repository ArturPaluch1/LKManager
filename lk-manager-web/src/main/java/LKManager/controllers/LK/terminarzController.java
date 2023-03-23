package LKManager.controllers.LK;

import LKManager.LK.Terminarz;
import LKManager.services.*;
import LKManager.model.UserMZ.UserData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.parser.ParseException;
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
    private final MZUserService MZUserService;


    private Integer numerRundy;
private final LKUserService lkUserService;
    public Integer getNumerRundy() {
        return numerRundy;
    }

    public void setNumerRundy(Integer numerRundy) {
        this.numerRundy = numerRundy;
    }
private final TerminarzService terminarzService;
    private final PlikiService plikiService;
    private String wybranyTerminarz;
    private  Terminarz terminarz;

    public terminarzController(MZUserService MZUserService,  LKUserService lkUserService, TerminarzService terminarzService, PlikiService plikiService) {
        this.MZUserService = MZUserService;

        this.lkUserService = lkUserService;

        this.terminarzService = terminarzService;
        this.plikiService = plikiService;
    }


    @GetMapping("/terminarz")
    public String index(Model model, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, ParseException, URISyntaxException {



        var terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);

        if(wybranyTerminarz!=null)
        {
            //nastapila zmiana terminarza ->nr rundy=1
            if(wybranyTerminarz!=this.wybranyTerminarz)
            {
                this.wybranyTerminarz=wybranyTerminarz;
                numerRundy=1;
            }



        }



         //nie przekazano terminarza
         if(this.wybranyTerminarz== null)
         {
             // nie ma terminarzy -> przekierowanie do tworzenia
             if(terminarze.length==0)
             {
return "redirect:/dodajTerminarz";
             }
             else {
           //wybieranie najnowszego moyfikowanego
          var najbardziejNiedawnoZmodyfikowanyTerminarz=       Arrays.stream(terminarze).toList().stream().max(Comparator.comparing(File::lastModified));
                      terminarz= terminarzService.wczytajTerminarz(najbardziejNiedawnoZmodyfikowanyTerminarz.get().getName());
                 model.addAttribute("wybranyTerminarz", najbardziejNiedawnoZmodyfikowanyTerminarz.get().getName());
             }
         }
         //wskazano terminarz
         else
         {
             //wybrany terminarz
                  terminarz= terminarzService.wczytajTerminarz(this.wybranyTerminarz);
             model.addAttribute("wybranyTerminarz", this.wybranyTerminarz);
         }






        model.addAttribute("nrRundy", terminarz.getTerminarz());
if(numerRundy== null)
{
    numerRundy=1;
}




        model.addAttribute("runda", terminarz.getTerminarz().get(numerRundy-1));
        model.addAttribute("mecze", terminarz.getTerminarz().get(numerRundy-1).getMecze()
        );
model.addAttribute("numerRundy", numerRundy);






        model.addAttribute("terminarze", terminarze);



        return "LK/terminarz/terminarz";
    }





    @RequestMapping("/dodajTerminarz")
public String dodajTerminarz(Model model) throws JAXBException, IOException, ParserConfigurationException, SAXException
{

  var gracze  =lkUserService.wczytajGraczyZXML();
List<graczOpakowanie>graczeOpakowani = new ArrayList<>();

//to jest niepotrzebne//////////////////////// todo
    for (var gracz: gracze

         ) {
        graczeOpakowani.add(new graczOpakowanie(false,gracz));
    }




        var terminarzCommand = new TerminarzCommand();

   model.addAttribute("gracze",gracze);
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

        var terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);

        model.addAttribute("terminarze", terminarze);
           return "LK/terminarz/usunTerminarz";


    }

    @PostMapping("/usunTerminarz")
    public String usunTerminarz(@RequestParam (value = "wybranyTerminarz", required = true)String terminarzDoUsuniecia)
    {
        try
        {
            File plikDoUsuniecia= new File("Data/terminarze/"+terminarzDoUsuniecia);
            plikDoUsuniecia.delete();
            return "redirect:/terminarz";
        }
        catch(Exception e)
        {
            return "LK/temp";
        }

    }

    @PostMapping("/terminarz")
    public String stworzTerminarz( @ModelAttribute TerminarzCommand command, @RequestParam(value = "wybraniGracze" , required = false) List<String> wybraniGracze) throws DatatypeConfigurationException {

        XMLGregorianCalendar data = DatatypeFactory.newInstance().newXMLGregorianCalendar();

data.setYear(Integer.parseInt(command.data.trim().split("-")[0]));
data.setMonth(Integer.parseInt(command.data.split("-")[1]));
data.setDay(Integer.parseInt(command.data.split("-")[2]));
var gracze=lkUserService.wczytajGraczyZXML();
List<UserData> templist = new ArrayList<>();
        for (var gracz:gracze
             ) {
            for ( int i=0;i<wybraniGracze.size();i++
                 ) {
                if(gracz.getUsername().equals(wybraniGracze.get(i)))
                {
                    templist.add(gracz);
                    i=0;
                    break;
                }
            }
        }


        terminarzService.utworzTerminarz(data, templist, command.nazwa);

return "redirect:/terminarz";
    }

    @PostMapping("/pokazRunde")
    public String pokazRunde(@RequestParam(value = "participant", required = true)Integer numerRundy)
    {
int oo=9;
     this.numerRundy=numerRundy;
        return "redirect:/terminarz";

    }



    @Getter
    @Setter
    @NoArgsConstructor
public class TerminarzCommand
{

 private String data;
 private String nazwa;


    public void setListaGraczy(){

    }


    }






@Getter
@Setter
@NoArgsConstructor
  public  class graczOpakowanie
    {
        //todo to jest niepotrzebne
       private boolean zaznaczony;
        private String gracz;


        public graczOpakowanie(boolean zaznaczony, UserData gracz) {
            this.zaznaczony = zaznaczony;
            this.gracz = gracz.getUsername();

        }
    }





}
