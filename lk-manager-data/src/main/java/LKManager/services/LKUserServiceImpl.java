package LKManager.services;

import LKManager.LK.Gracze;
import LKManager.model.UserMZ.UserData;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LKUserServiceImpl implements LKUserService {

private final MZUserService mzUserService;

    public LKUserServiceImpl(MZUserService mzUserService) {
        this.mzUserService = mzUserService;
    }

    @Override
    public UserData dodajGraczaDoXML(UserData gracz) throws JAXBException, IOException, ParserConfigurationException, SAXException {
      //sprawdzenie czy juz był dodany


        var grajek= mzUserService.findByUsernameInManagerzone(gracz.getUsername());

        UserData user = new UserData();
        user.setUsername(gracz.getUsername());
        user.setTeamlist(gracz.getTeamlist());
        user.setUserId(grajek.getUserId());
        user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
        user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());

        List<UserData> obecniGracze= wczytajGraczyZXML();
        if(obecniGracze==null)
        {
            obecniGracze= new ArrayList<UserData>();
        }
        obecniGracze.add(user);
        zapiszDoXML(obecniGracze);
        return user;
    }

    public UserData dodajGraczaDoXML(String gracz) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        //sprawdzenie czy juz był dodany


        var grajek= mzUserService.findByUsernameInManagerzone(gracz);

        UserData user = new UserData();
        user.setUsername(gracz);
        //user.setTeamlist();
        user.setUserId(grajek.getUserId());
        user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
        user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());

        List<UserData> obecniGracze= wczytajGraczyZXML();
        if(obecniGracze==null)
        {
            obecniGracze= new ArrayList<UserData>();
        }
        obecniGracze.add(user);
        zapiszDoXML(obecniGracze);
        return user;
    }
    @Override
    public boolean usunGraczaZXML(UserData gracz) throws JAXBException, ParserConfigurationException, IOException, SAXException {
        var gracze =wczytajGraczyZXML();
      var grajek=  gracze.stream().filter(a->a.getUserId().equals(gracz.getUserId())).collect(Collectors.toList());
        gracze.remove(grajek.get(0));
        zapiszDoXML(gracze);

        return true;
    }
    public boolean usunGraczaZXML(String gracz) throws JAXBException, ParserConfigurationException, IOException, SAXException {

       var graczMZ= mzUserService.findByUsernameInManagerzone(gracz);
       if(graczMZ!= null)
       {
           var gracze =wczytajGraczyZXML();
           var grajek=  gracze.stream().filter(a->a.getUserId().equals(graczMZ.getUserId())).collect(Collectors.toList());
           gracze.remove(grajek.get(0));
           zapiszDoXML(gracze);
           // gracze.stream().
           return true;
       }
       else
       {
           return false;
       }

    }
    @Override
    public List<UserData> zapiszGraczyDoXML(List<String>gracze) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        List<UserData> listaGraczy= new ArrayList<>();
        for (var nick: gracze
        ) {
            var grajek= mzUserService.findByUsernameInManagerzone(nick);
if(grajek== null)
{
    // nie ma w mz takiego nicku
    return null;
}
else
{
    UserData user = new UserData();
    user.setUsername(nick);
    user.setTeamlist(grajek.getTeamlist());
    user.setUserId(grajek.getUserId());
    user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
    user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());
    listaGraczy.add(user);
}

        }

        List<UserData> obecniGracze= wczytajGraczyZXML();
        if(obecniGracze==null)
        {
            obecniGracze= new ArrayList<UserData>();
        }
        obecniGracze.addAll(listaGraczy);
        zapiszDoXML(obecniGracze);
        return wczytajGraczyZXML();
    }

    @Override
    public List<UserData> wczytajGraczyZXML() {


        return jaxbXMLToObject("gracze.xml");
    }

    @Override
    public List<UserData> wczytajGraczy() {



        return null;
    }

    public void zapiszDoXML(  List<UserData> gracze) throws ParserConfigurationException, IOException, SAXException, JAXBException {

        jaxbObjectToXML(gracze);
    }

    private List<UserData> jaxbXMLToObject(String skladPath) {
        Gracze gracze= null;
        try {
            JAXBContext ctx = JAXBContext.newInstance(Gracze.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            File file = new File("Data/gracze/"+skladPath);

            if (file.exists()) {

                gracze = (Gracze) unmarshaller.unmarshal(file);





                return gracze.getGracze();
            }
            else
            {
                return null;
            }

        } catch (JAXBException e) {
            return null;
        }


    }
    private void jaxbObjectToXML(List<UserData>  gracze) {

        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Gracze.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Store XML to File

            new File("Data/gracze").mkdir();
            File file = new File("Data/gracze/gracze.xml");


            //Writes XML file to file-system
            Gracze klasaGracze= new Gracze();
            klasaGracze.setGracze(gracze);
            jaxbMarshaller.marshal(klasaGracze, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
