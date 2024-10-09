package LKManager.services;


import LKManager.model.UserMZ.MZUserData;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface CrudService<T, Integer> {
    List<MZUserData> findAll();
    T findByTeamId(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException;
    T save (T object);
    void delete (T object) throws JAXBException, IOException, ParserConfigurationException, SAXException;
    void deleteById(Integer id);
}
