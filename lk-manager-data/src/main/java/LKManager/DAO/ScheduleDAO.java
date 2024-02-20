package LKManager.DAO;


import LKManager.LK.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ScheduleDAO extends JpaRepository<Schedule, Long>, CustomScheduleDAO {

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
