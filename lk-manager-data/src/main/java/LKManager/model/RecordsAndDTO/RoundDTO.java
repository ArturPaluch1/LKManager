package LKManager.model.RecordsAndDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor

public class RoundDTO {
    private long id;
    private int nr;
  //  private ScheduleDTO scheduleDTO;
  private long scheduleId;
    private LocalDate date;
    private List<MatchDTO> matches;

    @Override
    public String toString() {
        return "RoundDTO{" +
                "id=" + id +
                ", nr=" + nr +
                ", scheduleId=" + scheduleId +
                ", date=" + date +
                ", matchesDTO=" + matches +
                '}';
    }

/*   @Override
    public String toString() {
        return "RoundDTO{" +
                "id=" + id +
                ", nr=" + nr +
                ", scheduleDTO=" + scheduleDTO.getId() +
                ", date=" + date +
                ", matchesDTO=" + matchesDTO +
                '}';
    }*/


}