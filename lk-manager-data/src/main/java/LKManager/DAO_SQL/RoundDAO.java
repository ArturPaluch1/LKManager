package LKManager.DAO_SQL;

import LKManager.model.RecordsAndDTO.ScheduleType;
import LKManager.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@RedisHash
public interface RoundDAO extends JpaRepository<Round,Long> ,CustomRoundDAO{

    @Query("SELECT distinct r FROM Round r  JOIN Schedule s ON r.schedule = s left join fetch r.matches m  WHERE s.name = :scheduleName order by r.date")
    List<Round> findAllRoundsOfSchedule(@Param("scheduleName")String scheduleName);

    @Query("SELECT distinct r FROM Round r LEFT JOIN Schedule s ON r.schedule = s join fetch r.matches m WHERE s.name = :scheduleName and r.nr=:roundNumber")
    Round  findRoundWitchMatches(@Param("scheduleName")String scheduleName,@Param("roundNumber")Integer roundNumber);
    @Query("Select r from Round r  inner join "+
            "Schedule s on s.id=r.schedule "+
            "where s.id=:scheduleId and r.nr=:roundNumber ")
    Round findByScheduleIdAndRoundNumber(@Param("scheduleId")long scheduleId, @Param("roundNumber")int roundNumber) throws java.sql.SQLSyntaxErrorException;
   /* @Modifying
    @Query("UPDATE Round r SET r.property = :newValue WHERE e.someOtherProperty = :someValue")
    Round updateRound(@Param("roundToUpdate") Round roundToUpdate, @Param("someValue") String someValue);*/

@Query("select distinct r from Round r " +
        " left JOIN  Match m ON m.round = r.id " +
        "where r.date=:matchDate"
)
    List<Round> findRoundsByDate(@Param("matchDate")LocalDate matchDate);

    @Query(" SELECT  r FROM Round r  " +
            "   left join Schedule s on s.id=r.schedule"+
            " left join Match m on m.round=r.id"+
            "  WHERE (r.date = (SELECT MAX(date) FROM Round) and s.scheduleType=:scheduleType )")
    List<Round> findLastRoundOFSchedulesOfGivenType(@Param("scheduleType") ScheduleType scheduleType);

}
