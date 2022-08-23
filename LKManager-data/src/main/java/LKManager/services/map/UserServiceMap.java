package LKManager.services.map;

import LKManager.model.UserMZ.UserData;
import LKManager.services.DocumentManager;
import LKManager.services.URLs;
import LKManager.services.UserService;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

@Service
public class UserServiceMap implements UserService {


    @Override
    public Set<UserData> findAll() {
        return null;
    }

    public  UserData findByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException {

      URL url=  URLs.MakeUserURL(username);

      return URLs.URLtoUserData(url) ;
    }
    @Override
    public UserData findById(int Teamid) throws IOException, ParserConfigurationException, SAXException, JAXBException {

URL url = URLs.MakeUserURL(Teamid);
   //     URL url=  MakeUserURL(id);
    //    Document doc = MakeDocumentFromUrl(url);
        return URLs.URLtoUserData(url); //ParseXMLtoUserData(doc);

    }

    @Override
    public UserData save(UserData object) {
        return null;
    }

    @Override
    public void delete(UserData object) {

    }

    @Override
    public void deleteById(Long along) {

    }











}
