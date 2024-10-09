package LKManager.services.Adapters;

import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.Schedule;
import LKManager.services.Comparators.RoundDTOByDateComparator;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleAdapter {
    public static ScheduleDTO adapt(Schedule schedule)
    {
        if(schedule==null)
        {
            return null;
        }
        else
        {
            ScheduleDTO scheduleDTO = new ScheduleDTO(schedule.getId(),schedule.getStartDate(), schedule.getName(),schedule.getScheduleType(),schedule.getScheduleStatus(),schedule.getEndDate());

 /*   var t=    schedule.getRounds().stream().map(round -> RoundAdapter.adaptWithScheduleParent(round, scheduleDTO)).collect(Collectors.toList());
        scheduleDTO.setRounds(t);*/

            List<RoundDTO> t=    schedule.getRounds().stream().map(round -> RoundAdapter.adapt(round)).sorted(new RoundDTOByDateComparator()).collect(Collectors.toList());
            scheduleDTO.setRounds(t);





            return scheduleDTO;
        }



    }
}
