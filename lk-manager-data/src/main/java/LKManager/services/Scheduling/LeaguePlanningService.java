package LKManager.services.Scheduling;

import LKManager.DAO_SQL.LeagueParticipantsDAO;
import LKManager.DAO_SQL.RoundDAO;
import LKManager.model.RecordsAndDTO.ScheduleType;
import LKManager.model.Schedule;
import LKManager.model.ScheduleStatus;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.services.ScheduleService;
import LKManager.services.UserService;
import LKManager.services.UserSettingsService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.DatatypeConfigurationException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class LeaguePlanningService {
    private final ScheduleService scheduleService;
    private final RoundDAO roundDAO;
private final UserSettingsService userSettingsService;
private final LeagueParticipantsDAO leagueParticipantsDAO;
private final UserService userService;


    public void planUpcomingLeagueSchedule() {
        List<Schedule> leagueSchedules=	scheduleService.getSchedules().stream().filter(s->s.getName().contains("Liga")).collect(Collectors.toList());

        //if(	scheduleService.getSchedules().stream().filter(s->(s.getName().contains("Liga")&&s.getScheduleStatus().equals(ScheduleStatus.PLANNED))).collect(Collectors.toList()).size()==0)

        if(leagueSchedules.isEmpty()) //nie ma żadnych ligowych
        {
            LocalDate today = LocalDate.now();
            LocalDate nextTuesday = today.with(DayOfWeek.TUESDAY);

            // Jeśli dzisiaj jest już wtorek, to dodajemy 7 dni, aby uzyskać najbliższy przyszły wtorek
            if (today.getDayOfWeek() == DayOfWeek.TUESDAY) {
                nextTuesday = nextTuesday.plusDays(7);
            }

            //todo   1/2 tutaj i niżej chyba zrobić nie najbliższy wtorek, tylko sprawdzać czy nie jest zbyt blisko worku i wtedy tworzyć na jeszcze kolejny wtore ( w razie jakby render albo baza sql nie działała dłużej)
            scheduleService.planSchedule(nextTuesday, "Liga s.1", ScheduleType.standardSchedule);

        }
        else
        {
            //nie ma planned
            if(leagueSchedules.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst().isEmpty()) {



                //	int leagueSeasonNumber= extractNumber(leagueRounds.get(0).getSchedule().getName());
                Optional<Schedule> lastSchedule=	leagueSchedules.stream().max(Comparator.comparing(Schedule::getEndDate)).stream().findAny();
//todo 2/2
                scheduleService.planSchedule(lastSchedule.get().getEndDate().plusDays(7), "Liga s." + (extractNumber(lastSchedule.get().getName())+1), ScheduleType.standardSchedule);

            }
            else  //jest już planned
            {

            }
        }

/*

        if (scheduleService.getSchedules().stream().filter(s -> (s.getName().contains("Liga") && s.getScheduleStatus().equals(ScheduleStatus.PLANNED))).collect(Collectors.toList()).size() == 0) {

            List<Round> rounds = roundDAO.findLastRoundOFSchedulesOfGivenType(ScheduleType.standardSchedule);
            List<Round> leagueRounds = rounds.stream().filter(round -> round.getSchedule().getName().contains("Liga")).collect(Collectors.toList());
            if (leagueRounds.size() == 0)//nie było wcześniej schedule z ligą
            {
                LocalDate today = LocalDate.now();
                LocalDate nextTuesday = today.with(DayOfWeek.TUESDAY);

                // Jeśli dzisiaj jest już wtorek, to dodajemy 7 dni, aby uzyskać najbliższy przyszły wtorek
                if (today.getDayOfWeek() == DayOfWeek.TUESDAY) {
                    nextTuesday = nextTuesday.plusDays(7);
                }
                scheduleService.planSchedule(nextTuesday, "Liga s.1", ScheduleType.standardSchedule);

            } else {
                int leagueSeasonNumber = Integer.parseInt(leagueRounds.get(0).getSchedule().getName().substring(7).trim());


                scheduleService.planSchedule(leagueRounds.get(0).getDate().plusDays(7), "Liga s." + leagueSeasonNumber, ScheduleType.standardSchedule);
            }
        }*/
    }

//tu ustawić na dzień po wtorku


    //boolean temp po testach na void
 @Transactional
  @Scheduled(cron = "0 15,55 2 * * *")  //2:15, 2:55 codziennie
// @Scheduled(cron = "16 31,58 * * * *")
public void manageLeague()
{
    System.out.println("******starts manageleague********");

    List<Schedule> leagueSchedules=	scheduleService.getSchedules().stream().filter(s->s.getName().contains("Liga")).collect(Collectors.toList());


    Optional<Schedule> plannedSchedule=  leagueSchedules.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst();
    Optional<Schedule> ongoingLeague=	leagueSchedules.stream().filter(s->(s.getScheduleStatus().equals(ScheduleStatus.ONGOING))).findFirst();

    if(plannedSchedule.isPresent())
{
    System.out.println("- planned sh: "+plannedSchedule.get().getName());
    if(ongoingLeague.isPresent())
    {

        System.out.println("- ongoing sh: "+ongoingLeague.get().getName());

        if(checkIfFinishLeague(ongoingLeague.get())) {
            System.out.println("finishing");
            finishLeague(ongoingLeague.get());
            System.out.println("finished");
            System.out.println("checking whether to start the season");
if(checkIfStartPlannedLeague())
{
    System.out.println("next season starting");
    startPlannedLeague(plannedSchedule.get());

}


      //     manageLeague();

        }
    }
    else
    {
        System.out.println("- ongoing sh: is null");
        System.out.println("checking whether to start the season");
        if(checkIfStartPlannedLeague())
        {
            System.out.println("next season starting");
            startPlannedLeague(plannedSchedule.get());

        }

     /*   try {
            System.out.println("planned schedule->ongoing");

              scheduleService.updatePlannedSchedule(plannedSchedule.get(), leagueParticipantsDAO.findAll());

        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        System.out.println("deleting participants");
        leagueParticipantsDAO.deleteParticipantsByLeagueParticipation(LeagueParticipation.SIGNED);
        userService.setLeagueSignedUnsigned();
*/
       // startPlannedLeague();
       // manageLeague();

    }
}


else  //planned empty
{

    {
        System.out.println("- planned sh: is null");
        System.out.println("...planning");
        planUpcomingLeagueSchedule();
        manageLeague();
    }


   /* System.out.println("Planned not empty");
    if(ongoingLeague.isEmpty())//ongoing is empty
    {

    }
    else//ongoing exists
    {
        System.out.println("ongoing not empty");

            //temp
            return false;
        }
        else return true;*/

    }





 //   return true;


}



    @Transactional
    private void startPlannedLeague(Schedule plannedSchedule) {
        try {
         //   List<Schedule> leagueSchedules=	scheduleService.getSchedules().stream().filter(s->s.getName().contains("Liga")).collect(Collectors.toList());


          //  Optional<Schedule> plannedSchedule=  leagueSchedules.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst();

            if(plannedSchedule.getStartDate().isBefore(LocalDate.now()))
            {
                System.out.println("updating season start date");
         //       System.out.println("1");
                LocalDate newStartDate = LocalDate.now().plusDays(6);
                plannedSchedule.setStartDate(newStartDate);
            }
          //  System.out.println("2");
            System.out.println("updating planned schedule");
            scheduleService.updatePlannedSchedule(plannedSchedule,leagueParticipantsDAO.findAll());
            System.out.println("cleaning league participant list");
     leagueParticipantsDAO.deleteParticipantsByLeagueParticipation(LeagueParticipation.SIGNED);
            System.out.println("updating signs/subs in users");
     userService.setLeagueSignedUnsigned();
   //  manageLeague();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean checkIfStartPlannedLeague() {
        return leagueParticipantsDAO.count() >= 4 && LocalDate.now().getDayOfWeek().equals(DayOfWeek.WEDNESDAY);

    }
    private boolean checkIfFinishLeague(Schedule ongoingLeague) {
/*     if( LocalDateTime.now().isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(19,42,00)))&&
             LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(19,35,00))))//skończyła się liga, jest dzień po ostatniej kolejce
//if(1==1)
{
    return true;
}*/

        if(ongoingLeague.getEndDate().equals(LocalDate.now().minusDays(1)))//skończyła się liga, jest dzień po ostatniej kolejce
        {
      //      System.out.println("godzina < 21:35");
           return true;



       /*     //ustaw planned na ongoing
            try {
                setPlannedScheduleOngoing();
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException(e);
            }

*/
            //ustaw nowe planned
          //  planUpcomingLeagueSchedule();

        }
        else
        //    manageLeague();
            return false;
    }

    private void finishLeague(Schedule ongoingLeague) {
        ongoingLeague.setScheduleStatus(ScheduleStatus.FINISHED);
        System.out.println("ongoing status=finished");
        scheduleService.updateSchedule(ongoingLeague);
        System.out.println("ongoing =finished");
    }


    public void setPlannedScheduleOngoing() throws DatatypeConfigurationException {
        List<Schedule> leagueSchedules=	scheduleService.getSchedules().stream().filter(s->s.getName().contains("Liga")).collect(Collectors.toList());
      Optional<Schedule> plannedLeague=  leagueSchedules.stream().filter(s->s.getScheduleStatus().equals(ScheduleStatus.PLANNED)).findFirst();
        if(plannedLeague.isEmpty()) {
//todo  ???
        }
        else  //jest zaplanowany
        {
            plannedLeague.get().setScheduleStatus(ScheduleStatus.ONGOING);
            scheduleService.updatePlannedSchedule(plannedLeague.get(),leagueParticipantsDAO.findAll());
        }
    }

    private static int extractNumber(String scheduleName) {
        return Integer.parseInt(scheduleName.substring(7).trim());
    }

}