package LKManager.services;

//import LKManager.DAO.Exceptions.AddUserUserDatabaseAccessFailureException;
//import LKManager.DAO.Exceptions.DeleteUserUserDatabaseAccessFailureException;
//import LKManager.DAO.Exceptions.GetUsersUserDatabaseAccessFailureException;
import LKManager.model.UserMZ.UserData;

import java.util.List;
public interface UserService {

// UserData saveUser(UserData playerMZ);


    /**
     * Marks user entity or list of users with deleted mark. Updates Cache list
     * @param chosenUser
     * @param chosenUsers
     * @throws DeleteUserUserDatabaseAccessFailureException
     */
    void deleteUser(String chosenUser, List<String> chosenUsers)  ;

  //  List<UserData>  findAllUsersFromCache();


    /**
     * Gets all users without deleted marker except pause entity. Trying firstly from Cache, if there is no Cache trying to get users from a database
     * @return List UserData
     * @throws GetUsersUserDatabaseAccessFailureException
     */
     List<UserData> findUsers_NotDeletedWithoutPause();

    /**
     * Gets all users without deleted marker including pause entity. Trying firstly from Cache, if there is no Cache trying to get users from a database
     * @return List UserData
     * @throws GetUsersUserDatabaseAccessFailureException
     */
    List<UserData> findUsers_NotDeletedWithPause();
    /** Adds user to database and to Cache
     * @param  userToAdd player's username
     * @return player's UserData object
     * @throws AddUserUserDatabaseAccessFailureException
     */
    UserData AddUser(String userToAdd) ;

    //to sie nigdy nie wykonuje bo zawsze dodawany jest tylko jeden gracz na raz
 /*  List< UserData> AddUsers(List<String> usersToAdd)throws AddUsersUserDatabaseAccessFailureException;*/



}


