package LKManager.model.RecordsAndDTO;

import LKManager.Security.RoundsNumberRequired;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RoundsNumberRequired
public class AddScheduleDTO {
/*todo walidacja
https://blog.mloza.pl/java-bean-validation-spring-boot-sprawdzanie-poprawnosci-danych-w-spring-boocie/

*/

    @NotBlank(message = "Musisz podać datę.")
    private String date;
    @NotBlank(message = "Nazwa nie może być pusta.")
    private String name;
    @Size(min = 2,message = "Musisz sybrać co najmniej jedną parę graczy.")
    private List<String> playersList;


    private Integer roundsNumber;
    @NotNull(message = "Wybierz rodzaj terminarza.")
    private ScheduleType scheduleType;

    public void setListaGraczy() {

    }


}