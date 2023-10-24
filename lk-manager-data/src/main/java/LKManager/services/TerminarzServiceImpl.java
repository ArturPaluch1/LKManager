package LKManager.services;

import LKManager.DAO.TerminarzDAOImpl;
import LKManager.DAO.UserDAOImpl;
import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service

public class TerminarzServiceImpl implements TerminarzService {


private TerminarzDAOImpl terminarzDAOimpl;

    public TerminarzServiceImpl(TerminarzDAOImpl terminarzDAOimpl) {
        this.terminarzDAOimpl = terminarzDAOimpl;
    }

    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
private MZCache mzCache;
    @Override
    public Terminarz utworzTerminarzWielodniowy(LocalDate data, List<UserData> grajki, String nazwa) throws DatatypeConfigurationException {

        ////////////////////////////////////////////////////////
        dodajPauzeDlaParzystosci(grajki);
/////////////////podzial grajkow na pol  /////////////////
        var grajkiA = grajki.subList(0, (grajki.size()) / 2);
        var grajkiB = grajki.subList(grajki.size() / 2, grajki.size());


        Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
        ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);

        List<Runda> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////
        for (int j = 1; j < grajki.size(); j++) {
            /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
            Runda runda;
            if (j != 1) {
             /*   LocalDate tempDate= LocalDate.of(calyTerminarz.get(calyTerminarz.size() - 1).getData().getYear(),
                        calyTerminarz.get(calyTerminarz.size() - 1).getData().getMonth(),
                        calyTerminarz.get(calyTerminarz.size() - 1).getData().getDay());*/
         /*       XMLGregorianCalendar tempData = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                tempData.setYear(calyTerminarz.get(calyTerminarz.size() - 1).getData().getYear());
                tempData.setMonth(calyTerminarz.get(calyTerminarz.size() - 1).getData().getMonth());
                tempData.setDay(calyTerminarz.get(calyTerminarz.size() - 1).getData().getDay());
                tempData.add(d);
*/
                runda = new Runda(j, calyTerminarz.get((calyTerminarz.size()-1)).getData().plusDays(7l));
            } else {
                runda = new Runda(j, data);
            }

            //////////ustalanie par /////////////////////////////
            for (int i = 0; i < grajkiA.size(); i++) {

                var para = listyGrajkow.getGrajkiA().get(i).getUsername() + " - " + listyGrajkow.getGrajkiB().get(i).getUsername();
                System.out.println(para);

                var tempMatch = new Match();
               tempMatch.setUser(listyGrajkow.getGrajkiA().get(i));
                tempMatch.setopponentUser(listyGrajkow.getGrajkiB().get(i));
                tempMatch.setDateDB(data);
/*tempMatch.setDate(runda.getDateTimeItem());
tempMatch.setDateDB(runda.getDateTimeItem());
*/
                runda.getMecze().add(tempMatch);

            }

  //runda.setData(data);
  //runda.setDateTimeItem();
runda.setPlayed(false);
            calyTerminarz.add(runda);
            System.out.println("=======" + runda.getNr() + " === " + runda.getData());
            listyGrajkow.przesunListy();

        }





        /////////////// zapis termnarza do xml    //////////////////////
       Terminarz terminarz = new Terminarz(calyTerminarz);



