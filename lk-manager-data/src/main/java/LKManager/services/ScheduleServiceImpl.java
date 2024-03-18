package LKManager.services;

import LKManager.DAO.RoundDAO;
import LKManager.DAO.ScheduleDAO;
import LKManager.DAO.UserDAO;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.internal.PersistentBag;
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
@RequiredArgsConstructor
//@AllArgsConstructor

public class ScheduleServiceImpl implements ScheduleService {


private final ScheduleDAO scheduleDAO;
    @Autowired
    private final RoundDAO roundDAO;


   // @Autowired
private final UserDAO userDAO;
    @Autowired
private final MZCache mzCache;



    public Schedule findByIdWithRoundsMatchesUsersAndTeams(long scheduleId)
    {
        Schedule schedule= scheduleDAO.findByScheduleId(scheduleId);

  //    var t=  roundDAO.findRoundWithMatches(schedule.getName(),1);
      var tt= roundDAO.findAllRoundsOfSchedule(schedule.getName());
       // schedule.getRounds().forEach(r->r.setMatches(roundDAO.findRoundWithMatches(schedule.getName(),r.getNr()).getMatches()));
schedule.setRounds(tt);
/*
schedule.getRounds().forEach(
        r-> r.setMatches( roundDAO.findRoundWitchMatches(schedule.getName(),r.getNr()).getMatches()));
*/

    //    r->r.setMatches(r.getMatches()));
return schedule;
    }



    @Override


    public Schedule getSchedule_ByName(String scheduleName) {

        System.out.println("temp in getsch by name");
        if(scheduleName.equals(null))
        {
            return null;
        }
        else{
            System.out.println("W Schedule getSchedule_ByName(String scheduleName)");
            System.out.println("sprawdzanie terminarzy w cache");

            /** ****************************
             * todo uncomment if need to use cache

            List<Schedule> schedules= mzCache.getSchedules();
            if(schedules.size()==0)
            {
                System.out.println("nie ma zapisanych w cache, pobieranie z bazy");
                schedules = scheduleDAO.findAllFetchRoundsEagerly();
            }

            Schedule foundSchedule= schedules.stream().filter(s->s.getName().equals(scheduleName)).findFirst()
                    .orElse(null);
             */
            Schedule foundSchedule=this.scheduleDAO.findByScheduleName(scheduleName);

if(foundSchedule!=null)
{

    System.out.println("sprawdzanie czy mecze są zainicjalizowane (lazy)");
    if(!((PersistentBag) foundSchedule.getRounds()).wasInitialized())
    {
        System.out.println("inicjalizacja rund");
        foundSchedule.setRounds(  this.findByIdWithRoundsMatchesUsersAndTeams(foundSchedule.getId()).getRounds());

    }


}


            return foundSchedule;
        }

    }
    @Override
    public Schedule getSchedule_ById(long id) {
        //sprawdzanie terminarzy w cache

        /** ****************************
         * todo uncomment if need to use cache
        List<Schedule> schedules= mzCache.getSchedules();
        if(schedules.size()==0)
        {
            //nie ma zapisanych w cache, pobieranie z bazy
            schedules = scheduleDAO.findAllFetchRoundsEagerly();
        }
        Schedule foundSchedule= schedules.stream().filter(s->s.getId()==id).findFirst().get();

        */
        Schedule foundSchedule=this.getSchedule_ById(id);

//sprawdzanie czy mecze są zainicjalizowane (lazy)
        if(!((PersistentBag) foundSchedule.getRounds().get(0).getMatches()).wasInitialized())
        {
            //inicjalizacja
            foundSchedule.setRounds(  this.findByIdWithRoundsMatchesUsersAndTeams(foundSchedule.getId()).getRounds());

        }



        return foundSchedule;
    }

    @Override
    public void deleteSchedule(String scheduleToDeleteName) {
        scheduleDAO.deleteByName(scheduleToDeleteName);
        /** ****************************
         * todo uncomment if need to use cache
        mzCache.setSchedules(scheduleDAO.findAll());
*/

    }

