package LKManager.services;

import LKManager.model.UserMZ.MZUserData;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface LKUserService   {

   
    MZUserData dodajGraczaDoXML(MZUserData gracz) throws JAXBException, IOException, ParserConfigurationException, SAXException;
    boolean usunGraczaZXML(MZUserData gracz) throws JAXBException, ParserConfigurationException, IOException, SAXException;
    List<MZUserData> zapiszGraczyDoXML(List<String>gracze) throws JAXBException, IOException, ParserConfigurationException, SAXException;
    List<MZUserData>  wczytajGraczyZXML();

    List<MZUserData>  wczytajGraczy();


}