 //       jaxbObjectToXML(terminarz,nazwa);

/////////////////////////////////////////////
// zapis  sql
terminarz.setName(nazwa);
//terminarz.getRundy().get(0).getTerminarz()

return terminarz;
//////////////
    }

    @Override
    public Terminarz utworzTerminarzJednodniowy(LocalDate data, List<UserData> mecze, String nazwa) {


        ////////////////////////////////////////////////////////
   //     dodajPauzeDlaParzystosci(grajki);
/////////////////podzial grajkow na pol  /////////////////
  /*      var grajkiA = grajki.subList(0, (grajki.size()) / 2);
        var grajkiB = grajki.subList(grajki.size() / 2, grajki.size());


        Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
        ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);
*/
        List<Runda> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////

            /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
            Runda runda;

                runda = new Runda(1, data);

        for (int i = 0; i < mecze.size(); i++) {
            if(i%2!=0)
            {
int yy=0;
            }
            else
            {
                var tempMatch = new Match();
               tempMatch.setDateDB(data);
                tempMatch.setUser(mecze.get(i));
                tempMatch.setopponentUser(mecze.get(i+1));
                runda.getMecze().add(tempMatch);
            }


        }
        runda.setPlayed(false);
        calyTerminarz.add(runda);
        System.out.println("=======" + runda.getNr() + " === " + runda.getData());






        //////////ustalanie par /////////////////////////////
  /*          for (int i = 0; i < grajkiA.size(); i++) {

                var para = listyGrajkow.getGrajkiA().get(i).getUsername() + " - " + listyGrajkow.getGrajkiB().get(i).getUsername();
                System.out.println(para);

                var tempMatch = new Match();
                tempMatch.setUser(listyGrajkow.getGrajkiA().get(i));
                tempMatch.setopponentUser(listyGrajkow.getGrajkiB().get(i));



                runda.getMecze().add(tempMatch);

            }
            runda.setStatus(Runda.status.nierozegrana);
            calyTerminarz.add(runda);
            System.out.println("=======" + runda.getNr() + " === " + runda.getData());
            listyGrajkow.przesunListy();



*/


//todo do usuniecia po przejsciu na sql
        /////////////// zapis termnarza do xml    //////////////////////
 /*       Terminarz terminarz = new Terminarz(calyTerminarz);
        jaxbObjectToXML(terminarz,nazwa);
*/
/////////////////////////////////////////////
        Terminarz terminarz = new Terminarz(calyTerminarz);
        terminarz.setName(nazwa);
terminarzDAOimpl.save(terminarz);

return terminarz;



    }

    @Override
    public Terminarz wczytajTerminarz(String sciezka) throws JAXBException {
        return jaxbXMLToObject(sciezka);
    }



    protected void dodajPauzeDlaParzystosci(List<UserData> grajki) {
        if (grajki.size() % 2 != 0) {
            UserData tempuser = new UserData();
            tempuser.setUserId(0);
            tempuser.setUsername("pauza");
            Team tempTeam= new Team();
            tempTeam.setTeamName(" ");
            tempTeam.setTeamId(0);
            List<Team> tempTeams= new ArrayList<>();
            tempTeams.add(tempTeam);
            tempuser.setTeamlist(tempTeams);
            grajki.add(tempuser);

          userDAO.save(tempuser);
          if(mzCache.getUsers().size()!=0)
          mzCache.addUser(tempuser);

        }
    }
    @Override
    public void aktualizujTerminarz(Terminarz terminarz, String nazwaPliku)
    {
        try {jaxbObjectToXML(terminarz,nazwaPliku);

        }
        catch (Exception e){

    }
    }



    protected void jaxbObjectToXML(Terminarz calyTerminarz, String nazwa) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Terminarz.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


            new File("Data/terminarze").mkdir();

            File file;
            if(nazwa.endsWith(".xml"))
            {
                 file = new File("Data/terminarze/"+nazwa);
            }
            else
            {
                 file = new File("Data/terminarze/"+nazwa+".xml");
            }



            //Writes XML file to file-system
            jaxbMarshaller.marshal(calyTerminarz, file);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }
    protected Terminarz jaxbXMLToObject(String terminarz) throws JAXBException {
        Terminarz terminarz1= null;

            JAXBContext ctx = JAXBContext.newInstance(Terminarz.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

        File file = new File(String.valueOf("Data/terminarze/"+terminarz));

          if (file.exists()) {

                terminarz1 = (Terminarz) unmarshaller.unmarshal(file);




                System.out.println(terminarz1.getRundy());
                return terminarz1;
            }
            else
            {
                return null;
            }




    }




    class ListyGrajkow {
        private List<UserData> grajkiPrzesunieteA;
        private List<UserData> grajkiPrzesunieteB;
        private List<UserData> grajkiA;
        private List<UserData> grajkiB;

        public List<UserData> getGrajkiPrzesunieteA() {
            return grajkiPrzesunieteA;
        }

        public void setGrajkiPrzesunieteA(List<UserData> grajkiPrzesunieteA) {
            this.grajkiPrzesunieteA = grajkiPrzesunieteA;
        }

        public List<UserData> getGrajkiPrzesunieteB() {
            return grajkiPrzesunieteB;
        }

        public void setGrajkiPrzesunieteB(List<UserData> grajkiPrzesunieteB) {
            this.grajkiPrzesunieteB = grajkiPrzesunieteB;
        }

        public List<UserData> getGrajkiA() {
            return grajkiA;
        }


        public List<UserData> getGrajkiB() {
            return grajkiB;
        }


        public ListyGrajkow(List<UserData> grajkiA, List<UserData> grajkiB) {
            this.grajkiA = grajkiA;
            this.grajkiB = grajkiB;
            grajkiPrzesunieteA = new ArrayList<>();
            grajkiPrzesunieteB = new ArrayList<>();
        }

        public void przesunListy() {
            //przesuwanie
            List<UserData> grajkiPrzesunieteA = new ArrayList<>();
            List<UserData> grajkiPrzesunieteB = new ArrayList<>();

            //A
            for (int i = 0; i < grajkiA.size(); i++) {
                if (i != 0 && i != 1) {
                    grajkiPrzesunieteA.add(grajkiA.get(i - 1));
                } else {
                    if (i == 0) {
                        grajkiPrzesunieteA.add(grajkiA.get(0));
                    } else {
                        grajkiPrzesunieteA.add(grajkiB.get(0));
                    }
                }
            }
//B
            for (int i = 0; i < grajkiB.size(); i++) {
                if (i != 0) {
                    grajkiPrzesunieteB.add(grajkiB.get(i));
                }
            }
            grajkiPrzesunieteB.add(grajkiA.get(grajkiA.size() - 1));


            grajkiA = grajkiPrzesunieteA;
            grajkiB = grajkiPrzesunieteB;

        }
    }
}
