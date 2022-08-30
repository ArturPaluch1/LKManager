package LKManager.services;

import LKManager.model.UserMZ.UserData;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface UserService extends CrudService<UserData,Long> {
     UserData findByUsername(String username) throws IOException, ParserConfigurationException, SAXException, JAXBException;
}
