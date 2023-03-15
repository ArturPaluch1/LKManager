package LKManager.controllers.LK;

import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.services.TeamTM;
import LKManager.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class wynikiController {
    private final LKManager.services.MZUserService MZUserService;
    private final MatchService matchService;
    private final TerminarzService terminarzService;
    // private Integer numerRundy;
    private Terminarz terminarz;
    private final PlikiService plikiService;
    private String poprzednioWybranyTerminarz;
    private  final WynikiService wynikiService;
    private File[] terminarze;


    public wynikiController(MZUserService MZUserService, LKManager.services.MZUserService mzUserService, MatchService matchService, URLs urLs, TerminarzService terminarzService, PlikiService plikiService, WynikiService wynikiService) {
        this.MZUserService = mzUserService;
        MZUserService = mzUserService;
        this.matchService = matchService;
        this.terminarzService = terminarzService;
        this.plikiService = plikiService;
        this.wynikiService = wynikiService;
    }

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
    public String index(Model model, @RequestParam (value="wybranyTerminarz", required = false)String wybranyTerminarz, @RequestParam (value="numerRundy", required = false)String nrRundy) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {
        if(nrRundy==null)
        {
            nrRundy="1";
        }


        //folder z terminarzami
        terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);

        //  Terminarz     terminarz = new Terminarz();

        if(wybranyTerminarz!=null)
        {
            //nastapila zmiana terminarza ->nr rundy=1
            if(wybranyTerminarz.equals(this.poprzednioWybranyTerminarz)||this.poprzednioWybranyTerminarz==null)
            {
                terminarz= terminarzService.wczytajTerminarz(wybranyTerminarz);
                model.addAttribute("wybranyTerminarz", wybranyTerminarz);
                model.addAttribute("numerRundy", nrRundy);

                model.addAttribute("runda", terminarz.getTerminarz().get(Integer.parseInt(nrRundy)-1));



                model.addAttribute("mecze", terminarz.getTerminarz().get(Integer.parseInt(nrRundy)-1).getMecze());
            }
            else
            {
                //wybrany terminarz
                model.addAttribute("numerRundy", 0);
                this.poprzednioWybranyTerminarz =wybranyTerminarz;
                terminarz= terminarzService.wczytajTerminarz(wybranyTerminarz);
                model.addAttribute("wybranyTerminarz", wybranyTerminarz);
                model.addAttribute("runda", terminarz.getTerminarz().get(0));

                model.addAttribute("mecze",terminarz.getTerminarz().get(0).getMecze() );
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
                //   numerRundy=1;

                this.poprzednioWybranyTerminarz =najbardziejNiedawnoZmodyfikowanyTerminarz.orElseThrow(NullPointerException::new).getName();
                model.addAttribute("wybranyTerminarz", najbardziejNiedawnoZmodyfikowanyTerminarz.get().getName());
                model.addAttribute("numerRundy", 0);
                model.addAttribute("runda", terminarz.getTerminarz().get(0));


                model.addAttribute("mecze",terminarz.getTerminarz().get(0).getMecze() );
            }

        }




        //  TeamTM teamy = new TeamTM(MZUserService);

        //   teamy.zapiszUPSGDoXML();
        //    var grajki=    teamy.wczytajUPSGZXML();
        //  =  teamy.LoadCalyUPSG();

        // terminarz=  wynikiService.wyswietlWyniki(numerRundy);


/*
        if(numerRundy== null)
        {
            numerRundy=1;
        }*/
        model.addAttribute("terminarz", terminarz);
        model.addAttribute("terminarze", terminarze);

        model.addAttribute("nrRundy", terminarz.getTerminarz());





        return "LK/wyniki";
    }

    @PostMapping({"/edytujWyniki"} )
    public String edytujWyniki(Model model,
                               RedirectAttributes redirectAttributes,
                               @RequestParam(value = "wybranyTerminarz", required=true)String wybranyTerminarz,
                               //NUMER RUNDY NIE INDEX RUNDY czyli -1
                               @RequestParam(value = "numerRundy", required=true)Integer numerRundy,
                               @RequestParam(value = "mecze", required=false ) List<Match> mecze,
                               @RequestParam(value = "user", required=true ) List<String> user,
                               @RequestParam(value = "opponentUser", required=true ) List<String> opponentUser,
                               @RequestParam(value = "UserMatchResult1", required=false ) List<String> UserMatchResult1,
                               @RequestParam(value = "OpponentMatchResult1", required=false ) List<String> OpponentMatchResult1,
                               @RequestParam(value = "UserMatchResult2", required=false ) List<String> UserMatchResult2,
                               @RequestParam(value = "OpponentMatchResult2", required=false ) List<String> OpponentMatchResult2,
                               @RequestParam(value = "bob", required=false ) List<String> bob) throws JAXBException {

        numerRundy--;
        if(numerRundy==-1)
        {
            numerRundy=1;
        }
        //folder z terminarzami
        terminarze= plikiService.pobierzPlikiZFolderu(PlikiService.folder.terminarze);
// zmiana nazwy numeru rundy na indeks


        var        terminarz= terminarzService.wczytajTerminarz(wybranyTerminarz);



        for (int i = 0; i < terminarz.getTerminarz().get(numerRundy).getMecze().size(); i++) {

            terminarz.getTerminarz().get(numerRundy).getMecze().get(i).setUserMatchResult1(
                    UserMatchResult1.get(i) );
            terminarz.getTerminarz().get(numerRundy).getMecze().get(i).setOpponentMatchResult1(
                    OpponentMatchResult1.get(i) );
            terminarz.getTerminarz().get(numerRundy).getMecze().get(i).setUserMatchResult2(
                    UserMatchResult2.get(i) );
            terminarz.getTerminarz().get(numerRundy).getMecze().get(i).setOpponentMatchResult2(
                    OpponentMatchResult2.get(i) );




        }


        terminarzService.aktualizujTerminarz(terminarz,wybranyTerminarz);

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
            @RequestParam(value = "numerRundy",required = true)Integer numerRundy,
            @RequestParam(value = "wybranyTerminarz", required = true)String wybranyTerminarz) throws JAXBException, DatatypeConfigurationException, ParserConfigurationException, IOException, SAXException {
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
            //    terminarz=terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
            terminarz=terminarzService.wczytajTerminarz(wybranyTerminarz);
            if(terminarz== null)
            {

                //todo
                return "redirect:/dodajTerminarz";
            }

        }

// runda -1 bo rundy od 1 a indeksy w liscie od 0
        if(terminarz.getTerminarz().get(numerRundy-1).getStatus()== Runda.status.rozegrana)
        {
            // TODO: 2022-11-23   zamienic na zapytanie, że czy na pewno chcesz zmienić rozegrana runde
            terminarz.getTerminarz().get(numerRundy-1).setStatus(Runda.status.rozegrana);
            wynikiService.aktualizujWyniki(numerRundy,terminarz,matchService, wybranyTerminarz);
            redirectAttributes.addAttribute("numerRundy", numerRundy);
            redirectAttributes.addAttribute("wybranyTerminarz",wybranyTerminarz );


            return "redirect:/wyniki";
        }
        else
        {
            terminarz.getTerminarz().get(numerRundy-1).setStatus(Runda.status.rozegrana);
            wynikiService.aktualizujWyniki(numerRundy,terminarz,matchService, wybranyTerminarz);
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


}
