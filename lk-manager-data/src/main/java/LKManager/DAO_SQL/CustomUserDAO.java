package LKManager.DAO_SQL;

import LKManager.model.UserMZ.UserData;
import org.springframework.stereotype.Repository;
@Repository
public interface CustomUserDAO  {


    //@Transactional
    UserData findByTeamId(int id) ;//throws IOException, ParserConfigurationException, SAXException, JAXBException;
    UserData saveUser(UserData user);


   //  List<UserData> findAll(boolean isDeleted);




   // void deleteById(Long id);



/*    List<UserData> findUsersFromCache_NotDeletedWithPause();

   List<UserData> findUsersFromCache_NotDeletedWithoutPause();

    List<UserData> findUsersFromCache_All();*/

 /*   List<UserData> findNotDeletedUsers();
*/


     void delete(UserData object) ;

    void deleteById(Long id);



}