    @Override
    public Schedule getSchedule_TheNewest() {
        //sprawdzanie terminarzy w cache
        /** ****************************
         * todo uncomment if need to use cache

        List<Schedule> schedules= mzCache.getSchedules();
        if(schedules.size()==0)
        {
            //nie ma zapisanych w cache, pobieranie z bazy
            schedules = scheduleDAO.findAllFetchRoundsEagerly();
        }
        //szukanie najnowszego wg daty pierwszej rundy terminarza
        schedules.sort(new ScheduleByLocalDateComparator());
          Schedule newestSchedule= schedules.get(0) ;
          */


        Schedule newestSchedule=scheduleDAO.getTheNewest();

//sprawdzanie czy mecze są zainicjalizowane (lazy)
        if(!((PersistentBag) newestSchedule.getRounds().get(0).getMatches()).wasInitialized())
        {
         //inicjalizacja
      newestSchedule.setRounds(  this.findByIdWithRoundsMatchesUsersAndTeams(newestSchedule.getId()).getRounds());

        }



        return newestSchedule;
    }

    @Override
    public List<Match> getAllMatchesOfSchedule(Schedule schedule) {

       List<Match>tempList= new ArrayList<>();
        schedule.getRounds().forEach(a-> tempList.addAll(a.getMatches()));
        return tempList;
    }

    @Override
    public List<Schedule> getSchedules() {
        /** ****************************
         * todo replace if need to use cache
      List<Schedule> schedules=  mzCache.getSchedules();
      if(schedules.size()==0)
      {
          schedules=scheduleDAO.findAll();
        //  schedules=scheduleDAO.findAllFetchRoundsEagerly();
      }
      */


        List<Schedule> schedules=scheduleDAO.findAll();

        return schedules;
    }


    @Override
    public Schedule utworzTerminarzWielodniowy(LocalDate data, List<String> chosenPlayers, String nazwa) throws DatatypeConfigurationException {
        List<UserData> players = userDAO.findNotDeletedUsers();

        List<UserData> chosenPlayersUserData = new ArrayList<>();
        for (var player : players
        ) {
            for (int i = 0; i < chosenPlayers.size(); i++
            ) {
                if (player.getUsername().equals(chosenPlayers.get(i))) {
                    chosenPlayersUserData.add(player);
                    i = 0;
                    break;
                }
            }
        }






        ////////////////////////////////////////////////////////
        dodajPauzeDlaParzystosci(chosenPlayersUserData);
/////////////////podzial grajkow na pol  /////////////////
        var grajkiA = chosenPlayersUserData.subList(0, (chosenPlayersUserData.size()) / 2);
        var grajkiB = chosenPlayersUserData.subList(chosenPlayersUserData.size() / 2, chosenPlayersUserData.size());


        Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
        ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);

        List<Round> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////
        for (int j = 1; j < chosenPlayersUserData.size(); j++) {
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
               tempMatch.setUserData(listyGrajkow.getGrajkiA().get(i));
                tempMatch.setOpponentUserData(listyGrajkow.getGrajkiB().get(i));
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
    public Schedule utworzTerminarzJednodniowy(LocalDate data, List<String> chosenPlayers, String nazwa) {
        List<UserData> playersUserData = userDAO.findNotDeletedUsers();

        List<UserData> playersUserDataList = new ArrayList<>();
        for (int i = 0; i < chosenPlayers.size(); i++
        ) {


            for (int j = 0; j < playersUserData.size(); j++) {


                if (playersUserData.get(j).getUsername().equals(chosenPlayers.get(i))) {
                    playersUserDataList.add(playersUserData.get(j));
                    j = 0;
                    break;
                }
            }
        }
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

        for (int i = 0; i < playersUserDataList.size(); i++) {
            if(i%2!=0)
            {
int yy=0;
            }
            else
            {
                var tempMatch = new Match();
               tempMatch.setDateDB(data);
                tempMatch.setUserData(playersUserDataList.get(i));
                tempMatch.setOpponentUserData(playersUserDataList.get(i+1));
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
            tempTeam.setUser(tempuser);
            List<Team> tempTeams= new ArrayList<>();
            tempTeams.add(tempTeam);
            tempuser.setTeamlist(tempTeams);
            grajki.add(tempuser);



            userDAO.save(tempuser);

            /** ****************************
             * todo uncomment if need to use cache

          if(mzCache.getUsers().size()!=0)
          mzCache.addUser(tempuser);
*/
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
