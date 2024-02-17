package LKManager.services;

import LKManager.LK.Schedule;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

   Schedule utworzTerminarzWielodniowy(LocalDate data, List<UserData> grajki, String nazwa  ) throws DatatypeConfigurationException;
    Schedule utworzTerminarzJednodniowy(LocalDate data, List<UserData> mecze, String nazwa );

   Schedule wczytajTerminarz(String sciezka) throws JAXBException;

     Schedule findByIdWithRoundsMatchesUsersAndTeams(long scheduleId);

    Schedule getSchedule_ByName(String scheduleName);



    Schedule getSchedule_ById(long id);

    Schedule getSchedule_TheNewest();
    List<Schedule> getSchedules();

    void aktualizujTerminarz(Schedule schedule, String nazwaPliku);



}