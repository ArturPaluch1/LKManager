package LKManager.model.RecordsAndDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMzDTO {

    private Long MZuserId;
    private String MZUsername;
    private Integer teamId;
    private String teamName;
}
