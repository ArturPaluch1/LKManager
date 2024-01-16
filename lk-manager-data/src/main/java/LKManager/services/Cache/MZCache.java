package LKManager.services.Cache;

import LKManager.DAO.ScheduleDAO;
import LKManager.LK.Comparators.scheduleIdComparator;
import LKManager.LK.Table;
import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Repository
@Transactional
public class MZCache {

  //  private final ScheduleDAO customScheduleDAOimpl;
    private final ScheduleDAO scheduleDAO;
    private Table table = null;
    private List<UserData> users = new ArrayList<>();
    private Schedule lastSchedule;
    private List<Schedule> schedules;
    private List<Match> matches;

   // ScheduleDAO customScheduleDAOImpl,
    public MZCache( ScheduleDAO scheduleDAO) {


        //this.customScheduleDAOimpl = customScheduleDAOImpl;
        this.scheduleDAO = scheduleDAO;
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
    @Transactional

    public List<Schedule> getSchedulesFromCacheOrDatabase() {
        List<Schedule> s;
        if (this.schedules != null) {
            s = this.schedules;
        } else {
            System.out.println("getSchedulesFromCacheOrDatabase ->findall db");
            //   s= terminarzDAO.findAll();
       /*    Hibernate.initialize(s.get(0).getRundy());
            for (var runda:s.get(0).getRundy()
                 ) {
             // Hibernate.initialize(runda.getMecze());
                for (var mecz:runda.getMecze()
                     ) {
                    if(!Hibernate.isInitialized(mecz.getUser()))
                    {

                        Hibernate.initialize(mecz.getUser());
                    }
                    if(!Hibernate.isInitialized(mecz.getopponentUser()))
                    {
                        Hibernate.initialize(mecz.getopponentUser());
                    }
                 //   Hibernate.initialize(mecz.getUser().getTeamlist());

                //    Hibernate.initialize(mecz.getopponentUser().getTeamlist());
                }
            }*/

            //s=terminarzDAOimpl.findAll1();
            s = scheduleDAO.findByIdAndFetchRolesEagerly();
            if (s.size() != 0) {

                this.setSchedules(s);

            }
        }
        return s;
    }

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
}
