package LKManager.services;

import LKManager.LK.Terminarz;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface WynikiService {
   // Terminarz wyswietlWyniki(Integer numerRundy ) ;
    void aktualizujWyniki(Integer runda,Terminarz terminarz, MatchService matchService, String nazwaPliku) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;

       //(plik z .xml)
    void zapiszDoXml(Terminarz terminarz, String nazwaPliku);

    // void WczytajZXml();

}
