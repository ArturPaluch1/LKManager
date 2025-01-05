package LKManager.services;

//import LKManager.DAO.Exceptions.AddUserUserDatabaseAccessFailureException;
//import LKManager.DAO.Exceptions.DeleteUserUserDatabaseAccessFailureException;
//import LKManager.DAO.Exceptions.GetUsersUserDatabaseAccessFailureException;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.account.User;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.SignUpForm;
import LKManager.model.account.UserSettingsFormModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional
public interface UserService extends UserDetailsService {

// UserData saveUser(UserData playerMZ);
 void authenticateUser(User user);

    UserDataDTO getUserByUsername(String username);

    /**
     * Marks user entity or list of users with deleted mark. Updates Cache list
     * @param chosenUser
     * @param chosenUsers
     * @throws
     */
    void deactivateUser(String chosenUser, List<String> chosenUsers)  ;
    UserDataDTO activateUser(String username);
  //  List<UserData>  findAllUsersFromCache();


    /**
     * Gets all users without deleted marker except pause entity. Trying firstly from Cache, if there is no Cache trying to get users from a database
     * @return List UserData
     * @throws GetUsersUserDatabaseAccessFailureException
     */
   //  List<UserDataDTO> findUsers_NotDeletedWithoutPause();

    /**
     * Gets all users without deleted marker including pause entity. Trying firstly from Cache, if there is no Cache trying to get users from a database
     * @return List UserData
     * @throws
     */
  //  List<UserDataDTO> findUsers_NotDeletedWithPause();

    /*
    System.out.println("found users in db");
   String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
  return  userDataDTO = gsonService.jsonToList(userDataDTOString,UserDataDTO.class);

}
else //no users in redis nor in db
{
    return null;
}
}
else
{
return userDataDTO;
System.out.println("found users in redis");
}







//}

}
*/

    List<UserDataDTO> findAllMZUsers(boolean active, boolean withPause);

    /**
     * Adds user to database and to Cache
     *
     * @param userToAdd player's username
     * @return player's UserData object
     * @throws
     */
    UserDataDTO addUser(SignUpForm userToAdd) ;

    User getPauseObject();

    MZUserData getMZUserById(Long userId);
    // UserData findUserById(String pauza);


    //UserDataDTO findUserById(Integer i);

    MZUserData getMZUserDataByUsername(String username);

    User changeUserLeagueParticipation(UserSettingsFormModel userSettingsFormModel, LeagueParticipation leagueParticipation);

    public Optional<MZUserData> findMzUserById(Long userId);
    public UserDataDTO getUserById(Long id);

    boolean setMZUser(String user, String mzUsername);

    void setLeagueSignedUnsigned();

    boolean setUsersEmail(Long userId, String email);

    UserDataDTO addUser(User user);



   String getUsersEmail(Long id) throws Exception;

   void refreshActiveUsers();

   //  boolean removeLeagueParticipant(String mzUsername);


    //to sie nigdy nie wykonuje bo zawsze dodawany jest tylko jeden gracz na raz
 /*  List< UserData> AddUsers(List<String> usersToAdd)throws AddUsersUserDatabaseAccessFailureException;*/



}


