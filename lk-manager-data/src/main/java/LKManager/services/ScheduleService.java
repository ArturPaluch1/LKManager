package LKManager.services;

import LKManager.model.RecordsAndDTO.*;
import LKManager.model.Schedule;
import LKManager.model.Table;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    CreateScheduleResult createMultiDaySchedule(LocalDate data, List<String> grajki, String nazwa , ScheduleType scheduleType ) throws DatatypeConfigurationException;
    CreateScheduleResult createOneDayShedule(LocalDate data, List<String> mecze, String nazwa , ScheduleType scheduleType );

   Schedule wczytajTerminarz(String sciezka) throws JAXBException;

     Schedule findByIdWithRoundsMatchesUsersAndTeams(long scheduleId);

    ScheduleDTO getSchedule_ByName(String scheduleName);



    ScheduleDTO getSchedule_ById(long id);

    boolean deleteSchedule(String scheduleToDeleteName);

    ScheduleDTO getSchedule_TheNewest();

    <T> List<MatchDTO> getAllMatchesOfSchedule(T schedule);
    List<Schedule> getSchedules();

    void updateSchedule(Schedule schedule, String nazwaPliku);

    boolean updateSchedule(Schedule schedule);
    List<ScheduleNameDTO> getScheduleNames();

    public CreateScheduleResult createSwissScheduleWithPlayerNames(LocalDate startDate, List<String> signedPlayers, String scheduleName, Integer roundsNumber , ScheduleType scheduleType);
    public CreateScheduleResult createSwissScheduleWithPlayerData(LocalDate startDate, List<UserDataDTO> signedPlayers, String scheduleName, Integer roundsNumber , ScheduleType scheduleType) ;
    public void calculateNextRoundOfSwissSchedule(ScheduleDTO schedule, Table table);



}
