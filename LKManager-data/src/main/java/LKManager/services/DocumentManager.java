package LKManager.services;

import LKManager.model.MatchesMz.Match;
import LKManager.model.MatchesMz.Matches;
import LKManager.model.UserMZ.ManagerZone_UserData;
import LKManager.model.UserMZ.UserData;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DocumentManager {

    public   Document URLtoDocument(URL url) throws IOException, ParserConfigurationException, SAXException {


        URLConnection urlConnection = url.openConnection();
        //urlConnection.addRequestProperty("Accept", "application/xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        return  db.parse(urlConnection.getInputStream());

    }



    }


