package LKManager.services;

import LKManager.LK.Gracze;
import LKManager.model.UserMZ.MZUserData;
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
    public MZUserData dodajGraczaDoXML(MZUserData gracz) throws JAXBException, IOException, ParserConfigurationException, SAXException {
      //sprawdzenie czy juz był dodany


        var grajek= mzUserService.findByUsernameInManagerzone(gracz.getUsername());

        MZUserData user = new MZUserData();
        user.setUsername(gracz.getUsername());
        user.setTeamlist(gracz.getTeamlist());
        user.setMZuser_id(grajek.getMZuser_id());
        user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
        user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());

        List<MZUserData> obecniGracze= wczytajGraczyZXML();
        if(obecniGracze==null)
        {
            obecniGracze= new ArrayList<MZUserData>();
        }
        obecniGracze.add(user);
        zapiszDoXML(obecniGracze);
        return user;
    }

    public MZUserData dodajGraczaDoXML(String gracz) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        //sprawdzenie czy juz był dodany


        var grajek= mzUserService.findByUsernameInManagerzone(gracz);

        MZUserData user = new MZUserData();
        user.setUsername(gracz);
        //user.setTeamlist();
        user.setMZuser_id(grajek.getMZuser_id());
        user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
        user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());

        List<MZUserData> obecniGracze= wczytajGraczyZXML();
        if(obecniGracze==null)
        {
            obecniGracze= new ArrayList<MZUserData>();
        }
        obecniGracze.add(user);
        zapiszDoXML(obecniGracze);
        return user;
    }
    @Override
    public boolean usunGraczaZXML(MZUserData gracz) throws JAXBException, ParserConfigurationException, IOException, SAXException {
        var gracze =wczytajGraczyZXML();
      var grajek=  gracze.stream().filter(a->a.getMZuser_id().equals(gracz.getMZuser_id())).collect(Collectors.toList());
        gracze.remove(grajek.get(0));
        zapiszDoXML(gracze);

        return true;
    }
    public boolean usunGraczaZXML(String gracz) throws JAXBException, ParserConfigurationException, IOException, SAXException {

       var graczMZ= mzUserService.findByUsernameInManagerzone(gracz);
       if(graczMZ!= null)
       {
           var gracze =wczytajGraczyZXML();
           var grajek=  gracze.stream().filter(a->a.getMZuser_id().equals(graczMZ.getMZuser_id())).collect(Collectors.toList());
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
    public List<MZUserData> zapiszGraczyDoXML(List<String>gracze) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        List<MZUserData> listaGraczy= new ArrayList<>();
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
    MZUserData user = new MZUserData();
    user.setUsername(nick);
    user.setTeamlist(grajek.getTeamlist());
    user.setMZuser_id(grajek.getMZuser_id());
    user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
    user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());
    listaGraczy.add(user);
}

        }

        List<MZUserData> obecniGracze= wczytajGraczyZXML();
        if(obecniGracze==null)
        {
            obecniGracze= new ArrayList<MZUserData>();
        }
        obecniGracze.addAll(listaGraczy);
        zapiszDoXML(obecniGracze);
        return wczytajGraczyZXML();
    }

    @Override
    public List<MZUserData> wczytajGraczyZXML() {


        return jaxbXMLToObject("gracze.xml");
    }

    @Override
    public List<MZUserData> wczytajGraczy() {



        return null;
    }

    public void zapiszDoXML(  List<MZUserData> gracze) throws ParserConfigurationException, IOException, SAXException, JAXBException {

        jaxbObjectToXML(gracze);
    }

    private List<MZUserData> jaxbXMLToObject(String skladPath) {
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
    private void jaxbObjectToXML(List<MZUserData>  gracze) {

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
