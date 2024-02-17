package LKManager.DAO;

import LKManager.model.UserMZ.UserData;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
@Repository
public interface CustomUserDAO  {


    //@Transactional
    UserData findByTeamId(int id) ;//throws IOException, ParserConfigurationException, SAXException, JAXBException;



   //  List<UserData> findAll(boolean isDeleted);




   // void deleteById(Long id);

   //UserData save(UserData user);

    List<UserData> findUsersFromCache_NotDeletedWithPause();

   List<UserData> findUsersFromCache_NotDeletedWithoutPause();

    List<UserData> findUsersFromCache_All();

    List<UserData> findNotDeletedUsers();



     void delete(UserData object) throws JAXBException, IOException, ParserConfigurationException, SAXException;

    void deleteById(Long id);
}
