package LKManager.services;

import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.Round;
import LKManager.model.Schedule;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ResultsService {

 //   void aktualizujWyniki(Integer runda,Terminarz terminarz, MatchService matchService, String nazwaPliku) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;


    Round updateResults(Integer roundNumber, ScheduleDTO schedule) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException;


    Round editResults(ScheduleDTO schedule, Integer roundNumber, List<Long> matchIds, List<String> userMatchResults1, List<String> userMatchResults2, List<String> opponentMatchResults1, List<String> opponentMatchResults2);
    void saveToXML(Schedule schedule, String fileName);

    List<Round>  updateRoundResultsForDate(LocalDate date);

    void simulateResults(ScheduleDTO schedule, int roundNumber);
}
