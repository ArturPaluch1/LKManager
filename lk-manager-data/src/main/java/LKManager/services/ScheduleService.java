package LKManager.services;

import LKManager.model.RecordsAndDTO.CreateScheduleResult;
import LKManager.model.RecordsAndDTO.MatchDTO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleNameDTO;
import LKManager.model.Schedule;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    CreateScheduleResult createMultiDaySchedule(LocalDate data, List<String> grajki, String nazwa  ) throws DatatypeConfigurationException;
    CreateScheduleResult createOneDayShedule(LocalDate data, List<String> mecze, String nazwa );

   Schedule wczytajTerminarz(String sciezka) throws JAXBException;

     Schedule findByIdWithRoundsMatchesUsersAndTeams(long scheduleId);

    ScheduleDTO getSchedule_ByName(String scheduleName);



    ScheduleDTO getSchedule_ById(long id);

    boolean deleteSchedule(String scheduleToDeleteName);

    ScheduleDTO getSchedule_TheNewest();

    <T> List<MatchDTO> getAllMatchesOfSchedule(T schedule);
    List<Schedule> getSchedules();

    void updateSchedule(Schedule schedule, String nazwaPliku);


    List<ScheduleNameDTO> getScheduleNames();
}
