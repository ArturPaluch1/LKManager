package LKManager.services;

import LKManager.DAO.ScheduleDAO;
import LKManager.DAO.UserDAOImpl;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service

public class ScheduleServiceImpl implements ScheduleService {


private final ScheduleDAO scheduleDAO;

    public ScheduleServiceImpl(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
private MZCache mzCache;
    @Override
    public Schedule utworzTerminarzWielodniowy(LocalDate data, List<UserData> grajki, String nazwa) throws DatatypeConfigurationException {

        ////////////////////////////////////////////////////////
        dodajPauzeDlaParzystosci(grajki);
/////////////////podzial grajkow na pol  /////////////////
        var grajkiA = grajki.subList(0, (grajki.size()) / 2);
        var grajkiB = grajki.subList(grajki.size() / 2, grajki.size());


        Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
        ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);

        List<Round> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////
        for (int j = 1; j < grajki.size(); j++) {
            /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
            Round round;
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
                round = new Round(j, calyTerminarz.get((calyTerminarz.size()-1)).getDate().plusDays(7L));
            } else {
                round = new Round(j, data);
            }

            //////////ustalanie par /////////////////////////////
            for (int i = 0; i < grajkiA.size(); i++) {

                var para = listyGrajkow.getGrajkiA().get(i).getUsername() + " - " + listyGrajkow.getGrajkiB().get(i).getUsername();
                System.out.println(para);

                var tempMatch = new Match();
               tempMatch.setUser(listyGrajkow.getGrajkiA().get(i));
                tempMatch.setopponentUser(listyGrajkow.getGrajkiB().get(i));
                tempMatch.setDateDB(data.plusDays(7L *(j-1)));
/*tempMatch.setDate(runda.getDateTimeItem());
tempMatch.setDateDB(runda.getDateTimeItem());
*/
                round.getMatches().add(tempMatch);

            }

  //runda.setData(data);
  //runda.setDateTimeItem();
round.setPlayed(false);
            calyTerminarz.add(round);
            System.out.println("=======" + round.getNr() + " === " + round.getDate());
            listyGrajkow.przesunListy();

        }





        /////////////// zapis termnarza do xml    //////////////////////
       Schedule schedule = new Schedule(calyTerminarz);



 //       jaxbObjectToXML(terminarz,nazwa);

/////////////////////////////////////////////
// zapis  sql
schedule.setName(nazwa);
//terminarz.getRundy().get(0).getTerminarz()


        scheduleDAO.save1(schedule);


return schedule;
//////////////
    }

    @Override
    public Schedule utworzTerminarzJednodniowy(LocalDate data, List<UserData> mecze, String nazwa) {


        ////////////////////////////////////////////////////////
   //     dodajPauzeDlaParzystosci(grajki);
/////////////////podzial grajkow na pol  /////////////////
  /*      var grajkiA = grajki.subList(0, (grajki.size()) / 2);
        var grajkiB = grajki.subList(grajki.size() / 2, grajki.size());


        Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
        ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);
*/
        List<Round> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////

            /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
            Round round;

                round = new Round(1, data);

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
                round.getMatches().add(tempMatch);
            }


        }
        round.setPlayed(false);
        calyTerminarz.add(round);
        System.out.println("=======" + round.getNr() + " === " + round.getDate());






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
        Schedule schedule = new Schedule(calyTerminarz);
        schedule.setName(nazwa);
scheduleDAO.save1(schedule);

return schedule;



    }

    @Override
    public Schedule wczytajTerminarz(String sciezka) throws JAXBException {
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
    public void aktualizujTerminarz(Schedule schedule, String nazwaPliku)
    {
        try {jaxbObjectToXML(schedule,nazwaPliku);

        }
        catch (Exception e){

    }
    }



    protected void jaxbObjectToXML(Schedule calySchedule, String nazwa) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Schedule.class);

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
            jaxbMarshaller.marshal(calySchedule, file);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }
    protected Schedule jaxbXMLToObject(String terminarz) throws JAXBException {
        Schedule schedule1 = null;

            JAXBContext ctx = JAXBContext.newInstance(Schedule.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

        File file = new File("Data/terminarze/" + terminarz);

          if (file.exists()) {

                schedule1 = (Schedule) unmarshaller.unmarshal(file);




                System.out.println(schedule1.getRounds());
                return schedule1;
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
