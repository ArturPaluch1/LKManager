package LKManager.services.map;

import LKManager.model.UserMZ.UserData;
import LKManager.services.TMTeamService;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

public class TMTeamServiceMap implements TMTeamService {


    @Override
    public UserData findByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        return null;
    }

    @Override
    public Set<UserData> findAll() {
        return null;
    }

    @Override
    public UserData findById(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        return null;
    }

    @Override
    public Object save(Object object) {
        return null;
    }

    @Override
    public void delete(Object object) {

    }

    @Override
    public void deleteById(Object id) {

    }
}
