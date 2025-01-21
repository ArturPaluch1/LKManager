package LKManager.model.RecordsAndDTO;

import LKManager.model.ScheduleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;


@Data


public class ScheduleSettingsDTO {
private long id;
private String name;
    private ScheduleStatus scheduleStatus;
    private ScheduleType scheduleType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ScheduleSettingsDTO(long id, String name, ScheduleStatus scheduleStatus, ScheduleType scheduleType, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.scheduleStatus = scheduleStatus;
        this.scheduleType = scheduleType;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public ScheduleSettingsDTO(long id, String name) {
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
