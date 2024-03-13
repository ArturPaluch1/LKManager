package LKManager.controllers.LK;

import LKManager.DAO.MatchDAO;
import LKManager.DAO.ScheduleDAO;
import LKManager.LK.Schedule;
import LKManager.LK.Table;
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



       Table table = null;
       //todo dodać że jeśli==null error że wybierz terminarz
if(!chosenscheduleName.equals(null))
{

   table= tableService.createTable(chosenscheduleName);




}



if(table!=null)
{
  //  model.addAttribute("schedules", scheduleDAO.findAll());
    model.addAttribute("schedules",mzCache.getSchedules());
    model.addAttribute("chosenSchedule",chosenscheduleName);
    model.addAttribute("table",table.getPlayerSummaries());
    return "LK/table";
}
else
{
    model.addAttribute("chosenSchedule",chosenscheduleName);
    model.addAttribute("schedules",mzCache.getSchedules());
    return "LK/table";
}




    }




}
