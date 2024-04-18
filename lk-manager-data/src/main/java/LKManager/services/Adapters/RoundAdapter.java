package LKManager.services.Adapters;

import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.model.Round;

import java.util.stream.Collectors;


public class RoundAdapter {
    public static RoundDTO adapt(Round round )
    {



        return new RoundDTO(round.getId(),round.getNr(),round.getSchedule().getId(),round.getDate(),round.getMatches().stream().map(match->MatchAdapter.adapt(match)).collect(Collectors.toList()));


    }
  /*  public static RoundDTO adaptWithScheduleParent(Round round, ScheduleDTO scheduleDTO)
    {



        return new RoundDTO(round.getId(),round.getNr(),scheduleDTO,round.getDate(),round.getMatches().stream().map(match->MatchAdapter.adapt(match)).collect(Collectors.toList()));


    }*/
}