package LKManager.model.RecordsAndDTO;

import lombok.Data;

import java.util.List;


@Data


public class ScheduleDTO {
private long id;
private String name;
    private ScheduleType scheduleType;
private List<RoundDTO> rounds;

   public ScheduleDTO(long id, String name, List<RoundDTO> rounds, ScheduleType scheduleType) {
        this.id = id;
        this.name = name;
this.scheduleType=scheduleType;
        this.rounds = rounds;
    }

    public ScheduleDTO(long id, String name, ScheduleType scheduleType) {
        this.id = id;
        this.name = name;
        this.scheduleType=scheduleType;
    }

    public String getName() {
        return name.replace("_"," ");
    }

    public void setName(String name) {
        this.name = name.replace(" ","_");
    }
}
