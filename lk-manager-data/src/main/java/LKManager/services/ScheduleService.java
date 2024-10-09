package LKManager.services;

import LKManager.model.*;
import LKManager.model.RecordsAndDTO.*;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;
@Transactional
public interface ScheduleService {

    CreateScheduleResult createMultiDaySchedule(LocalDate data, List<String> grajki, String nazwa , ScheduleType scheduleType, ScheduleStatus scheduleStatus) throws DatatypeConfigurationException;



    CreateScheduleResult updatePlannedSchedule(Schedule schedule, List<LeagueParticipants> chosenPlayers) throws DatatypeConfigurationException, DatatypeConfigurationException;

    @Transactional
    CreateScheduleResult createOneDayShedule(LocalDate data, List<String> chosenPlayers, String scheduleName, ScheduleType scheduleType, ScheduleStatus status);

    Schedule wczytajTerminarz(String sciezka) throws JAXBException;

     Schedule findByIdWithRoundsMatchesUsersAndTeams(long scheduleId);

    ScheduleDTO getSchedule_ByName(String scheduleName);



    ScheduleDTO getSchedule_ById(long id);

    boolean deleteSchedule(String scheduleToDeleteName);

    ScheduleDTO getSchedule_TheNewestOngoingOrFinished();

    <T> List<MatchDTO> getAllMatchesOfSchedule(T schedule);
    List<Schedule> getSchedules();

    void updateSchedule(Schedule schedule, String nazwaPliku);

    boolean updateSchedule(Schedule schedule);
    List<ScheduleNameDTO> getScheduleNamesOngoingOrFinished();

    public CreateScheduleResult createSwissScheduleWithPlayerNames(LocalDate startDate, List<String> signedPlayers, String scheduleName, Integer roundsNumber , ScheduleType scheduleType, ScheduleStatus scheduleStatus);
  //  public CreateScheduleResult createSwissScheduleWithPlayerData(LocalDate startDate, List<UserDataDTO> signedPlayers, String scheduleName, Integer roundsNumber , ScheduleType scheduleType, ScheduleStatus scheduleStatus) ;
    public void calculateNextRoundOfSwissSchedule(ScheduleDTO schedule, Table table);
   // public CreateScheduleResult planSchedule(LocalDate startDate, LocalDate endDate, String scheduleName, ScheduleType scheduleType);
    public CreateScheduleResult planSchedule(LocalDate startDate,  String scheduleName, ScheduleType scheduleType);
  //  boolean addLeagueParticipant(User  user);
//public List<LeagueParticipants> getLeagueParticipants();

}
