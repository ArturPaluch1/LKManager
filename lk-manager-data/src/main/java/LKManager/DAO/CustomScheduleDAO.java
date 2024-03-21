package LKManager.DAO;

import LKManager.model.Schedule;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
public interface CustomScheduleDAO {




    Schedule save1(Schedule schedule);

    /*    void saveRound(Terminarz terminarz, int runda);*/
    //  List<Terminarz> findAll();
    void saveResults(Integer round, Schedule schedule1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;
    Schedule findByScheduleId(long id);
    Schedule findByScheduleName(String name);

    Schedule findLastById();

     void deleteByName(String objectName);


}
