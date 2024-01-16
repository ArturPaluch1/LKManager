package LKManager.DAO;


import LKManager.LK.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ScheduleDAO extends JpaRepository<Schedule, Long>, CustomScheduleDAO {

    @Query("SELECT distinct s FROM Schedule s left JOIN FETCH s.rounds r where s.id=r.schedule "
    )
    List<Schedule> findByIdAndFetchRolesEagerly();

}
