package LKManager.services;

import LKManager.model.UserMZ.MZUserData;
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
    public List<MZUserData> findAll() {
        return null;
    }

    @Override
    public MZUserData getMZUserDataById(Long player)  {
        URL url= null;


        try {
            url = URLs.MakeUserURLwithId(player);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            return URLs.URLtoUserData(url) ;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public MZUserData findByUsernameInManagerzone(String username) {

        URL url= null;
        try {
            url = URLs.MakeUserURLwithUsername(username);
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
    public MZUserData findByTeamId(int Teamid) throws IOException, ParserConfigurationException, SAXException, JAXBException {

URL url = URLs.MakeUserURLwithTeamId(Teamid);

        return URLs.URLtoUserData(url);

    }

    @Override
    public MZUserData save(MZUserData object) {
        return null;
    }

    @Override
    public void delete(MZUserData object) {

    }

    @Override
    public void deleteById(Long along) {

    }











}
