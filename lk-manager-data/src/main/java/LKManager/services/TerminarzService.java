package LKManager.services;

import LKManager.LK.Terminarz;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

public interface TerminarzService {

   void utworzTerminarzWielodniowy(XMLGregorianCalendar data, List<UserData> grajki, String nazwa  ) throws DatatypeConfigurationException;
    void utworzTerminarzJednodniowy(XMLGregorianCalendar data, List<UserData> mecze, String nazwa );

   Terminarz wczytajTerminarz(String sciezka) throws JAXBException;


    void aktualizujTerminarz(Terminarz terminarz, String nazwaPliku);
}
