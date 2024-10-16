package LKManager.services.Scheduling;

import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleType;
import LKManager.model.Schedule;
import LKManager.model.ScheduleStatus;
import LKManager.model.Table;
import LKManager.services.Adapters.ScheduleAdapter;
import LKManager.services.ScheduleService;
import LKManager.services.TableService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class SwissScheduleResultsService {
private final ScheduleService scheduleService;
private final TableService tableService;

 @Scheduled(cron = "0 55 0 * * *")// 0:55 codziennie
public boolean calculateNewRound()
{


    List<Schedule> schedules=scheduleService.getScheduleByTypeAndStatus(ScheduleType.swissSchedule, ScheduleStatus.ONGOING);

    for (Schedule schedule:schedules
    ) {
        ScheduleDTO scheduleDTO= ScheduleAdapter.adapt(schedule);
        Table table = tableService.createSwissScheduleTable(scheduleDTO);

        scheduleService.calculateNextRoundOfSwissSchedule(scheduleDTO, table);
    }

    return true;
    }


}
