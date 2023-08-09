package LKManager.services;


import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;

public interface CrudService<T, Integer> {
    Set<T> findAll();
    T findByTeamId(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException;
    T save (T object);
    void delete (T object) throws JAXBException, IOException, ParserConfigurationException, SAXException;
    void deleteById(Integer id);
}
