package LKManager.DAO;

import LKManager.LK.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoundDAO extends JpaRepository<Round,Long> ,CustomRoundDAO{

    @Query("SELECT distinct r FROM Round r LEFT JOIN Schedule s ON r.schedule = s join fetch r.matches m  WHERE s.name = :scheduleName order by r.date")
    List<Round> findAllRoundsOfSchedule(@Param("scheduleName")String scheduleName);

    @Query("SELECT r FROM Round r LEFT JOIN Schedule s ON r.schedule = s WHERE s.name = :scheduleName and r.nr=:roundNumber")
    Round  findRoundWitchMatches(@Param("scheduleName")String scheduleName,@Param("roundNumber")Integer roundNumber);

   /* @Modifying
    @Query("UPDATE Round r SET r.property = :newValue WHERE e.someOtherProperty = :someValue")
    Round updateRound(@Param("roundToUpdate") Round roundToUpdate, @Param("someValue") String someValue);*/
}
