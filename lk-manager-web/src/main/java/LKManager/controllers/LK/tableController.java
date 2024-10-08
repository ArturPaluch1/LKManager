package LKManager.controllers.LK;

import LKManager.DAO_SQL.MatchDAO;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.model.Schedule;
import LKManager.model.Table;
import LKManager.services.*;
import LKManager.services.FilesService_unused.PlikiService;
import LKManager.services.RedisService.RedisTableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class tableController {
    private final MZUserService MZUserService;

private final ScheduleService scheduleService;
/*@Autowired
  private   MZCache mzCache;*/
    private String chosenSchedule;
    private Schedule schedule;
    private final PlikiService plikiService;
    private final TableService tableService;
    private final ScheduleDAO scheduleDAO;

    private final MatchDAO matchDAO;
    private final MatchDAO matchDAOimpl;
private final LKUserService lkUserService;
private final CookieManager cookieManager;
private final RedisTableService redisTableService;
    public tableController(MZUserService MZUserService, ScheduleService scheduleService, PlikiService plikiService, TableService tableService, ScheduleDAO scheduleDAO, MatchDAO matchDAO1, MatchDAO matchDAO, LKUserService lkUserService, CookieManager cookieManager, RedisTableService redisTableService) {
        this.MZUserService = MZUserService;

        this.scheduleService = scheduleService;


        this.plikiService = plikiService;
        this.tableService = tableService;
        this.scheduleDAO = scheduleDAO;
        this.matchDAO = matchDAO1;
        this.matchDAOimpl = matchDAO;
        this.lkUserService = lkUserService;
        this.cookieManager = cookieManager;
        this.redisTableService = redisTableService;
    }



   @GetMapping({"public/LK/table.html", "public/LK/table"} )
    public String index(HttpServletResponse response, HttpServletRequest request,Model model, @RequestParam(value="chosenSchedule", required = false)String chosenscheduleName) throws URISyntaxException, IOException, JAXBException {


       chosenscheduleName= cookieManager.saveOrUpdateChosenScheduleCookie(chosenscheduleName);



       Table table = null;
       //todo dodać że jeśli==null error że wybierz terminarz
if(chosenscheduleName!=null)
{


table= tableService.getTable(chosenscheduleName);







}



if(table!=null)
{
  //  model.addAttribute("schedules", scheduleDAO.findAll());
    //todo zaminic \/ cache na redis
   // model.addAttribute("schedules",mzCache.getSchedules());
    model.addAttribute("schedules",scheduleService.getScheduleNamesOngoingOrFinished());
    model.addAttribute("chosenSchedule",chosenscheduleName);
    model.addAttribute("table",table.getPlayerSummaries());
    return "public/LK/table";
}
else
{
    model.addAttribute("chosenSchedule",chosenscheduleName);
    model.addAttribute("schedules",scheduleService.getScheduleNamesOngoingOrFinished());

    //todo zaminic \/ cache na redis
   // model.addAttribute("schedules",mzCache.getSchedules());
    return "public/LK/table";
}




    }




}
