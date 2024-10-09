package LKManager.DAO_SQL;

import LKManager.model.Schedule;
import LKManager.model.UserMZ.MZUserData;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Transactional
public interface CustomScheduleDAO {




    Schedule saveSchedule(Schedule schedule);

    /*    void saveRound(Terminarz terminarz, int runda);*/
    //  List<Terminarz> findAll();
    void saveResults(Integer round, Schedule schedule1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;

    @Query(" FROM Schedule s where s.id=:id")
    Schedule findByScheduleId(long id);

    @Query(" FROM Schedule s where s.name=:name")
    Schedule findByScheduleName(String name);

    Schedule findLastOngoingOrFinishedById();

     boolean deleteByName(String objectName);


     List<MZUserData> findAllParticipantsOfSchedule(String ScheduleName);

    void refresh(Schedule schedule);
}
