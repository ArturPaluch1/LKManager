package LKManager.DAO;

import LKManager.model.MatchesMz.Match;
import LKManager.LK.Schedule;
import java.util.List;

public interface CustomMatchDAO {
 //   Match save(Match match);

    void saveRound(Schedule schedule, int round);
    List<Match> findAll();
 //   void saveResults(Integer runda, Terminarz terminarz1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;
    Match findByMatchId(long id);


    List<Match> findAllByScheduleIdAndRoundId(long id, int i);

    List<Match> findAllbyScheduleId(long id);


}
