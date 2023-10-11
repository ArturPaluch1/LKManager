package LKManager.services;

import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface WynikiService {

 //   void aktualizujWyniki(Integer runda,Terminarz terminarz, MatchService matchService, String nazwaPliku) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;


    void aktualizujWyniki(Integer roundNumber, Runda round, MatchService matchService, String scheduleName) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;

    void zapiszDoXml(Terminarz terminarz, String nazwaPliku);



}
