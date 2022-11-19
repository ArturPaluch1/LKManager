package LKManager.services;

import LKManager.LK.SkladUPSG;
import LKManager.model.UserMZ.Team;
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
import java.util.Arrays;
import java.util.List;

@Service
public class TeamTM {

    private final UserService userService;
    private List<UserData> skladTM;
private List<UserData> calyUPSG;


    public TeamTM(UserService userService) {
        this.userService = userService;
        skladTM = new ArrayList<>();

    }

    public List<UserData> LoadCalyUPSG() throws ParserConfigurationException, JAXBException, SAXException, IOException {

        List<String> nicki = Arrays.asList("szczepinho", "mark_oh", "k0niak", "lapatrenera", "mrcszef", "paulikmaster", "kyo555", "kamilosin", "kloc213", "szuram",
                "ciosek_999", "kingsajz", "rejbonaldinho", "speedylfc", "harry84", "yaretzky", "piko66", "wredny", "czajas", "wwojtek80", "olborinho", "hetman_zmc", "bruno43",
                "hadriano", "tomaszewsky", "kozi69", "pawcio1980", "mnowak", "domodelu");
        List<UserData> skladUPSG= new ArrayList<>();
        for (var nick: nicki
             ) {
            var grajek= userService.findByUsername(nick);

            UserData user = new UserData();
            user.setUsername(nick);
            user.setTeamlist(new Team());
            user.setUserId(grajek.getUserId());
            user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
            user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());
            skladUPSG.add(user);
        }
return skladUPSG;
    }

    public List<UserData> LoadLigaPSG() throws ParserConfigurationException, JAXBException, SAXException, IOException {

        List<String> nicki = Arrays.asList/*("szczepinho", "mark_oh", "k0niak", "lapatrenera", "mrcszef", "paulikmaster", "kyo555", "kamilosin", "kloc213", "szuram",
                "ciosek_999", "kingsajz", "rejbonaldinho", "speedylfc", "harry84", "yaretzky", "piko66", "wredny", "czajas", "wwojtek80", "olborinho", "hetman_zmc", "bruno43",
                "hadriano", "tomaszewsky", "kozi69", "pawcio1980", "mnowak", "domodelu");
*/
                ("marc0888" ,"ciosek_999","mnowak","szuram" ,"harry84" ,"jerzykw","olborinho" ,"piko66",
                        "yarek", "kamilosin","kingsajz","domodelu","szczepinho","hetman_zmc", "wwojtek80");
        List<UserData> skladUPSG= new ArrayList<>();
        for (var nick: nicki
        ) {
            var grajek= userService.findByUsername(nick);

            UserData user = new UserData();
            user.setUsername(nick);
            user.setTeamlist(new Team());
            user.setUserId(grajek.getUserId());
            user.getTeamlist().get(0).setTeamName(grajek.getTeamlist().get(0).getTeamName());
            user.getTeamlist().get(0).setTeamId(grajek.getTeamlist().get(0).getTeamId());
            skladUPSG.add(user);
        }
        return skladUPSG;
    }

  public void zapiszUPSGDoXML() throws ParserConfigurationException, IOException, SAXException, JAXBException {
        SkladUPSG skladUPSG = new SkladUPSG(this.LoadLigaPSG());
      jaxbObjectToXML(skladUPSG);
    }

    public SkladUPSG  wczytajUPSGZXML()
    {
        return jaxbXMLToObject("lk-manager-web/src/main/java/LKManager/XMLData/skladUPSG.xml");
    }

    private SkladUPSG jaxbXMLToObject(String skladPath) {
        SkladUPSG sklad= null;
        try {
            JAXBContext ctx = JAXBContext.newInstance(SkladUPSG.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            File file = new File(skladPath);

            if (file.exists()) {

                sklad = (SkladUPSG) unmarshaller.unmarshal(file);




              //  System.out.println(terminarz1.getTerminarz());
                return sklad;
            }
            else
            {
                return null;
            }

        } catch (JAXBException e) {
            return null;
        }


    }
    private void jaxbObjectToXML(SkladUPSG calySklad) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(SkladUPSG.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Store XML to File
            File file = new File("lk-manager-web/src/main/java/LKManager/XMLData/skladUPSG.xml");

            //Writes XML file to file-system
            jaxbMarshaller.marshal(calySklad, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public List<UserData> LoadTMRzeszow() {
        UserData yarek = new UserData();
        yarek.setUsername("yarek");
        yarek.setTeamlist(new Team());
        yarek.getTeamlist().get(0).setTeamName("Przemysl");

        UserData kingsajz = new UserData();
        kingsajz.setUsername("kingsajz");
        kingsajz.setTeamlist(new Team());
        kingsajz.getTeamlist().get(0).setTeamName("Wisła Kraków Old Boys");
        // kingsajz.setTeam("Wisła Kraków Old Boys");

        UserData jerzykw = new UserData();
        jerzykw.setUsername("jerzykw");
        jerzykw.setTeamlist(new Team());
        jerzykw.getTeamlist().get(0).setTeamName("FC Harnaś Sanok");
        //  jerzykw.setTeam("FC Harnaś Sanok");

        UserData marc0888 = new UserData();
        marc0888.setUsername("marc0888");
        marc0888.setTeamlist(new Team());
        marc0888.getTeamlist().get(0).setTeamName("SMOCZANKA MIELEC");
        //  marc0888.setTeam("SMOCZANKA MIELEC");

        skladTM.add(yarek);
        skladTM.add(kingsajz);
        skladTM.add(jerzykw);
        skladTM.add(marc0888);
        return  skladTM;
    }

    public void addTeam(String username, String teamName)
    {
        UserData tempUser = new UserData();
        tempUser.setUsername(username);
        tempUser.setTeamlist(new Team());
        tempUser.getTeamlist().get(0).setTeamName(teamName);


        skladTM.add(tempUser);
    }

    public List<UserData> LoadLKUPSGV() {
        UserData piko66 = new UserData();
        piko66.setUsername("piko66");
        piko66.setTeamlist(new Team());
        piko66.getTeamlist().get(0).setTeamName("KS Olimpia Warszawa");

        UserData wredny = new UserData();
        wredny.setUsername("wredny");
        wredny.setTeamlist(new Team());
        wredny.getTeamlist().get(0).setTeamName("JABOLOWE OFIARY");
        // kingsajz.setTeam("Wisła Kraków Old Boys");

        UserData czajas = new UserData();
        czajas.setUsername("czajas");
        czajas.setTeamlist(new Team());
        czajas.getTeamlist().get(0).setTeamName("Green Street Elite");
        //  jerzykw.setTeam("FC Harnaś Sanok");

        UserData wwojtek80 = new UserData();
        wwojtek80.setUsername("wwojtek80");
        wwojtek80.setTeamlist(new Team());
        wwojtek80.getTeamlist().get(0).setTeamName("Żuberia United");
        //  marc0888.setTeam("SMOCZANKA MIELEC");

        UserData olborinho = new UserData();
        olborinho.setUsername("olborinho");
        olborinho.setTeamlist(new Team());
        olborinho.getTeamlist().get(0).setTeamName("Spartans Lodz");

        skladTM.add(wwojtek80);
        skladTM.add(olborinho);
        skladTM.add(czajas);
        skladTM.add(wredny);
        skladTM.add(piko66);
        return  skladTM;
    }

}
