package LKManager.services;

import LKManager.LK.Terminarz;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

public interface TerminarzService  {

   Terminarz utworzTerminarzWielodniowy(LocalDate data, List<UserData> grajki, String nazwa  ) throws DatatypeConfigurationException;
    Terminarz utworzTerminarzJednodniowy(LocalDate data, List<UserData> mecze, String nazwa );

   Terminarz wczytajTerminarz(String sciezka) throws JAXBException;


    void aktualizujTerminarz(Terminarz terminarz, String nazwaPliku);
}
