package LKManager.services;

import LKManager.model.UserMZ.UserData;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface LKUserService   {

   
    UserData dodajGraczaDoXML(UserData gracz) throws JAXBException, IOException, ParserConfigurationException, SAXException;
    boolean usunGraczaZXML(UserData gracz) throws JAXBException, ParserConfigurationException, IOException, SAXException;
    List<UserData> zapiszGraczyDoXML(List<String>gracze) throws JAXBException, IOException, ParserConfigurationException, SAXException;
    List<UserData>  wczytajGraczyZXML();
}
