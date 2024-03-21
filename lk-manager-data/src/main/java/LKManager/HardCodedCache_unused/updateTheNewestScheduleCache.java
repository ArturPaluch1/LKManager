package LKManager.HardCodedCache_unused;

import LKManager.DAO.ScheduleDAO;
import LKManager.model.Schedule;
import LKManager.HardCodedCache_unused.Cache.MZCache;
import LKManager.services.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public
class updateTheNewestScheduleCache extends MZCacheAction {
    @Autowired
    private MZCache mzCache;
    private final ScheduleService scheduleService;
    private final ScheduleDAO scheduleDAO;

    @Override @Transactional
    public MZCache update() {

        try {
            List<Schedule> schedules = null;
            if (mzCache.getSchedules().size() != 0) {
                schedules = mzCache.getSchedules();
            } else {
                schedules = scheduleDAO.findAllFetchRoundsEagerly();
            }


            List<Schedule> finalSchedules = schedules;
            var temps = mzCache.getSchedules().stream().filter(s -> s.getId() == finalSchedules.get(0).getId()).findFirst().get();



            temps.setRounds(scheduleService.findByIdWithRoundsMatchesUsersAndTeams(schedules.get(0).getId()).getRounds());

        }
        catch (Exception ex)
        {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Przekroczono limit połączeń z bazą danych.");
        }
        finally {
            return mzCache;
        }


    }
}