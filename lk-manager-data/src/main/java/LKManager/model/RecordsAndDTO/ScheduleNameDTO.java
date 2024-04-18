package LKManager.model.RecordsAndDTO;

import lombok.Data;


@Data


public class ScheduleNameDTO {
private long id;
private String name;

   public ScheduleNameDTO(long id, String name) {
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
