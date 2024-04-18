package LKManager.services;

import LKManager.DAO_SQL.RoundDAO;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.DAO_SQL.UserDAO;
import LKManager.model.MatchesMz.Match;
import LKManager.model.RecordsAndDTO.CreateScheduleResult;
import LKManager.model.RecordsAndDTO.MatchDTO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleNameDTO;
import LKManager.model.Round;
import LKManager.model.Schedule;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Adapters.MatchAdapter;
import LKManager.services.Adapters.ScheduleAdapter;
import LKManager.services.RedisService.RedisScheduleService;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@AllArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private SessionFactory sessionFactory;

private final ScheduleDAO scheduleDAO;
    @Autowired
    private final RoundDAO roundDAO;
private final UserService userService;
   // @Autowired
private final UserDAO userDAO;
/*    @Autowired
private final MZCache mzCache;
*/
private final MZUserService mzUserService;
private final RedisScheduleService redisScheduleService;

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



   // @Override


    //todo usunac\/ bo jest uzywane query na interfejsie

    public ScheduleDTO getSchedule_ByName(String scheduleName) {

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

ScheduleDTO foundSchedule=redisScheduleService.getSchedule_ByName(scheduleName);
if(foundSchedule==null) {
    foundSchedule = ScheduleAdapter.adapt(this.scheduleDAO.findByScheduleName(scheduleName));
redisScheduleService.setSchedule(foundSchedule);

}
       /*     if(foundSchedule!=null)
            {

                System.out.println("sprawdzanie czy mecze są zainicjalizowane (lazy)");
                if(!foundSchedule.getRounds().isEmpty())
                {
                    System.out.println("inicjalizacja rund");
                    foundSchedule.setRounds(  this.findByIdWithRoundsMatchesUsersAndTeams(foundSchedule.getId()).getRounds());

                }


            }


          ScheduleDTO scheduleDTO= ScheduleAdapter.adapt(foundSchedule);

            return scheduleDTO;*/

            return foundSchedule;
        }




    }
    @Override
    public ScheduleDTO getSchedule_ById(long id) {
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

        ScheduleDTO foundSchedule=redisScheduleService.getSchedule_ById(id);
        if(foundSchedule==null) {
            foundSchedule = ScheduleAdapter.adapt(this.scheduleDAO.findByScheduleId(id));
            redisScheduleService.setSchedule(foundSchedule);
        }
    /*
            ScheduleDTO foundSchedule=this.getSchedule_ById(id);

//sprawdzanie czy mecze są zainicjalizowane (lazy)
        if(!((PersistentBag) foundSchedule.getRounds().get(0).getMatches()).wasInitialized())
        {
            //inicjalizacja
            foundSchedule.setRounds(  this.findByIdWithRoundsMatchesUsersAndTeams(foundSchedule.getId()).getRounds());

        }
*/


        return foundSchedule;
    }

    @Override
    public boolean deleteSchedule(String scheduleToDeleteName) {
        ScheduleNameDTO tempSchedule=this.getScheduleNames().stream().filter(s->s.getName().equals(scheduleToDeleteName)).findFirst().orElse(null);
     if(tempSchedule!=null)
     {
         boolean result=  scheduleDAO.deleteByName(scheduleToDeleteName);
         redisScheduleService.deleteScheduleByName(tempSchedule);

         /** ****************************
          * todo uncomment if need to use cache
          mzCache.setSchedules(scheduleDAO.findAll());
          */
         return result;
     }
     else return false;

    }

    @Override
    public ScheduleDTO getSchedule_TheNewest() {
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


        ScheduleDTO newestSchedule=ScheduleAdapter.adapt(scheduleDAO.getTheNewest());

/*
//sprawdzanie czy mecze są zainicjalizowane (lazy)
        if(!((PersistentBag) newestSchedule.getRounds().get(0).getMatches()).wasInitialized())
        {
         //inicjalizacja
      newestSchedule.setRounds(  this.findByIdWithRoundsMatchesUsersAndTeams(newestSchedule.getId()).getRounds());

        }
*/



        return newestSchedule;
    }

    @Override
    public <T> List<MatchDTO> getAllMatchesOfSchedule(T schedule) {
if(schedule instanceof Schedule)
{
    List<MatchDTO>tempList= new ArrayList<>();
    ((Schedule) schedule).getRounds().forEach(a-> tempList.addAll(a.getMatches().stream().map(m-> MatchAdapter.adapt(m)).collect(Collectors.toList())));
    return tempList;
}
else if (schedule instanceof ScheduleDTO)
{
    List<MatchDTO>tempList= new ArrayList<>();
    ((ScheduleDTO) schedule).getRounds().forEach(a-> tempList.addAll(a.getMatches()));
    return tempList;
}
else return null;

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
    @Transactional
    public CreateScheduleResult createMultiDaySchedule(LocalDate data, List<String> chosenPlayers, String scheduleName) throws DatatypeConfigurationException, DatatypeConfigurationException {
        List<UserData> playersNotInMZ= new ArrayList<>();
        List<UserData> playersForSchedule= new ArrayList<>();
        chosenPlayers.stream().forEach(
                player-> {
                    UserData foundPlaer= mzUserService.findByUsernameInManagerzone(player);
                    if(foundPlaer==null)
                        playersNotInMZ.add(foundPlaer);
                    else playersForSchedule.add(foundPlaer);

                }
        );
        if(!playersNotInMZ.isEmpty())
        {
            return new CreateScheduleResult(null,playersNotInMZ);
        }
        else {

            // List<UserData> players = userDAO.findUsers_NotDeletedWithPause();

/*
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


*/



            ////////////////////////////////////////////////////////
            AddPauseForParity(playersForSchedule);
            /////////////////podzial grajkow na pol  /////////////////




            Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);


            List<Round> rounds = new ArrayList<>();

/////////tworzenie terminarza/////////////////
            for (int j = 1; j < playersForSchedule.size(); j++) {
                /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
                Round round;
                if (j != 1) {
                    round = new Round(j, rounds.get((rounds.size()-1)).getDate().plusDays(7L));
                } else {
                    round = new Round(j, data);
                }
                //////////ustalanie par /////////////////////////////
                for (int i = 0; i < playersForSchedule.size()-1; i+=2) {

                  /*  var para = listyGrajkow.getGrajkiA().get(i).getUsername() + " - " + listyGrajkow.getGrajkiB().get(i).getUsername();
                    System.out.println(para);*/

                    var tempMatch = new Match();
                    int shift=i+j;
                    if(shift>=playersForSchedule.size())
                    {
                        shift=shift-playersForSchedule.size();
                    }
                    tempMatch.setUserData(playersForSchedule.get(i));
                    tempMatch.setOpponentUserData(playersForSchedule.get(shift));
                    tempMatch.setDateDB(data.plusDays(7L *(j-1)));

                    round.getMatches().add(tempMatch);

                }





                round.setPlayed(false);
                rounds.add(round);
                System.out.println("=======" + round.getNr() + " === " + round.getDate());


            }

            /////////////// zapis termnarza do xml    //////////////////////
            Schedule schedule = new Schedule(rounds,scheduleName);

            //       jaxbObjectToXML(terminarz,nazwa);

/////////////////////////////////////////////
// zapis  sql
            schedule.setName(scheduleName);

         Schedule   savedSchedule=    scheduleDAO.saveSchedule(schedule);
            redisScheduleService.setSchedule(ScheduleAdapter.adapt(savedSchedule));

            return new CreateScheduleResult(schedule,playersNotInMZ);
            /*
/////////////////podzial grajkow na pol  /////////////////



            var grajkiA = playersForSchedule.subList(0, (playersForSchedule.size()) / 2);
            var grajkiB = playersForSchedule.subList(playersForSchedule.size() / 2, playersForSchedule.size());


            Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
            ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);

            List<Round> calyTerminarz = new ArrayList<>();

/////////tworzenie terminarza/////////////////
            for (int j = 1; j < playersForSchedule.size(); j++) {
                /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
                Round round;
                if (j != 1) {

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

                    round.getMatches().add(tempMatch);

                }

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

            scheduleDAO.save(schedule);


            return schedule;
            */


        }


//////////////
    }



    @Override
    @Transactional
    public CreateScheduleResult createOneDayShedule(LocalDate data, List<String> chosenPlayers, String scheduleName) {
       List<UserData> playersNotInMZ= new ArrayList<>();
        List<UserData> playersInMZ= new ArrayList<>();
               chosenPlayers.stream().forEach(
                player-> {
                    if(player.equals("pauza"))
                    {
                        playersInMZ.add(userService.getPauseObject());
                    }
                    else
                    {
                        UserData foundPlaer= mzUserService.findByUsernameInManagerzone(player);
                        if(foundPlaer==null)
                            playersNotInMZ.add(foundPlaer);
                        else playersInMZ.add(foundPlaer);
                    }


                }
        );
        if(!playersNotInMZ.isEmpty())
        {
            return new CreateScheduleResult(null,playersNotInMZ);
        }
        else
        {
            List<Round> rounds = new ArrayList<>();

/////////tworzenie terminarza/////////////////

            /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
            Round round;

            round = new Round(1, data);

            for (int i = 0; i < playersInMZ.size(); i++) {
                if(i%2!=0)
                {
                    int yy=0;
                }
                else
                {
                    var tempMatch = new Match();
                    tempMatch.setDateDB(data);
                    tempMatch.setUserData(playersInMZ.get(i));
                    tempMatch.setOpponentUserData(playersInMZ.get(i+1));
                    round.getMatches().add(tempMatch);
                }


            }
            round.setPlayed(false);
            rounds.add(round);
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
            Schedule schedule = new Schedule(rounds, scheduleName);

          Schedule savedSchedule=  scheduleDAO.saveSchedule(schedule);


            redisScheduleService.setSchedule(ScheduleAdapter.adapt(savedSchedule));


            return new CreateScheduleResult(schedule,playersNotInMZ);


        }

/*
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
        }*/
        ////////////////////////////////////////////////////////
   //     dodajPauzeDlaParzystosci(grajki);
/////////////////podzial grajkow na pol  /////////////////
  /*      var grajkiA = grajki.subList(0, (grajki.size()) / 2);
        var grajkiB = grajki.subList(grajki.size() / 2, grajki.size());


        Duration d = DatatypeFactory.newInstance().newDuration(true, 0, 0, 7, 0, 0, 0);
        ListyGrajkow listyGrajkow = new ListyGrajkow(grajkiA, grajkiB);
*/


    }

    @Override
    public Schedule wczytajTerminarz(String sciezka) throws JAXBException {
        return jaxbXMLToObject(sciezka);
    }



    protected void AddPauseForParity(List<UserData> players) {
        if (players.size() % 2 != 0)
players.add(userService.getPauseObject());



    }
    @Override
    public void updateSchedule(Schedule schedule, String nazwaPliku)
    {
        try {jaxbObjectToXML(schedule,nazwaPliku);

        }
        catch (Exception e){

    }
    }

    @Override
    public List<ScheduleNameDTO> getScheduleNames() {
        List<ScheduleNameDTO> scheduleNames=null;
        try{
          scheduleNames=  redisScheduleService.getScheduleNames();
        }
        catch (Exception e)
        {

        }

        if (scheduleNames.isEmpty())
        {
            System.out.println("from sql");
            scheduleNames= scheduleDAO.getScheduleNames();
        redisScheduleService.setScheduleNames(scheduleNames);

        }
        return scheduleNames;
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
