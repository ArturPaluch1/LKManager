package LKManager.services.RedisService;

import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class RedisRoundService {
    private final GsonService gsonService;
    RoundDTO findByScheduleIdAndRoundId(long scheduleId, Integer roundId)
    {
return null;
    }


    List<RoundDTO> findAllByScheduleId(long scheduleId)
    {
        return null;
    }





}
