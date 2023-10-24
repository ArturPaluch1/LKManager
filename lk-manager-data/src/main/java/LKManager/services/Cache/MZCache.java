package LKManager.services.Cache;

import LKManager.DAO.TerminarzDAOImpl;
import LKManager.DAO.UserDAOImpl;
import LKManager.LK.Comparators.scheduleIdComparator;
import LKManager.LK.Tabela;
import LKManager.LK.Terminarz;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter

public class MZCache {

  private final TerminarzDAOImpl terminarzDAO;
    private  Tabela tabela;
    private  List<UserData> users= new ArrayList<>();
    private Terminarz lastSchedule;
    private List<Terminarz> terminarze= new ArrayList<>();

    public MZCache(TerminarzDAOImpl terminarzDAO) {


        this.terminarzDAO = terminarzDAO;
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
    public  List<Terminarz>getSchedulesFromCacheOrDatabase()
    {
        List<Terminarz> schedules;
        if(this.terminarze.size()!=0)
        {
            schedules=this.terminarze;
        }
        else {
            schedules = terminarzDAO.findAll();
        }
        return schedules;
    }
public Terminarz findChosenScheduleByScheduleNameFromCacheOrDatabase(String chosenSchedule)
{
    Terminarz schedule=null;
    if(this.terminarze.size()!=0)
    {
        System.out.println("in cache");
        schedule=this.terminarze.stream().filter(a->a.getName().equals(chosenSchedule)).collect(Collectors.toList()).get(0);
    }
    else {
        System.out.println("in from db");
        schedule = terminarzDAO.findByTerminarzName(chosenSchedule);
    }
    return schedule;
}

public Terminarz findLastScheduleByIdFromCacheOrDatabase()
{
    Terminarz schedule=null;
    if(this.terminarze.size()!=0)
    {
        System.out.println("in cache last");
        schedule=this.terminarze.stream().sorted(new scheduleIdComparator()) .collect(Collectors.toList()).get(0);
    }
    else {
        System.out.println("in from db last");
        schedule = terminarzDAO.findLastById();
    }
    return schedule;
}


public void addSchedule(Terminarz schedule)
{
    this.terminarze.add(schedule);
}
    public void addUser(UserData user)
    {

        this.users.add(user);
    }
//setters


    public void setUsers(List<UserData> users) {
        this.users = users;
    }

    public void setTerminarze(List<Terminarz> terminarze) {
        this.terminarze = terminarze;
    }
}
