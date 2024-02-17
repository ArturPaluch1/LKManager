package LKManager.services;

import LKManager.model.UserMZ.UserData;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface UserService {

// UserData saveUser(UserData playerMZ);





    void deleteUser(String chosenUser, List<String> chosenUsers) throws JAXBException, IOException, ParserConfigurationException, SAXException;

    List<UserData>  findAllUsersFromCache();


     List<UserData> findUsers_NotDeletedWithoutPause();
    List<UserData> findUsersFromCache_NotDeletedWithPause();
    UserData AddUser(String userToAdd) throws JAXBException, IOException, ParserConfigurationException, SAXException;
   List< UserData> AddUsers(List<String> usersToAdd);
}


