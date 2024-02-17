package LKManager.controllers.LK;

import LKManager.DAO.MatchDAO;
import LKManager.DAO.ScheduleDAO;
import LKManager.LK.Table;
import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.services.Cache.MZCache;
import LKManager.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class tableController {
    private final MZUserService MZUserService;

private final ScheduleService scheduleService;
@Autowired
  private   MZCache mzCache;
    private String chosenSchedule;
    private Schedule schedule;
    private final  PlikiService plikiService;
    private final TableService tableService;
    private final ScheduleDAO scheduleDAO;

    private final MatchDAO matchDAO;
    private final MatchDAO matchDAOimpl;
private final LKUserService lkUserService;
    public tableController(MZUserService MZUserService, ScheduleService scheduleService, PlikiService plikiService, TableService tableService, ScheduleDAO scheduleDAO, MatchDAO matchDAO1, MatchDAO matchDAO, LKUserService lkUserService) {
        this.MZUserService = MZUserService;

        this.scheduleService = scheduleService;


        this.plikiService = plikiService;
        this.tableService = tableService;
        this.scheduleDAO = scheduleDAO;
        this.matchDAO = matchDAO1;
        this.matchDAOimpl = matchDAO;
        this.lkUserService = lkUserService;
    }



   @GetMapping({"table.html", "table"} )
    public String index(HttpServletResponse response, HttpServletRequest request,Model model, @RequestParam(value="chosenSchedule", required = false)String chosenscheduleName) throws URISyntaxException, IOException, JAXBException {


       chosenscheduleName= CookieManager.saveOrUpdateChosenScheduleCookie(Optional.ofNullable(chosenscheduleName),response,request, scheduleDAO);

/*


       if(wybranyTerminarz!=null)
       {
           //nastapila zmiana terminarza ->nr rundy=1
           if(wybranyTerminarz!=this.wybranyTerminarz)
           {
               this.wybranyTerminarz=wybranyTerminarz;

           }



       }

*/

       Table table = null;
if(!chosenscheduleName.equals(null))
{
    if(mzCache.getSchedules().size()!=0)
    {


        String finalChosenScheduleName = chosenscheduleName;
        Schedule schedule =  mzCache.getSchedules().stream().filter(a->a.getName().equals(finalChosenScheduleName)).findFirst().orElse(null);
        List<Match> matches = new ArrayList<>();
        if(schedule!=null)
           // if(mzCache.getSchedules())
     //       matches=  matchDAOimpl.findAllbyScheduleId(schedule.getId());// mzCache.getTerminarze().stream().filter(a->a.getName().equals(finalWybranyTerminarz)).distinct().toList();
    schedule.getRounds().forEach(r->matches.addAll(r.getMatches()));
       // Terminarz schedule;
        if(matches.size()!=0)
        {

            table=   tableService.createTable(matches);
        }



    }
    else
    {
        try {
                       /*        List<Match> matches=   terminarzDAO.findAllMatchesByTerminarzName(wybranyTerminarz);
                               List<UserData> participants = terminarzDAO.findAllParticipantsOfSchedule(wybranyTerminarz);
                 */
            Schedule schedule= scheduleDAO.findByScheduleName(chosenscheduleName);

            table= tableService.createTable(matchDAOimpl.findAllbyScheduleId(schedule.getId()));
        }
        catch (Exception e )
        {
            System.out.println("nie można pobrać wyników, spróbuj później");
        }



    }




}









/*
       Tabela tabela = new Tabela();
List<Match>mecze= terminarzDAO.findAllMatchesByTerminarzName(wybranyTerminarz);

      List<UserData> users=terminarzDAO.findAllParticipantsOfSchedule(wybranyTerminarz);

      if(users.size()!=0 && mecze.size()!=0)
      {
          users.forEach(a->{
              var tempGracz= new GraczPodsumowanie();
              tempGracz.setGracz(a);

              tabela.getGraczePodsumowanie().add(tempGracz);
          });


          for (Match match:mecze
          ){



              var   user =tabela.getGraczePodsumowanie().stream().
                      filter(a->a.getGracz().getUserId().equals(match.getUser().getUserId())).findFirst().orElse(null);
              var   userOp =tabela.getGraczePodsumowanie().stream().
                      filter(a->a.getGracz().getUserId().equals(match.getopponentUser().getUserId())).findFirst().orElse(null);

              checkResult(match,user,userOp);








          }
      }

      else
      {
          //todo ze brak danych z db
      }
*/


/*
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

               sortGoalsDifference(tempstring, posortowane);


               //sortowanie po strzelonych
               //  posortowane.sort(new GraczPodsumowanieComparatorGoalScored());

           }
           //sortowanie normalnie
           else
           {
             var  posortowane=  gracze.subList(listaIndeksow.get(i),listaIndeksow.get(i+1));
               posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());

               sortGoalsDifference(tempstring, posortowane);


               //sortowanie po strzelonych
            //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
             //  tempstring.addAll(posortowane);
               //sortowanie posortowane  po straconych
           }






       }*/
//sortowanie pozostalych


       //tempstring= posortowane przez pkt i roznice





  //     table.getGraczePodsumowanie().clear();

  //     table.setGraczePodsumowanie(tempstring);

       //// sprawdzanie czy roznica jest rowna i sortowanie po strzelonych


       ///////////////////////////////

if(table!=null)
{
  //  model.addAttribute("schedules", scheduleDAO.findAll());
    model.addAttribute("schedules",mzCache.getSchedules());
    model.addAttribute("chosenSchedule",chosenscheduleName);
    model.addAttribute("table",table.getPlayerSummaries());
}



        return "LK/table";
    }




}
