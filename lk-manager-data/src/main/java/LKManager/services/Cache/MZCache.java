package LKManager.services.Cache;

import LKManager.DAO.RoundDAO;
import LKManager.DAO.ScheduleDAO;
import LKManager.LK.Comparators.scheduleIdComparator;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.LK.Table;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Component
@Transactional

@Setter

public class MZCache {

  //  private final ScheduleDAO customScheduleDAOimpl;
    private final ScheduleDAO scheduleDAO;
    private Table table;
    private List<UserData> users = new ArrayList<>();
  //  private Schedule lastSchedule;
    private List<Schedule> schedules;
    private List<Match> matches;
    private final RoundDAO roundDAO;

   // ScheduleDAO customScheduleDAOImpl,


    public MZCache(ScheduleDAO scheduleDAO, RoundDAO roundDAO) {
        this.scheduleDAO = scheduleDAO;
        this.roundDAO = roundDAO;
        this.table = new Table();
        this.users= new ArrayList<>();
        this.schedules = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    public Round updateRound(Round round, Schedule schedule)
    {
    Round round1 =    schedules.stream().filter(s -> s.getId() == schedule.getId()).findFirst().orElse(null).getRounds().stream().filter(r -> r.getId() == round.getId()).findFirst().orElse(null);
    round1.setMatches(round.getMatches());
   return round1;
       // Round roundInDB= roundDAO.findById(round.getId()).orElse(null);
    }
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    /*    public List<UserData> getUsersFromCacheOrDatabase() {
            List<UserData> gracze;
            if(this.users.size()!=0)
            {
                gracze=this.users;
            }
            else {
                gracze = userDAO.findAll(false);
            }
            return gracze;
        }*/
  /*  @Transactional

    public List<Schedule> getSchedulesFromCacheOrDatabase() {
        List<Schedule> s;
        if (this.schedules != null) {
            s = this.schedules;
        } else {
            System.out.println("getSchedulesFromCacheOrDatabase ->findall db");
            //   s= terminarzDAO.findAll();


            //s=terminarzDAOimpl.findAll1();
            s = scheduleDAO.findByIdAndFetchRoundsEagerly();
            if (s.size() != 0) {

                this.setSchedules(s);

            }
        }
        return s;
    }*/

    public Schedule findChosenScheduleByScheduleNameFromCacheOrDatabase(String chosenSchedule) {
        Schedule schedule = null;
        if (this.schedules.stream().filter(a -> a.getName().equals(chosenSchedule)).toList().size() != 0) {

            System.out.println("in cache");
            schedule = this.schedules.stream().filter(a -> a.getName().equals(chosenSchedule)).collect(Collectors.toList()).get(0);

        } else {
            System.out.println("in from db");
            schedule = scheduleDAO.findByScheduleName(chosenSchedule);

        }
        return schedule;
    }

    public Schedule findLastScheduleByIdFromCacheOrDatabase() {
        Schedule schedule = null;
        if (this.schedules.size() != 0) {
            System.out.println("in cache last");
            schedule = this.schedules.stream().sorted(new scheduleIdComparator()).collect(Collectors.toList()).get(0);
        } else {
            System.out.println("in from db last");
            schedule = scheduleDAO.findLastById();
        }
        return schedule;
    }


    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }

    public void addUser(UserData user) {

        this.users.add(user);
    }
//setters


    public void setUsers(List<UserData> users) {
        this.users = users;
    }


    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void updateScheduleMatchesCache(Schedule schedule) {
        //check if matches initialized
        if(schedule.checkMatchesInitialization()) {
            Schedule foundSchedule=this.getSchedules().stream().filter(a->a.getId()==schedule.getId()).findFirst().orElse(null);
            if(foundSchedule!=null)
            {
                foundSchedule.setRounds(schedule.getRounds());
                System.out.println("updateScheduleMatchesCache - success");
             }

        }

    }
}
