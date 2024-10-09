package LKManager.model.account;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserSettingsFormModel {

        private UserDataDTO user;
    @Email(message = "Email should be valid")
    private String userEmail;
        private TMcity tmCity;
        private Schedule upcomingSchedule;


    @Data
    @AllArgsConstructor
    public static class TMcity{
        private     Long Id;
        private String name;
    }
}
