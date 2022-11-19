package LKManager.controllers.LK;

import LKManager.LK.Comparators.GraczPodsumowanieComparatorGoalDifference;
import LKManager.LK.Comparators.GraczPodsumowanieComparatorGoalLost;
import LKManager.LK.Comparators.GraczPodsumowanieComparatorGoalScored;
import LKManager.LK.Comparators.GraczPodsumowanieComparatorPoints;
import LKManager.LK.GraczPodsumowanie;
import LKManager.LK.Tabela;
import LKManager.LKManagerApplication;
import LKManager.services.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping({"tabela.html", "tabela", "tabela"} )
public class tabelaController {
    private final UserService userService;
    private final MatchService matchService;
  //  private List<UserData> skladTM = new ArrayList<>();
private final TerminarzService terminarzService;
    private final TeamTM teamTM;

    public tabelaController(UserService userService, MatchService matchService, TerminarzService terminarzService, TeamTM teamTM) {
        this.userService = userService;
        this.matchService = matchService;
        this.terminarzService = terminarzService;

        this.teamTM = teamTM;
    }


   @GetMapping({"tabela.html", "tabela", "tabela"} )
    public String index(Model model) throws URISyntaxException, IOException, JAXBException {


//var terminarz=terminarzService.wczytajTerminarz("lk-manager-web/src/main/java/LKManager/terminarz.xml");
       var terminarz=terminarzService
               .wczytajTerminarz("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
       Tabela tabela = new Tabela();



    //   if() todo jak tabeli pliku nie ma
           //   TeamTM
           for (var item :teamTM.wczytajUPSGZXML().getSkladUPSG()
           ) {
               var tempGracz= new GraczPodsumowanie();tempGracz.setGracz(item);

               tabela.getGraczePodsumowanie().add(tempGracz);
           }

////////////////////////////////////////////////////////////////////
       for (var runda:terminarz.getTerminarz()
            ) {
           for (var mecz: runda.getMecze()
                ) {

      if(LocalDate.now().plusDays(1).isAfter(LocalDate.parse(runda.getData().toString())))
      {
          var meczUser = mecz.getUser();
          var meczOpponent = mecz.getopponentUser();

/*var user =tabela.getGraczePodsumowanie().stream().
        findFirst().filter(a->a.getGracz().getUserId().equals(mecz.getUser().getUserId()))
        .orElse(null);
*/
          var   user =tabela.getGraczePodsumowanie().stream().
                  filter(a->a.getGracz().getUserId().equals(mecz.getUser().getUserId())).findFirst().orElse(null);
          var   userOp =tabela.getGraczePodsumowanie().stream().
                  filter(a->a.getGracz().getUserId().equals(mecz.getopponentUser().getUserId())).findFirst().orElse(null);


          if(userOp!=null&&userOp.getGracz().getTeamlist().get(0).getTeamName()!="pauza")
          {
              var goleStrzelone1 =mecz.getUserMatchResult1();
              var goleStrzelone2 =mecz.getUserMatchResult2();
              var goleStracone1= mecz.getOpponentMatchResult1();
              var goleStracone2=mecz.getOpponentMatchResult2();

              if(goleStrzelone1=="")goleStrzelone1="0";
              if(goleStrzelone2=="")goleStrzelone2="0";
              if(goleStracone1=="")goleStracone1="0";
              if(goleStracone2=="")goleStracone2="0";

              var sumaPunktow=user.getSumaPunktow();
              var userGoleStrzelone = user.getGoleStrzelone();
              var userGoleStracone = user.getGoleStracone();

              if(sumaPunktow=="")sumaPunktow="0";
              if(userGoleStrzelone=="")userGoleStrzelone="0";
              if(userGoleStracone=="")userGoleStracone="0";





              user.setGoleStrzelone(
                      String.valueOf( Integer.parseInt(userGoleStrzelone)
                              +  Integer.parseInt(goleStrzelone1)
                              +  Integer.parseInt(goleStrzelone2))
              );
              user.setGoleStracone(
                      String.valueOf( Integer.parseInt( userGoleStracone)
                              +Integer.parseInt(goleStracone1)
                              + Integer.parseInt(goleStracone2))
              );

              userGoleStrzelone = user.getGoleStrzelone();
              userGoleStracone = user.getGoleStracone();

              user.setRoznica(Integer.parseInt(userGoleStrzelone)-Integer.parseInt(userGoleStracone));


              if(Integer.parseInt(goleStrzelone1)>Integer.parseInt(goleStracone1)){user.setSumaPunktow(String.valueOf(Integer.parseInt(sumaPunktow)+3));}
              else if(Integer.parseInt(goleStrzelone1)==Integer.parseInt(goleStracone1)){user.setSumaPunktow(String.valueOf(Integer.parseInt(sumaPunktow)+1));}

               sumaPunktow=user.getSumaPunktow();
              if(sumaPunktow=="")sumaPunktow="0";

              if(Integer.parseInt(goleStrzelone2)>Integer.parseInt(goleStracone2)){user.setSumaPunktow(String.valueOf(Integer.parseInt(sumaPunktow)+3));}
              else if(Integer.parseInt(goleStrzelone2)==Integer.parseInt(goleStracone2)){user.setSumaPunktow(String.valueOf(Integer.parseInt(sumaPunktow)+1));}




                  /////////////////////////////////////////////////////


              var OpponentgoleStrzelone1 =mecz.getOpponentMatchResult1();
              var OpponentgoleStrzelone2 =mecz.getOpponentMatchResult2();
              var OpponentgoleStracone1= mecz.getUserMatchResult1();
              var OpponentgoleStracone2=mecz.getUserMatchResult2();

              if(OpponentgoleStrzelone1=="")OpponentgoleStrzelone1="0";
              if(OpponentgoleStrzelone2=="")OpponentgoleStrzelone2="0";
              if(OpponentgoleStracone1=="")OpponentgoleStracone1="0";
              if(OpponentgoleStracone2=="")OpponentgoleStracone2="0";

              var OpponentsumaPunktow=userOp.getSumaPunktow();
              var OpponentGoleStrzelone = userOp.getGoleStrzelone();
              var OpponentGoleStracone = userOp.getGoleStracone();

              if(OpponentsumaPunktow=="")OpponentsumaPunktow="0";
              if(OpponentGoleStrzelone=="")OpponentGoleStrzelone="0";
              if(OpponentGoleStracone=="")OpponentGoleStracone="0";




              userOp.setGoleStrzelone(
                      String.valueOf( Integer.parseInt(OpponentGoleStrzelone)
                              +Integer.parseInt(OpponentgoleStrzelone1)
                              + Integer.parseInt(OpponentgoleStrzelone2))

              );
              userOp.setGoleStracone(
                      String.valueOf( Integer.parseInt(OpponentGoleStracone)
                              +Integer.parseInt(OpponentgoleStracone1)
                              + Integer.parseInt(OpponentgoleStracone2))
              );
              OpponentGoleStrzelone = userOp.getGoleStrzelone();
              OpponentGoleStracone = userOp.getGoleStracone();

              userOp.setRoznica(Integer.parseInt(OpponentGoleStrzelone)-Integer.parseInt(OpponentGoleStracone));

              if(Integer.parseInt(OpponentgoleStrzelone1)>Integer.parseInt(OpponentgoleStracone1)){userOp.setSumaPunktow(String.valueOf(Integer.parseInt(OpponentsumaPunktow)+3));}
              else if(Integer.parseInt(OpponentgoleStrzelone1)==Integer.parseInt(OpponentgoleStracone1)){userOp.setSumaPunktow(String.valueOf(Integer.parseInt(OpponentsumaPunktow)+1));}

              OpponentsumaPunktow=userOp.getSumaPunktow();
              if(OpponentsumaPunktow=="")OpponentsumaPunktow="0";

              if(Integer.parseInt(OpponentgoleStrzelone2)>Integer.parseInt(OpponentgoleStracone2)){userOp.setSumaPunktow(String.valueOf(Integer.parseInt(OpponentsumaPunktow)+3));}
              else if(Integer.parseInt(OpponentgoleStrzelone2)==Integer.parseInt(OpponentgoleStracone2)){userOp.setSumaPunktow(String.valueOf(Integer.parseInt(OpponentsumaPunktow)+1));}
          }
//}
      }

           }

       }
///////////////////////////////////////////////
       //sortowanie po punktach
       tabela.getGraczePodsumowanie().sort(new GraczPodsumowanieComparatorPoints());
var gracze=tabela.getGraczePodsumowanie();
boolean nowaRownosc= false;

//indeksy zmian punktów -> i=początek zmiany
       List<Integer> listaIndeksow= new ArrayList<>();
       listaIndeksow.add(0);
       for (int i = 0; i <gracze.size()-1 ; i++) {
           if(!(gracze.get(i+1).getSumaPunktow().equals(gracze.get(i).getSumaPunktow())))
           {
               listaIndeksow.add(i+1);
           }

       }

      // sprawdzanie czy punkty są równe i sortowanie po różnicy
       List<GraczPodsumowanie> tempstring=new ArrayList<>();
       for (int i = 0; i <listaIndeksow.size() ; i++) {
           //sortowanie końcówki listy (ze względu na porównanie ostatniego indeksu z i+1)
           if(i==listaIndeksow.size()-1)
           {
             //  var  posortowane=  gracze.subList(listaIndeksow.get(listaIndeksow.size()-1),gracze.size());
               var  posortowane=  gracze.subList(listaIndeksow.get(i),(gracze.size()));
               posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());

               sortujRoznicaBramek(tempstring, posortowane);


               //sortowanie po strzelonych
               //  posortowane.sort(new GraczPodsumowanieComparatorGoalScored());

           }
           //sortowanie normalnie
           else
           {
             var  posortowane=  gracze.subList(listaIndeksow.get(i),listaIndeksow.get(i+1));
               posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());

               sortujRoznicaBramek(tempstring, posortowane);


               //sortowanie po strzelonych
            //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
             //  tempstring.addAll(posortowane);
               //sortowanie posortowane  po straconych
           }






       }
//sortowanie pozostalych


