package LKManager.DAO;

import LKManager.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoundDAO extends JpaRepository<Round,Long> ,CustomRoundDAO{

    @Query("SELECT distinct r FROM Round r LEFT JOIN Schedule s ON r.schedule = s join fetch r.matches m  WHERE s.name = :scheduleName order by r.date")
    List<Round> findAllRoundsOfSchedule(@Param("scheduleName")String scheduleName);

    @Query("SELECT distinct r FROM Round r LEFT JOIN Schedule s ON r.schedule = s join fetch r.matches m WHERE s.name = :scheduleName and r.nr=:roundNumber")
    Round  findRoundWitchMatches(@Param("scheduleName")String scheduleName,@Param("roundNumber")Integer roundNumber);
    @Query("Select r from Round r  inner join "+
            "Schedule s on s.id=r.schedule "+
            "where s.id=:scheduleId and r.nr=:roundNumber ")
    Round findByScheduleIdAndRoundId(@Param("scheduleId")long scheduleId, @Param("roundNumber")int roundId) throws java.sql.SQLSyntaxErrorException;
   /* @Modifying
    @Query("UPDATE Round r SET r.property = :newValue WHERE e.someOtherProperty = :someValue")
    Round updateRound(@Param("roundToUpdate") Round roundToUpdate, @Param("someValue") String someValue);*/

@Query("select distinct r from Round r " +
        " left JOIN  Match m ON m.round = r.id " +
        "where r.date=:matchDate"
)
    List<Round> findRoundsByDate(@Param("matchDate")LocalDate matchDate);

}
