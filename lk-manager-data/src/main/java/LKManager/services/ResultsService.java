package LKManager.services;

import LKManager.LK.Round;
import LKManager.LK.Schedule;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface ResultsService {

 //   void aktualizujWyniki(Integer runda,Terminarz terminarz, MatchService matchService, String nazwaPliku) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;


    void updateResults(Integer roundNumber, Round round, MatchService matchService, String scheduleName) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;

    void saveToXML(Schedule schedule, String fileName);



}
