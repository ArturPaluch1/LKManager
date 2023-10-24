package LKManager.DAO;

import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface MeczDAO  {

    Match save(Match match);

    void saveRound(Terminarz terminarz, int runda);
     List<Match> findAll();
    void saveResults(Integer runda, Terminarz terminarz1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;
    Match findByMatchId(long id);
/*    Match findByTerminarzName(String name);*/

/*    Match findLastById();*/

    List<Match> findAllByTerminarzIdAndRundaId(long id, int i);
}
