package LKManager.services.Scheduling;

import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.Table;
import LKManager.services.RedisService.RedisScheduleService;
import LKManager.services.RedisService.RedisTableService;
import LKManager.services.RoundService;
import LKManager.services.ScheduleService;
import LKManager.services.TableService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Data
@Service
public class RoundMatchesUpdateService {

    private final ScheduleService scheduleService;
    private final RoundService roundService;
    private final RedisTableService redisTableService;
    private final RedisScheduleService redisScheduleService;
private final TableService tableService;
    public RoundMatchesUpdateService(ScheduleService scheduleService, RoundService roundService, RedisTableService redisTableService, TableService tableService, RedisScheduleService redisScheduleService) {
        this.scheduleService = scheduleService;
        this.redisTableService = redisTableService;
        this.redisScheduleService = redisScheduleService;
        this.roundService = roundService;


        this.tableService = tableService;
    }


    @Scheduled(cron = "0 0 1 * * ?", zone = "Europe/Warsaw")
            public void updateRoundMatches() {
        ZonedDateTime warsawTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);

        System.out.println("Update round matches task performed on: " + warsawTime.format(formatter) + "\n" +"Thread's name: " + Thread.currentThread().getName());

               List<RoundDTO> foundRounds= roundService.getRoundsByDate(warsawTime.toLocalDate().plusDays(6));
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
        }



