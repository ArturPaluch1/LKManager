package LKManager.TimerTasks;

import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.Table;
import LKManager.services.RedisService.RedisScheduleService;
import LKManager.services.RedisService.RedisTableService;
import LKManager.services.RoundService;
import LKManager.services.ScheduleService;
import LKManager.services.TableService;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
@Data
public class RoundMatchesUpdateTimer  extends Timer {
    private TimerTask task;
    private final ScheduleService scheduleService;
    private final RoundService roundService;
    private final RedisTableService redisTableService;
    private final RedisScheduleService redisScheduleService;
private final TableService tableService;
    public RoundMatchesUpdateTimer(ScheduleService scheduleService, RoundService roundService, RedisTableService redisTableService, TableService tableService, RedisScheduleService redisScheduleService) {
        this.scheduleService = scheduleService;
        this.redisTableService = redisTableService;
        this.redisScheduleService = redisScheduleService;

        this.task = new TimerTask() {

            @Override
            public void run() {
                System.out.println("Update round matches task performed on: " + new Date()  + "\n" +"Thread's name: " + Thread.currentThread().getName());

               List<RoundDTO> foundRounds= roundService.getRoundsByDate(LocalDate.now().plusDays(6));
               if(foundRounds!=null)
               {
                   foundRounds.removeIf(r->r.getMatches().size()!=0);
                   for (RoundDTO round:foundRounds
                   ) {
                       ScheduleDTO scheduleContainigRound= scheduleService.getSchedule_ById(round.getScheduleId());
                       Table table= tableService.createSwissScheduleTable(scheduleContainigRound);
                       scheduleService.calculateNextRoundOfSwissSchedule(scheduleContainigRound,table);



                   }
               }



            }
        };
        this.roundService = roundService;


        this.tableService = tableService;
    };
}
