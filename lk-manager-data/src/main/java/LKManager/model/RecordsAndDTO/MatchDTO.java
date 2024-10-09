package LKManager.model.RecordsAndDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data @AllArgsConstructor
public class MatchDTO {
    private Long id;
    private LocalDate dateDB;
    private Byte userMatchResult1;
    private Byte userMatchResult2;
    private Byte opponentMatchResult1;
    private Byte opponentMatchResult2;
    private UserMzDTO opponentUserMzDTO;
    private UserMzDTO userUserMzDTO;


}
