package LKManager.services;

import LKManager.DAO_SQL.RoundDAO;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.DAO_SQL.UserDAO;
import LKManager.LK.PlayerSummary;
import LKManager.model.MatchesMz.Match;
import LKManager.model.RecordsAndDTO.*;
import LKManager.model.Round;
import LKManager.model.Schedule;
import LKManager.model.Table;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Adapters.MatchAdapter;
import LKManager.services.Adapters.ScheduleAdapter;
import LKManager.services.Adapters.UserAdapter;
import LKManager.services.Comparators.UserDataDTOReliabilityComparator;
import LKManager.services.RedisService.RedisScheduleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
private final RoundService roundService;

///temp
// private final TableService tableService;



//////////



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
  Schedule scheduleinDB=  this.scheduleDAO.findByScheduleName(scheduleName);
  if(scheduleinDB!=null)
  {
      foundSchedule = ScheduleAdapter.adapt(scheduleinDB);
      redisScheduleService.setSchedule(foundSchedule);
  }
else return null;

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
            foundSchedule = ScheduleAdapter.adapt(scheduleDAO.findByScheduleId(id));
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
                        //UserData foundPlayer= mzUserService.findByUsernameInManagerzone(player);
                        UserData foundPlayer= userService.getUserDataByUsername(player);

                        if(foundPlayer==null)
                            playersNotInMZ.add(foundPlayer);
                        else playersInMZ.add(foundPlayer);
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
    @Transactional
    public boolean updateSchedule(Schedule schedule) {

        Schedule savedSchedule=  scheduleDAO.saveSchedule(schedule);


        redisScheduleService.setSchedule(ScheduleAdapter.adapt(savedSchedule));


        return true;
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










    public ScheduleDTO createSwissSystemSchedule (List<UserDAO> users)
    {

return null;







    }

  /*  @Getter
    @Setter
    @RequiredArgsConstructor

    class SwissPlayerDTO {
        private UserDataDTO player;
        private   int totalPoints;
        private   boolean wolnyLos;

        public SwissPlayerDTO(UserDataDTO player) {
            this.player = player;
            this.totalPoints = 0;
            this.wolnyLos = false;
        }
        public SwissPlayerDTO(UserData player) {
            this.player = UserAdapter.convertUserDataToUserDataDTO(player);
            this.totalPoints = 0;
            this.wolnyLos = false;
        }
    }
*/

    public RoundDTO createPairingsForNextRound(List<UserDAO> players, int roundNr, List<RoundDAO> rounds,LocalDate startDate)
    {
return null;
    }


    @Service
    @Getter @Setter
    class SwissTable{
        private List<PlayerSummary> table;

     /*   List<SwissPlayer>
        createSwissTable()
        {

        }*/
    }

    public CreateScheduleResult createSwissScheduleWithPlayerNames(LocalDate startDate, List<String> signedPlayers, String scheduleName, Integer roundsNumber) {
if(signedPlayers!=null)
{
  //  List<UserDataDTO> signedPlayersData= signedPlayers.stream().forEach(p-> userService.get);


    List<UserDataDTO> playersInMZ= new ArrayList<>();
    List<UserDataDTO> playersNotInMZ= new ArrayList<>();
    signedPlayers.stream().forEach(
            player-> {
                if(player.equals("pauza"))
                {
                    playersInMZ.add(UserAdapter.convertUserDataToUserDataDTO(userService.getPauseObject()));
                }
                else
                {
                    UserDataDTO foundPlayer= UserAdapter.convertUserDataToUserDataDTO(userService.getUserDataByUsername(player));
                    if(foundPlayer==null)
                        playersNotInMZ.add(foundPlayer);
                    else playersInMZ.add(foundPlayer);
                }


            }
    );

    if(playersNotInMZ.size()==0)
    {

        return createSwissScheduleWithPlayerData(startDate,playersInMZ,scheduleName,roundsNumber);
    }
    else return null;

}
else return null;

    }
        public CreateScheduleResult createSwissScheduleWithPlayerData(LocalDate startDate, List<UserDataDTO> signedPlayers, String scheduleName,Integer roundsNumber) {
       ScheduleNameDTO scheduleInDB=this.getScheduleNames().stream().filter(schedule->schedule.getName().equals(scheduleName)).findFirst().orElse(null);
       if(scheduleInDB==null)
       {
           List<Round> rounds = new ArrayList<>();

           List<UserData> playersInMZ = new ArrayList<>();
           List<UserData> playersNotInMZ = new ArrayList<>();



           if(signedPlayers.size()%2!=0)
           {
               signedPlayers.add(UserAdapter.convertUserDataToUserDataDTO( userService.getPauseObject()));
           }





           Collections.sort(signedPlayers, new UserDataDTOReliabilityComparator());

           //todo tutaj wziac pod uwage regularnosc
           signedPlayers.stream().forEach(
                   player -> {
                       if (player.getUserId() == 0) {
                           playersInMZ.add(userService.getPauseObject());
                       } else {
                           // UserData foundPlayer = mzUserService.findByUsernameInManagerzone(player.getUsername());
                           UserData foundPlayer = userService.getUserDataByUsername(player.getUsername());
                           if (foundPlayer == null)
                               playersNotInMZ.add(foundPlayer);
                           else playersInMZ.add(foundPlayer);
                       }


                   }
           );
           if (!playersNotInMZ.isEmpty()) {
               // todo nie ma takiego grcza w mz  .  dodatkowo zrobić jeszcze sprawdzenie czy ma drużynę w piłce!!!
               return new CreateScheduleResult(null,playersNotInMZ);

           } else {

               for (int roundNumber = 0; roundNumber <roundsNumber ; roundNumber++) {
                   /////ustalanie dat i id kolejnych rund /////////////////////////////////////////
                   Round round = new Round(roundNumber+1,startDate.plusDays(roundNumber*7));


                   if(roundNumber==0) //create starting round pairings
                   {
                       for (int i = 0; i < playersInMZ.size(); i++) {
                           if (i % 2 != 0) {
                               int yy = 0;
                           } else {
                               var tempMatch = new Match();
                               tempMatch.setDateDB(startDate);
                               tempMatch.setUserData(playersInMZ.get(i));
                               tempMatch.setOpponentUserData(playersInMZ.get(i + 1));
                               tempMatch.setRound(round);
                               round.getMatches().add(tempMatch);
                           }
                       }


                   }

                   round.setPlayed(false);
                   rounds.add(round);



               }


               Schedule schedule = new Schedule(rounds, scheduleName);

               Schedule savedSchedule = scheduleDAO.saveSchedule(schedule);

redisScheduleService.deleteScheduleByName(new ScheduleNameDTO(savedSchedule.getId(),savedSchedule.getName()));
               redisScheduleService.setSchedule(ScheduleAdapter.adapt(savedSchedule));


               return new CreateScheduleResult(schedule, playersNotInMZ);
           }
           }


       else return null;

    }


void calculateSwissScheduleTable()
{

}




@Transactional
public void calculateNextRoundOfSwissSchedule(ScheduleDTO schedule,Table table) {
  //  LocalDate startDate = schedule.getRounds().get(schedule.getRounds().size() - 1).getDate().plusDays(7);
    LocalDate startDate =     LocalDate.now().plusDays(6);
RoundDTO foundRound=schedule.getRounds().stream().filter(round-> round.getDate().equals(LocalDate.now().plusDays(6))).findFirst().orElse(null);
//todo /\ to są temp opcje

      //  RoundDTO foundRound=schedule.getRounds().stream().filter(r->r.getDate().equals(LocalDate.now())).findFirst().orElse(null);

    System.out.println(table.getPlayerSummaries());

        Round round=null;
        if(foundRound!=null&&foundRound.getMatches().size()==0)
        {

            try {
                round=   roundDAO.findByScheduleIdAndRoundNumber(schedule.getId(),foundRound.getNr());
            } catch (SQLSyntaxErrorException e) {
                throw new RuntimeException(e);
            }



            List<PlayerSummary> sp = new ArrayList<>();


            //   Round round = new Round(schedule.getRounds().size()+1, startDate);
            //  round.setPlayed(false);


            List<UserDataDTO> usersHadPause = schedule.getRounds().stream().flatMap(r -> r.getMatches().stream()).
                    filter(
                            matchDTO ->
                            {
                                if (matchDTO.getUserDataDTO().getUserId() == 0)//id pauzy /wolnego losu
                                {
                                    return true;

                                } else if (matchDTO.getOpponentUserDataDTO().getUserId() == 0) {
                                    return true;
                                } else return false;
                            }
                    ).map(matchDTO -> {
                        if (matchDTO.getUserDataDTO().getUserId() == 0) {
                            return matchDTO.getOpponentUserDataDTO(); // zwraca przeciwnika, jeśli użytkownik miał pauzę / wolny los
                        } else {
                            return matchDTO.getUserDataDTO(); // zwraca użytkownika, jeśli przeciwnik miał pauzę / wolny los
                        }
                    })
                    .collect(Collectors.toList());

            /*SwissTable  swissTable= new SwissTable();*/

            List<UserData> allParticipants = scheduleDAO.findAllParticipantsOfSchedule(schedule.getName());

            List<PlayerSummary> availablePlayers = allParticipants.stream()
                    .map(PlayerSummary::new)
                    .collect(Collectors.toList());


            // Sprawdzenie czy liczba graczy jest nieparzysta
            if (sp.size() % 2 != 0) {
                // Znalezienie gracza z najmniejszą całkowitą sumą punktów, który jeszcze nie ma wolnego losu
                PlayerSummary playerWithMinPoints = null;
                for (PlayerSummary player : sp) {
                    if (!player.isWolnyLos()) {
                        if (playerWithMinPoints == null || player.getTotalPoints() < playerWithMinPoints.getTotalPoints()) {
                            playerWithMinPoints = player;

                        }
                    }
                }

                // Ustawienie zmiennej wolnyLos na true dla znalezionego gracza
                if (playerWithMinPoints != null) {
                    playerWithMinPoints.isWolnyLos();// = true;

                    UserData pauseObject = userService.getPauseObject();

                    var tempMatch = new Match();
                    tempMatch.setDateDB(startDate);
                    tempMatch.setUserData(userService.getUserById(playerWithMinPoints.getPlayer().getUserId()));
                    tempMatch.setOpponentUserData(pauseObject);
                    round.getMatches().add(tempMatch);


                    round.getMatches().add(tempMatch);

                    System.out.println("wolny los dostał: " + playerWithMinPoints.getPlayer().getUsername());
                    sp.remove(playerWithMinPoints);
                }
            }

            // Utworzenie rundy
            //  List<Match>mecze= new ArrayList<>();


            // Posortowanie graczy według całkowitej sumy punktów
            availablePlayers=table.getPlayerSummaries();
            Collections.sort(availablePlayers, Comparator.comparingInt(PlayerSummary::getTotalPoints).reversed());



            //  availablePlayers.sort(Comparator.comparingInt(player -> player.totalPoints)).reversed());

            // Parowanie graczy na podstawie najbardziej zbliżonej wartości całkowitej sumy punktów
            int i=0;
            while (availablePlayers.size() > 0) {
// todo ???

                //    for (int i = 0; i < availablePlayers.size() / 2; i++) {


                if (availablePlayers.size() < 3) {
                    int t = 0;//todo???
                }
                PlayerSummary player1 = availablePlayers.get(i);
                PlayerSummary player2= findOpponent(player1, availablePlayers, schedule.getRounds());//= availablePlayers.get(i+1);

                var tmp1=player1.getPlayer().getUsername();
                //   var tmp2=player2.player.getUsername();

                // Sprawdzenie czy gracze nie grali ze sobą wcześniej
                // ...
                //   player2

                if (player2 == null) {
                    System.out.println(player1.getPlayer().getUsername());
                    System.out.println(availablePlayers.get(i).getPlayer().getUsername());
                    System.out.println(availablePlayers.get(i + 1).getPlayer().getUsername());
                    // System.out.println(player2.name);
                    player1 = availablePlayers.get(i);
                    player2 = availablePlayers.get(i + 1);

                    int lastIndex = round.getMatches().size() - 1;
                    for (int j = lastIndex; j >= 0; j--) {
                        //player1 -> nowy player1
                        PlayerSummary tempPlayer1n1 = findOpponent(player1, new ArrayList<>(List.of(new PlayerSummary(round.getMatches().get(j).getUserData()))), schedule.getRounds());
                        //player1 -> nowy player2
                        PlayerSummary tempPlayer1n2 = findOpponent(player1, new ArrayList<>(List.of(new PlayerSummary(round.getMatches().get(j).getOpponentUserData()))), schedule.getRounds());

                        PlayerSummary tempPlayer2n1 = findOpponent(player2, new ArrayList<>(List.of(new PlayerSummary(round.getMatches().get(j).getUserData()))), schedule.getRounds());
                        PlayerSummary tempPlayer2n2 = findOpponent(player2, new ArrayList<>(List.of(new PlayerSummary(round.getMatches().get(j).getOpponentUserData()))), schedule.getRounds());


                        if (tempPlayer1n1 != null && tempPlayer2n2 != null) {
                            //wcześniejsza para //todo to zamienic tak jak niżej ? \/
                            System.out.println("zamiana1 " + player1.getPlayer().getUsername() + " " + tempPlayer1n1.getPlayer().getUsername() + " / " + player2.getPlayer().getUsername() + " " + tempPlayer2n2.getPlayer().getUsername());

                            round.getMatches().get(j).setUserData(userService.getUserById(player1.getPlayer().getUserId()));
                            round.getMatches().get(j).setOpponentUserData(userService.getUserById(tempPlayer1n1.getPlayer().getUserId()));
                            //ost para
                            //  round.matches.get(lastIndex).setPlayer1(player2);
                            //    round.matches.get(lastIndex).setPlayer2(tempPlayer2n2);

                            availablePlayers.remove(player1);
                            availablePlayers.remove(player2);

                            player1 = player2;
                            player2 = tempPlayer2n2;

                            break;

                        } else if (tempPlayer1n2 != null && tempPlayer2n1 != null) {
//wcześniejsza para
                            PlayerSummary finalPlayer = player1;
                            round.getMatches().get(j).setUserData(userService.getUserById(player1.getPlayer().getUserId()));
                            round.getMatches().get(j).setOpponentUserData(userService.getUserById(tempPlayer1n2.getPlayer().getUserId()));

                            //       round.getMatches().get(j).setUserData(allParticipants.stream().filter(u -> u.getUserId() == finalPlayer.player.getUserId()).findFirst().orElse(null));
                            //       round.getMatches().get(j).setOpponentUserData(allParticipants.stream().filter(u -> u.getUserId() == tempPlayer1n2.player.getUserId()).findFirst().orElse(null));
                            //ost para
                            //   round.matches.get(lastIndex).setPlayer1(player2);
                            //    round.matches.get(lastIndex).setPlayer2(tempPlayer2n1);

                            //   System.out.println("zamiana2 " + player1.name + " " + tempPlayer1n2.getName() + " / " + player2.name + " " + tempPlayer2n1.getName());
                            availablePlayers.remove(player1);
                            availablePlayers.remove(player2);

                            player1 = player2;
                            player2 = tempPlayer2n1;

                            break;
                        }
                    }
                }

                // Utworzenie meczu i dodanie go do rundy

                //  Match match = new Match(player1, player2, "To be played");
                Match match = new Match();
                match.setDateDB(startDate);
                PlayerSummary finalPlayer1 = player1;
                match.setUserData(allParticipants.stream().filter(u -> u.getUserId().equals(finalPlayer1.getPlayer().getUserId())).findFirst().orElse(null));
                PlayerSummary finalPlayer2 = player2;

                match.setOpponentUserData(allParticipants.stream().filter(u -> u.getUserId().equals(finalPlayer2.getPlayer().getUserId())).findFirst().orElse(null));
                match.setRound(round);

                round.getMatches().add(match);


                availablePlayers.remove(player1);
                availablePlayers.remove(player2);
                //  round.getMatches().add(match);




                //    }





                // Wyświetlenie parowania dla danej rundy
                //    System.out.println("Runda " + roundNr + " - parowanie: ");
     /*   for (Match match : round.getMatches()
        ) {
            //      System.out.println(match.getPlayer1().name+" vs "+match.getPlayer2().name+" "+match.result+"\n");
        }

        rounds.add(round);
        //     return round;


        Schedule schedule = new Schedule(rounds, scheduleName);*/


            }

            roundDAO.save(round);
   /* Schedule scheduleInDB= scheduleDAO.findByScheduleId(schedule.getId());
    scheduleInDB.getRounds().add(round);
    this.updateSchedule(scheduleInDB);*/
           Schedule updatedSchedule= scheduleDAO.findByScheduleId(schedule.getId());
            scheduleDAO.refresh(updatedSchedule);
            ScheduleDTO updatedScheduleDTO=ScheduleAdapter.adapt(updatedSchedule );
 //  todo /\ jak to nie działa to to na dole to bkp działający !!
      //     ScheduleDTO updatedSchedule=this.getSchedule_ById(schedule.getId());


            //todo to naprawic bo chyba nie usuwa. Najlepiej zamienić na po id
            redisScheduleService.deleteScheduleByName(new ScheduleNameDTO(updatedSchedule.getId(), updatedSchedule.getName()));


            redisScheduleService.setSchedule(updatedScheduleDTO);
        }
        else
        {

        }








}



    private PlayerSummary findOpponent(PlayerSummary player, List<PlayerSummary> opponents, List<RoundDTO> rounds) {
        for (PlayerSummary opponent : opponents) {
            if(opponent!=player)
            {

                if (!havePlayedBefore(player, opponent,rounds)) {
                    return opponent;
                }
            }

        }
        return null;
    }
/*    private SwissPlayerDTO findOpponent(SwissPlayerDTO player, List<SwissPlayerDTO> opponents, List<RoundDTO> rounds) {
        for (SwissPlayerDTO opponent : opponents) {
            if(opponent!=player)
            {

                if (!havePlayedBefore(player, opponent,rounds)) {
                    return opponent;
                }
            }

        }
        return null;
    }*/
    private boolean havePlayedBefore(PlayerSummary player1, PlayerSummary player2, List<RoundDTO> rounds) {
        for (RoundDTO round:rounds
        ) {
            for (MatchDTO match : round.getMatches()) {
             var u1=  match.getUserDataDTO().getUserId();
                 var u2=    player1.getPlayer().getUserId() ;
boolean warunek1=u1==u2;
          var o1=  match.getOpponentUserDataDTO().getUserId() ;
         var o2=   player2.getPlayer().getUserId();
                boolean warunek2=o1==o2;




                    if ((match.getUserDataDTO().getUserId().equals(player1.getPlayer().getUserId()) && match.getOpponentUserDataDTO().getUserId().equals(player2.getPlayer().getUserId())) ||
                        (match.getUserDataDTO().getUserId().equals(player2.getPlayer().getUserId()) && match.getOpponentUserDataDTO().getUserId().equals(player1.getPlayer().getUserId()))) {
                    return true;
                }
            }

        }

        return false;
    }

























}