       //tempstring= posortowane przez pkt i roznice






       tabela.getGraczePodsumowanie().clear();

       tabela.setGraczePodsumowanie(tempstring);

       //// sprawdzanie czy roznica jest rowna i sortowanie po strzelonych
    /*   gracze.clear();
       gracze=tabela.getGraczePodsumowanie();

       List<Integer> listaIndeksow2= new ArrayList<>();
       listaIndeksow2.add(0);
       for (int i = 0; i <gracze.size()-1 ; i++) {
           if(!(gracze.get(i+1).getSumaPunktow().equals(gracze.get(i).getSumaPunktow())))
           {
               listaIndeksow2.add(i+1);
           }

       }

       List<GraczPodsumowanie> tempstring2=new ArrayList<>();
       for (int i = 0; i <listaIndeksow.size()-1 ; i++) {
           var  posortowane=  gracze.subList(listaIndeksow.get(i),listaIndeksow.get(i+1));
           posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());
           tempstring.addAll(posortowane);
       }

       var  posortowane=  gracze.subList(listaIndeksow.get(listaIndeksow.size()-1),gracze.size());
       posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());
       tempstring.addAll(posortowane);


       */

       ///////////////////////////////




model.addAttribute("tabela",tabela.getGraczePodsumowanie());
        return "LK/tabela";
    }

    protected void sortujRoznicaBramek(List<GraczPodsumowanie> tempstring, List<GraczPodsumowanie> posortowane) {
        var listaIndeksowPowtorzonychRoznic = znajdzIndeksyPowtorzonychRoznic(posortowane);

        for (int j = 0; j < listaIndeksowPowtorzonychRoznic.size(); j++) {
            //sortowanie końcówki listy (ze względu na porównanie ostatniego indeksu z i+1)
            if (j == listaIndeksowPowtorzonychRoznic.size() - 1) {



                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychRoznic.get(j), (posortowane.size()));
                // sortowanie po strzelonych
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalScored());

                sortujStracone(tempstring, posortowaneRoznice);
              //  tempstring.addAll(posortowaneRoznice);
            } else {
                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychRoznic.get(j), listaIndeksowPowtorzonychRoznic.get(j + 1));
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalScored());

                sortujStracone(tempstring, posortowaneRoznice);
                //sortowanie po strzelonych
                //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
             //   tempstring.addAll(posortowaneRoznice);
                //sortowanie posortowane  po straconych
            }
        }
    }

    protected void sortujStracone(List<GraczPodsumowanie> tempstring, List<GraczPodsumowanie> posortowane) {
        var listaIndeksowPowtorzonychStrzelonych = znajdzIndeksyPowtorzonychStrzelonych(posortowane);

        for (int j = 0; j < listaIndeksowPowtorzonychStrzelonych.size(); j++) {
            //sortowanie końcówki listy (ze względu na porównanie ostatniego indeksu z i+1)
            if (j == listaIndeksowPowtorzonychStrzelonych.size() - 1) {

                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychStrzelonych.get(j), (posortowane.size()));
                // sortowanie po strzelonych
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalLost());
                tempstring.addAll(posortowaneRoznice);
            } else {
                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychStrzelonych.get(j), listaIndeksowPowtorzonychStrzelonych.get(j + 1));
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalLost());
                //sortowanie po strzelonych
                //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
                tempstring.addAll(posortowaneRoznice);
                //sortowanie posortowane  po straconych
            }
        }
    }




    protected List<Integer> znajdzIndeksyPowtorzonychRoznic(List<GraczPodsumowanie> gracze) {
        List<Integer> listaIndeksow1= new ArrayList<>();
        listaIndeksow1.add(0);
        for (int ii = 0; ii < gracze.size()-1 ; ii++) {
            if(!(gracze.get(ii+1).getRoznica().equals(gracze.get(ii).getRoznica())))
            {
                listaIndeksow1.add(ii+1);
            }

        }
        return listaIndeksow1;
    }
    protected List<Integer> znajdzIndeksyPowtorzonychStrzelonych(List<GraczPodsumowanie> gracze) {
        List<Integer> listaIndeksow1= new ArrayList<>();
        listaIndeksow1.add(0);
        for (int ii = 0; ii < gracze.size()-1 ; ii++) {
            if(!(gracze.get(ii+1).getGoleStrzelone().equals(gracze.get(ii).getGoleStrzelone())))
            {
                listaIndeksow1.add(ii+1);
            }

        }
        return listaIndeksow1;
    }

}
