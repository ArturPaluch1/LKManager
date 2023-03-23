package LKManager.services;

import LKManager.model.UserMZ.UserData;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Set;

@Service
public class MZUserServiceImpl implements MZUserService, Serializable {


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

        return URLs.URLtoUserData(url);

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
