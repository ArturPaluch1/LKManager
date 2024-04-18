package LKManager.model.RecordsAndDTO;

import lombok.Data;

import java.util.List;


@Data


public class ScheduleDTO {
private long id;
private String name;
private List<RoundDTO> rounds;

   public ScheduleDTO(long id, String name, List<RoundDTO> rounds) {
        this.id = id;
        this.name = name;
        this.rounds = rounds;
    }

    public ScheduleDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name.replace("_"," ");
    }

    public void setName(String name) {
        this.name = name.replace(" ","_");
    }
}
