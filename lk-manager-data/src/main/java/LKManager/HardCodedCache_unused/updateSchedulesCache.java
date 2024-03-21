package LKManager.HardCodedCache_unused;

import LKManager.DAO.ScheduleDAO;
import LKManager.LK.Comparators.ScheduleByLocalDateComparator;
import LKManager.model.Schedule;
import LKManager.HardCodedCache_unused.Cache.MZCache;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public
class updateSchedulesCache extends MZCacheAction {
    @Autowired
    private MZCache mzCache;
    private final ScheduleDAO scheduleDAO;

    @Override @Transactional
    public MZCache update() {

        try {
            List<Schedule> schedules = scheduleDAO.findAllFetchRoundsEagerly();
            schedules.sort(new ScheduleByLocalDateComparator());
            mzCache.setSchedules(schedules);
        }
        catch (DataAccessResourceFailureException ex)
        {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Przekroczono limit połączeń z bazą danych.");
        }
        finally {
            return mzCache;
        }
    }
}