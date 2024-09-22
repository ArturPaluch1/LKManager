package LKManager.model.RecordsAndDTO;

import LKManager.model.ScheduleStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data


public class ScheduleDTO {
private long id;
private String name;
    private ScheduleType scheduleType;
private List<RoundDTO> rounds;
private ScheduleStatus scheduleStatus;
private LocalDate startDate;
   public ScheduleDTO(long id,LocalDate startDate, String name, List<RoundDTO> rounds, ScheduleType scheduleType,ScheduleStatus scheduleStatus) {
        this.id = id;
        this.name = name;
this.scheduleType=scheduleType;
        this.rounds = rounds;
        this.scheduleStatus=scheduleStatus;
        this.startDate=startDate;
    }

    public ScheduleDTO(long id, LocalDate startDate, String name, ScheduleType scheduleType,ScheduleStatus scheduleStatus) {
        this.id = id;
        this.name = name;
        this.scheduleType=scheduleType;
        this.scheduleStatus=scheduleStatus;
        this.startDate=startDate;
    }

    public String getName() {
        return name.replace("_"," ");
    }

    public void setName(String name) {
        this.name = name.replace(" ","_");
    }
}
