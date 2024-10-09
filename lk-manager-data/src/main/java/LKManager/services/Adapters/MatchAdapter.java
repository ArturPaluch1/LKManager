package LKManager.services.Adapters;

import LKManager.model.MatchesMz.Match;
import LKManager.model.RecordsAndDTO.MatchDTO;

public class MatchAdapter {

    public static MatchDTO adapt(Match match)
    {


        return new MatchDTO(match.getId(), match.getDateDB(), match.getUserMatchResult1(), match.getUserMatchResult2(), match.getOpponentMatchResult1(), match.getOpponentMatchResult2() ,UserAdapter.convertMZUserDataToUserMzDTO(match.getOpponentMZUserData()),UserAdapter.convertMZUserDataToUserMzDTO(match.getMZUserData()));



    }
}
