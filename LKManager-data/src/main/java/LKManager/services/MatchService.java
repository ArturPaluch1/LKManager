package LKManager.services;

import LKManager.model.MatchesMz.Match;
import LKManager.model.MatchesMz.Matches;
import LKManager.model.UserMZ.UserData;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface MatchService extends CrudService<Match, Long> {
     Matches findPlannedByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException;
     Matches findPlayedByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException;
}
