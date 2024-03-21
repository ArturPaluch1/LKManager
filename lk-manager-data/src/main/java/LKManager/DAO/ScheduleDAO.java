package LKManager.DAO;


import LKManager.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ScheduleDAO extends JpaRepository<Schedule, Long>, CustomScheduleDAO {


    @Query("SELECT s FROM Schedule s " +
            "INNER JOIN Round r ON r.schedule = s.id " +
            "INNER JOIN Match m ON m.round = r.id " +
            "WHERE r.schedule IN " +
            "(SELECT r2.schedule FROM Round r2 " +
            "INNER JOIN Match m2 ON m2.round = r2.id " +
            "WHERE m2.dateDB = (SELECT MAX(m3.dateDB) FROM Match m3))"
    )
    Schedule getTheNewest();

    @Query("SELECT distinct s FROM Schedule s left JOIN FETCH s.rounds r where s.id=r.schedule "
    )
    List<Schedule> findAllFetchRoundsEagerly();


    @Query("SELECT distinct s FROM Schedule s left JOIN FETCH s.rounds r where s.id=:scheduleId ")
 //   @Query("SELECT DISTINCT s FROM Schedule s LEFT JOIN FETCH s.rounds r LEFT JOIN FETCH r.matches WHERE s.id = :scheduleId")
 //   @Query("SELECT DISTINCT s FROM Schedule s LEFT JOIN FETCH s.rounds LEFT JOIN FETCH s.rounds.matches WHERE s.id = :scheduleId")


    Schedule findByIdAndFetchMatchesEagerly(@Param("scheduleId") long scheduleId);

  /*  @Query("SELECT DISTINCT s FROM Schedule s " +
            "LEFT JOIN FETCH s.rounds r " +
            "LEFT JOIN FETCH r.matches m " +


            "WHERE s.id = :parentId")
    List<Schedule> findByIdWitchRoundsMatchesUsersAndTeams(@Param("parentId") long parentId);*/
/*   "LEFT JOIN FETCH m.userData u " +
            "LEFT JOIN FETCH u.teamlist t " +*/
}
