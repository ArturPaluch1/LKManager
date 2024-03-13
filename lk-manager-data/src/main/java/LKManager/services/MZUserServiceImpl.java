package LKManager.services;

import LKManager.model.UserMZ.UserData;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class MZUserServiceImpl implements MZUserService, Serializable {


    @Override
    public List<UserData> findAll() {
        return null;
    }

    public  UserData findByUsername(String username) {

        URL url= null;
        try {
            url = URLs.MakeUserURL(username);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            return URLs.URLtoUserData(url) ;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public UserData findByTeamId(int Teamid) throws IOException, ParserConfigurationException, SAXException, JAXBException {

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
