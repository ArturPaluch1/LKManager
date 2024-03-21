package LKManager.services;

import LKManager.model.Schedule;
import LKManager.model.MatchesMz.Match;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

   Schedule utworzTerminarzWielodniowy(LocalDate data, List<String> grajki, String nazwa  ) throws DatatypeConfigurationException;
    Schedule utworzTerminarzJednodniowy(LocalDate data, List<String> mecze, String nazwa );

   Schedule wczytajTerminarz(String sciezka) throws JAXBException;

     Schedule findByIdWithRoundsMatchesUsersAndTeams(long scheduleId);

    Schedule getSchedule_ByName(String scheduleName);



    Schedule getSchedule_ById(long id);

    void deleteSchedule(String scheduleToDeleteName);

    Schedule getSchedule_TheNewest();

    List<Match> getAllMatchesOfSchedule(Schedule schedule);
    List<Schedule> getSchedules();

    void aktualizujTerminarz(Schedule schedule, String nazwaPliku);



}
