package LKManager.services.RedisService;

import LKManager.model.RecordsAndDTO.MatchDTO;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class RedisMatchService {
    private final GsonService gsonService;

     List<MatchDTO>  findAllByScheduleIdAndRoundId(long scheduleId, Integer roundId)
     {
         return null;
     }







}
