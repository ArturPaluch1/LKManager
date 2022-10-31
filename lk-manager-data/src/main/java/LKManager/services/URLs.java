package LKManager.services;

import LKManager.model.MatchesMz.Matches;
import LKManager.model.UserMZ.ManagerZone_UserData;
import LKManager.model.UserMZ.UserData;
import LKManager.services.map.UserServiceMap;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
@Service
public  class URLs {

    private  UserService userService;

    public URLs() {

        this.userService = new UserServiceMap() {
        };
    }

    public static URL MakeUserURL(String username) throws MalformedURLException {
        return new URL( "http://www.managerzone.com/xml/manager_data.php?sport_id=1&username="+username);

    }

    public static URL MakeUserURL(int idTeam) throws MalformedURLException {
        return new URL( "http://www.managerzone.com/xml/manager_data.php?sport_id=1&team_id="+idTeam);

    }

    public static URL MakePlayedMatchesURL(int userId) throws MalformedURLException {
        return new URL( "http://www.managerzone.com/xml/team_matchlist.php?sport_id=1&team_id="+String.valueOf(userId)+"&match_status=1&limit=100");

    }
    public  URL MakePlayedMatchesURL(String username) throws IOException, ParserConfigurationException, JAXBException, SAXException {
URL przed =new URL( "http://www.managerzone.com/xml/team_matchlist.php?sport_id=1&team_id="+userService.findByUsername(username).getTeamlist().get(0).getTeamId().toString()+"&match_status=1&limit=100");
        return przed;

    }

    public static URL MakeOngoingMatchesURL(int userId) throws MalformedURLException {
        return new URL( "http://www.managerzone.com/xml/team_matchlist.php?sport_id=1&team_id="+String.valueOf(userId)+"&match_status=2&limit=100");

    }
    public  URL MakeOngoingMatchesURL(String username) throws IOException, ParserConfigurationException, JAXBException, SAXException {

        return new URL( "http://www.managerzone.com/xml/team_matchlist.php?sport_id=1&team_id="+userService.findByUsername(username).getTeamlist().get(0).getTeamId().toString()+"&match_status=2&limit=100");

    }


    public static UserData URLtoUserData(URL url) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        JAXBContext jaxbContext;
     /* try
      {*/
        jaxbContext = JAXBContext.newInstance (ManagerZone_UserData.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();


        //  URL url2 = y;


        ManagerZone_UserData user = (ManagerZone_UserData) jaxbUnmarshaller.unmarshal( url);
        //  user1 = (ManagerZone_UserData) jaxbUnmarshaller.unmarshal(doc);

    //    System.out.println(user);
        return user.getUserData();


    }

    public static Matches URLtoMatches(URL url) throws JAXBException, ParserConfigurationException, SAXException, IOException {

        //    Document doc = URLtoDocument(url);
        JAXBContext jaxbContext;
     /* try
      {*/
        jaxbContext = JAXBContext.newInstance (Matches.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();


        //  URL url2 = y;


        Matches matches = (Matches) jaxbUnmarshaller.unmarshal( url);

     //   System.out.println(matches.getMatches().get(0).getTeamlist().get(0).getTeamName());
        return matches;


    }

}
