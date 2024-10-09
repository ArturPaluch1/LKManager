package LKManager.services;

import LKManager.model.MatchesMz.Match;
import LKManager.model.MatchesMz.Matches;
import LKManager.model.UserMZ.MZUserData;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import static LKManager.services.URLs.URLtoMatches;

@Service
public class MatchServiceImpl implements MatchService, Serializable {

    @Override
    public List<MZUserData> findAll() {

        return null;
    }

    @Override
    public Match findByTeamId(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException {
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


    public Matches findPlayedByUser(MZUserData user) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        URL url=  URLs. MakePlayedMatchesURL(user);
    Matches matches=   URLtoMatches(url);
        return matches;
    }


    public Matches findPlannedByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        URL url=  new URLs( ). MakeOngoingMatchesURL(username);
        Matches matches=   URLtoMatches(url);
        return matches;
    }
}
