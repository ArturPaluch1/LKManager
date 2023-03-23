package LKManager.services;

import LKManager.model.MatchesMz.Match;
import LKManager.model.MatchesMz.Matches;
import LKManager.services.MatchService;
import LKManager.services.URLs;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Set;

import static LKManager.services.URLs.URLtoMatches;

@Service
public class MatchServiceImpl implements MatchService, Serializable {

    @Override
    public Set<Match> findAll() {

        return null;
    }

    @Override
    public Match findById(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        return null;
    }

    @Override
    public Match save(Match object) {
        return null;
    }

    @Override
    public void delete(Match object) {

    }

    @Override
    public void deleteById(Long id) {

    }


    public Matches findPlayedByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        URL url=  new URLs( ). MakePlayedMatchesURL(username);
    Matches matches=   URLtoMatches(url);
        return matches;
    }


    public Matches findPlannedByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        URL url=  new URLs( ). MakeOngoingMatchesURL(username);
        Matches matches=   URLtoMatches(url);
        return matches;
    }
}
