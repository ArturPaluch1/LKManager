package LKManager.DAO_SQL;

import LKManager.model.Round;

import java.util.List;

public interface CustomRoundDAO {

//    Runda save(Runda runda);

    Round saveRound(Round round);

    //  void saveRound(Runda runda, int runda);
    //  List<Terminarz> findAll();
    //  void saveResults(Integer runda, Terminarz terminarz1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;


  //  Round findByScheduleIdAndRoundId(long scheduleId, int roundId);

    List<Round> findAllByScheduleId(long id)throws java.sql.SQLSyntaxErrorException;

    List<Round> findAllByScheduleName(String chosenScheduleName);

    Round findRound(String chosenScheduleName, Integer roundNumber);

 /*   Round findRoundWitchMatches(String chosenScheduleName, Integer roundNumber);*/




    //  Terminarz findByTerminarzName(String name);

    //  Runda findLastById();
}
