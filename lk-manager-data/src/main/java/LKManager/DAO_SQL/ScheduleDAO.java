package LKManager.DAO_SQL;


import LKManager.model.RecordsAndDTO.ScheduleNameDTO;
import LKManager.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RedisHash
public interface ScheduleDAO extends JpaRepository<Schedule, Long>, CustomScheduleDAO {


    @Query("SELECT s FROM Schedule s " +
            "INNER JOIN Round r ON r.schedule = s.id " +
            "INNER JOIN Match m ON m.round = r.id " +
            "WHERE  (s.scheduleStatus=1 or s.scheduleStatus=2) and  r.schedule IN " +
            "(SELECT r2.schedule FROM Round r2 " +
            "INNER JOIN Match m2 ON m2.round = r2.id " +
            "WHERE m2.dateDB = (SELECT MAX(m3.dateDB) FROM Match m3))"
    )
    Schedule getTheNewestOngoingOrFinished();

    @Query("SELECT distinct s FROM Schedule s left JOIN FETCH s.rounds r where s.id=r.schedule "
    )
    List<Schedule> findAllFetchRoundsEagerly();

//@Query("SELECT  new LKManager.model.RecordsAndDTO.ScheduleDTO( s.id, s.name,s.rounds) FROM Schedule s ")


/*@Query("SELECT new LKManager.model.RecordsAndDTO.ScheduleDTO(s.id, s.name, " +
        "(SELECT  new LKManager.model.RecordsAndDTO.RoundDTO(r.id, r.nr) " +
        " FROM Round r WHERE r.schedule = s)) " +
        "FROM Schedule s")*/
/*@Query("SELECT new LKManager.model.RecordsAndDTO.ScheduleDTO(s.id, s.name, r.id, r.nr) " +
        "FROM Schedule s " +
        "LEFT JOIN s.rounds r")*/


    @Query("select new LKManager.model.RecordsAndDTO.ScheduleNameDTO(id, name) from Schedule s where s.scheduleStatus!=0")
    List<ScheduleNameDTO> getScheduleNamesOngoingOrFinished();
    @Query("SELECT distinct s FROM Schedule s left JOIN FETCH s.rounds r where s.id=:scheduleId ")
    Schedule findByIdAndFetchMatchesEagerly(@Param("scheduleId") long scheduleId);





  /*  @Query("SELECT DISTINCT s FROM Schedule s " +
            "LEFT JOIN FETCH s.rounds r " +
            "LEFT JOIN FETCH r.matches m " +


            "WHERE s.id = :parentId")
    List<Schedule> findByIdWitchRoundsMatchesUsersAndTeams(@Param("parentId") long parentId);*/
/*   "LEFT JOIN FETCH m.userData u " +
            "LEFT JOIN FETCH u.teamlist t " +*/




}
