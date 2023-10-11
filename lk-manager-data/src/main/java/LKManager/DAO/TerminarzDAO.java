package LKManager.DAO;

import LKManager.LK.Terminarz;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface TerminarzDAO  {

    Terminarz save(Terminarz terminarz);

/*    void saveRound(Terminarz terminarz, int runda);*/
     List<Terminarz> findAll();
    void saveResults(Integer runda, Terminarz terminarz1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;
     Terminarz findByTerminarzId(long id);
    Terminarz findByTerminarzName(String name);

    Terminarz findLastById();
}
