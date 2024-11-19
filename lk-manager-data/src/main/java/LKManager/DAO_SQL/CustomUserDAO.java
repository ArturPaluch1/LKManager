package LKManager.DAO_SQL;

import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.User;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserDAO  {


    //@Transactional
    MZUserData findMZUserByTeamId(int id) ;//throws IOException, ParserConfigurationException, SAXException, JAXBException;


    MZUserData saveMZUser(MZUserData mzUser);

    User saveUser(User user);
   //  List<UserData> findAll(boolean isDeleted);




   // void deleteById(Long id);



/*    List<UserData> findUsersFromCache_NotDeletedWithPause();

   List<UserData> findUsersFromCache_NotDeletedWithoutPause();

    List<UserData> findUsersFromCache_All();*/

 /*   List<UserData> findNotDeletedUsers();
*/


     void deleteUser(User object) ;

    void deleteUserById(Long id);


   }
