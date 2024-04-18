package LKManager.model.RecordsAndDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamDTO {
    private Integer teamId;

    private String teamName;

    private UserDataDTO userDTO;


}
