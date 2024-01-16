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


    void aktualizujTerminarz(Schedule schedule, String nazwaPliku);
}
