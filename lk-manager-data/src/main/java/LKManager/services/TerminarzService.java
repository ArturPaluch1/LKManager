package LKManager.services;

import LKManager.LK.Terminarz;
import LKManager.model.UserMZ.UserData;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

public interface TerminarzService {

   void utworzTerminarz(XMLGregorianCalendar data, List<UserData> grajki  ) throws DatatypeConfigurationException;
    Terminarz wczytajTerminarz(String sciezka) throws JAXBException;


}
