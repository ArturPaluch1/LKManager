package LKManager.services;

import LKManager.DAO_SQL.CustomScheduleDAOImpl;
import LKManager.DAO_SQL.MatchDAO;
import LKManager.DAO_SQL.RoundDAO;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.model.MatchesMz.Match;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleNameDTO;
import LKManager.model.Round;
import LKManager.model.Schedule;
import LKManager.model.UserMZ.MZUserData;
import LKManager.services.Adapters.ScheduleAdapter;
import LKManager.services.RedisService.RedisScheduleService;
import LKManager.services.RedisService.RedisTableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultsServiceImpl implements ResultsService {


private final MZUserService mzUserService;
    private final CustomScheduleDAOImpl customScheduleDAOimpl;
 private final    MatchService matchService;
private final RoundDAO roundDAO;
//private MZCache mzCache;
private final ScheduleService scheduleService;
  private final EntityManager entityManager;
  private final MatchDAO matchDAO;
private final RedisTableService redisTableService;
    private final TableService tableService;
private final RedisScheduleService redisScheduleService;
private final ScheduleDAO scheduleDAO;
private final UserService userService;

   public void simulateResults(ScheduleDTO scheduleDTO, int roundNumber)
   {

        Round   round=roundDAO.findRoundWitchMatches(scheduleDTO.getName(),roundNumber);

           if(round!=null)
           {
               //updatuj tylko które nie były rozegrane
               if(round.getMatches().get(0).getOpponentMZUserData()!=null)
               {
                   for (var match:round.getMatches()
                   ) {

                       Random random = new Random();
                       match.setUserMatchResult1((byte)(random.nextInt(5)));
                       match.setUserMatchResult2((byte)(random.nextInt(5)));
                       match.setOpponentMatchResult1((byte)(random.nextInt(5)));
                       match.setOpponentMatchResult2((byte)(random.nextInt(5)));
                   }
                   roundDAO.save(round);
               }
           }

           redisScheduleService.setSchedule(ScheduleAdapter.adapt(scheduleDAO.findByScheduleId(round.getSchedule().getId())));
       redisTableService.setTable(tableService.createTable(ScheduleAdapter.adapt(round.getSchedule())));


   }







    @Override
    public Round updateResults(Integer roundNumber,  ScheduleDTO schedule) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException {

        System.out.println("round number: "+roundNumber+"  sched: "+schedule.getName());
        //todo tu zamienic zeby najpierw szukalo w cache

        Round round = roundDAO.findRoundWitchMatches(schedule.getName(),roundNumber);
     //   System.out.println("round"+round.getMatches());
        if(round != null)
        {
            // terminarz= terminarzService.wczytajTerminarz("terminarz.xml");
        }
        else
        {
            //todo zablokowac robienie 2 z ta samą nazwą?
            //todo nie ma takiej rundy
            return null;
        }

// runda -1 bo rundy od 1 a indeksy w liscie od 0
        //      if(terminarz.getRundy().get(numerRundy-1).getStatus()== Runda.status.rozegrana)
        //todo to jest sprawdzane ale zawsze jest false, a w baie jest niezapisane bo nie ma takiej kolumny. Albo usunąć albo dodać kolumnę
  /*      if(round.isPlayed())
        {
            // TODO: 2022-11-23   zamienic na zapytanie, że czy na pewno chcesz zmienić rozegrana runde
            //rundy.get(numerRundy-1).setPlayed(true);
            resultsService.updateResults(roundNumber, round,matchService, chosenSchedule);
            redirectAttributes.addAttribute("numerRundy", roundNumber);
            redirectAttributes.addAttribute("chosenSchedule",chosenSchedule );


            return "redirect:/results";
        }
        else
        {
            round.setPlayed(true);
            resultsService.updateResults(roundNumber, round,matchService, chosenSchedule);
            redirectAttributes.addAttribute("roundNumber", roundNumber);
            redirectAttributes.addAttribute("chosenSchedule",chosenSchedule );

            mzCache.updateRound(round);

            return "redirect:/results";
        }

*/





       round= getResultsFromMZ(roundNumber, schedule.getName() );

        //todo to zamienić na  jpql
     //   roundDAO.saveRound(round);
    round=    roundDAO.save(round);

        redisScheduleService.deleteScheduleByName(new ScheduleNameDTO(round.getSchedule().getId(), round.getSchedule().getName()));
       ScheduleDTO updatedSchedule= redisScheduleService.setSchedule(scheduleService.getSchedule_ById(round.getSchedule().getId()));
redisTableService.setTable(tableService.createTable(updatedSchedule));

        System.out.println("round=" + round.getMatches());

        /** ****************************
         * todo uncomment if need to use cache
         *
mzCache.updateRound(round, schedule);
*/


        //System.out.println("cache:=" + mzCache.getSchedules().stream().filter(s->s.getName().equals(schedule)).findFirst().get().getRounds().get(0).getMatches());

     //   terminarzDAO.saveRound(terminarz, runda);
//zapiszDoXml(terminarz, nazwaPliku);


      //  rundaDAO.saveRoundResults(round,);



//terminarzDAO.saveResults(roundNumber, terminarz,matchService, mzUserService);
return round;
    }

    @Override
    @Transactional
    public List<Round> updateRoundResultsForDate(LocalDate date) {


        List<Round> roundsForDate=   roundDAO.findRoundsByDate(date);

List<Round>resultsList=  roundsForDate.stream().map( round-> {
    try {
        System.out.println("Trying to update round:");

                    System.out.println(round.getSchedule().getName()+" round: "+round.getNr());


        Round tempRound = getResultsFromMZ(round);
         tempRound= roundDAO.save(tempRound);

        redisScheduleService.deleteScheduleByName(new ScheduleNameDTO(tempRound.getSchedule().getId(), tempRound.getSchedule().getName()));
        redisScheduleService.setSchedule(scheduleService.getSchedule_ById(tempRound.getSchedule().getId()));



        redisTableService.setTable(tableService.createTable(tempRound.getSchedule().getId()));



        System.out.println("With success");
return tempRound;

    } catch (Exception e) {
        //todo redirect to error page with message: could not retrive mz match results
        // or not redirect? It works on the server, not from user.

        e.printStackTrace();
        System.out.println("With failure");
        return null;
    }


    //todo to zamienić na  jpql
    //   roundDAO.saveRound(round);
}).filter(Objects::nonNull).collect(Collectors.toList());



        return resultsList;
    }




    @Override
    public Round editResults(ScheduleDTO schedule, Integer roundNumber, List<Long> matchIds, List<String> userMatchResults1, List<String> userMatchResults2, List<String> opponentMatchResults1, List<String> opponentMatchResults2)
 {
     Round round= null;
     try{
         System.out.println("w try");

         for (int i = 0; i < matchIds.size(); i++) {
             System.out.println("id="+i);
             Boolean changed=false;
             Match tempMatch= matchDAO.findByMatchId(matchIds.get(i));

             if(tempMatch.getUserMatchResult1()==null
                     ||tempMatch.getUserMatchResult1()!=Byte.valueOf(userMatchResults1.get(i)) ||
                     tempMatch.getOpponentMatchResult1()==null||
                     tempMatch.getOpponentMatchResult1()!=Byte.valueOf(opponentMatchResults1.get(i))
             )
             {
                 if(userMatchResults1.get(i)!=null&&opponentMatchResults1.get(i)!="")
                 {
                     tempMatch.setUserMatchResult1(Byte.valueOf(userMatchResults1.get(i)));

                     tempMatch.setOpponentMatchResult1(Byte.valueOf(opponentMatchResults1.get(i)));

                     changed=true;
                 }


             }
             System.out.println("-2");
             if(tempMatch.getUserMatchResult2()==null||tempMatch.getUserMatchResult2()!=Byte.valueOf(userMatchResults2.get(i)) ||
                     tempMatch.getOpponentMatchResult2()==null|| tempMatch.getOpponentMatchResult2()!=Byte.valueOf(opponentMatchResults2.get(i))
             )
             {
                 System.out.println("2");
                 if(userMatchResults2.get(i)!=null&&opponentMatchResults2.get(i)!="")
                 {
                     tempMatch.setUserMatchResult2(Byte.valueOf(userMatchResults2.get(i)));
                     tempMatch.setOpponentMatchResult2(Byte.valueOf(opponentMatchResults2.get(i)));
                     changed=true;
                 }

             }
             System.out.println(changed);
             if(changed)
             {
                 System.out.println("changed=true try save");
                 matchDAO.save(tempMatch);


                 System.out.println("saved");
             }

         }

 /*    for (var matchId:matchIds
          ) {
       Match tempMatch= matchDAO.findByMatchId(matchId);
       if(tempMatch.)
         matchDAO.save(tempMatch);
     }

        matchDAO.updateMatchesResults( schedule,  roundNumber,matchIds, userMatchResults1, userMatchResults2, opponentMatchResults1, opponentMatchResults2);
   */
          round = roundDAO.findRoundWitchMatches(schedule.getName(),roundNumber);
         System.out.println("try save cache rn="+roundNumber);

         /** ****************************
          * todo uncomment if need to use cache

         mzCache.updateRound(round, schedule);

         */
         System.out.println("saved");

         redisScheduleService.deleteScheduleByName(new ScheduleNameDTO(schedule.getId(), schedule.getName()));
         ScheduleDTO updatedSchedule=   redisScheduleService.setSchedule(scheduleService.getSchedule_ById(schedule.getId()));
 redisTableService.setTable(tableService.createTable(updatedSchedule));




         return round;
     }
     catch (Exception e)
     {
         System.out.println("catch");
         return round;
     }



    }

    @Transactional
    private  Round getResultsFromMZ(Integer roundNumber, String scheduleName) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {

        //todo jak wyzej nie dzała to nizej sprobowac
      Round round=    roundDAO.findRoundWitchMatches(scheduleName,roundNumber);
        return getMZResults(round);
    }
    @Transactional
    private  Round getResultsFromMZ(Round round) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {

        return getMZResults(round);
    }
    @Transactional

    private Round getMZResults(Round round) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

// todo \/  to niepotrzebne??
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        Duration d = DatatypeFactory.newInstance().newDuration(false, 0, 0, 7, 0, 0, 0);
        now.add(d);
// todo /\  to niepotrzebne??

        if(round.getMatches()!=null)
        {
            for (var mecz : round.getMatches()
            ) {

                //sprawdzanie czy  gospodarzem jest apuza
                if (!(mecz.getMZUserData().getUsername().equals("pauza") || mecz.getOpponentMZUserData().getUsername().equals("pauza"))) {





                    MZUserData user = mecz.getMZUserData();
                    var userTeamId = user.getTeamlist().get(0).getTeamId();
                    var oponent = mecz.getOpponentMZUserData();
                    var oponentTeamId = oponent.getTeamlist().get(0).getTeamId();

                    var rozegrane = matchService.findPlayedByUser(user);

                    var meczeTurniejowe = rozegrane.getMatches().stream().
                            filter(a -> a.getDate()!=null).//.contains(item.getData().toString())).
                                    filter(a -> a.getType().equals("friendly")).collect(Collectors.toList());

                    //todo sprawzic czy id oponenta to id z terminarza
                    for (var rozegranyMecz : meczeTurniejowe
                    ) {
                        //sprawdzanie daty  contains a nie equals bo rozegranyMecz.getDate() zawiera godziny a w round jest bez godzin
                        if(rozegranyMecz.getDate().contains(round.getDate().toString()))
                        {
                            //tutaj user ma id =0 oponent 1
                            if (rozegranyMecz.getTeamlist().get(0).getTeamId() == userTeamId) {
                                //sprawdzanie czy prawidlowy przeciwnik
                                if (rozegranyMecz.getTeamlist().get(1).getTeamId() == mecz.getOpponentMZUserData().getTeamlist().get(0).getTeamId()) {
                                    //aktualizacja
                                    //  mecz.setMatchResult1("1");



                                    if(mecz.getUserMatchResult1()==null &&mecz.getOpponentMatchResult1()==null)
                                    {
                                        mecz.setUserMatchResult1(rozegranyMecz.getTeamlist().get(0).getGoals());
                                        mecz.setOpponentMatchResult1(rozegranyMecz.getTeamlist().get(1).getGoals());

                                        //todo przerobić na zmiane dla klasy User
                                   /*   long userReliability=  mecz.getMZUserData().getReliability();
                                        long opponentReliability=   mecz.getOpponentMZUserData().getReliability();
                                        mecz.getMZUserData().setReliability(userReliability+=3);
                                        mecz.getOpponentMZUserData().setReliability(opponentReliability+=3);*/
                                    }

                                }


                            }
                            //tutaj user ma id 1  oponent =0
                            else if (rozegranyMecz.getTeamlist().get(1).getTeamId() == userTeamId) {
                                //sprawdzanie czy prawidlowy przeciwnik
                                if (rozegranyMecz.getTeamlist().get(0).getTeamId() == mecz.getOpponentMZUserData().getTeamlist().get(0).getTeamId()) {
                                    //aktualizacja
                                    //  mecz.setMatchResult1("1");
                                    if(mecz.getUserMatchResult2()==null &&mecz.getOpponentMatchResult2()==null) {
                                        mecz.setUserMatchResult2(rozegranyMecz.getTeamlist().get(1).getGoals());
                                        mecz.setOpponentMatchResult2(rozegranyMecz.getTeamlist().get(0).getGoals());
                                        //todo przerobić na zmiane dla klasy User
                                       /* long userReliability=  mecz.getMZUserData().getReliability();
                                        long opponentReliability=   mecz.getOpponentMZUserData().getReliability();
                                        mecz.getMZUserData().setReliability(userReliability+=3);
                                        mecz.getOpponentMZUserData().setReliability(opponentReliability+=3);
                                    */
                                    }
                                }


                            }

                        }
                    }

                }
        /*        else
                {
                   if (mecz.getUserData().getUsername().equals("pauza"))
                    {
                        mecz.
                    }
                   else if(mecz.getOpponentUserData().getUsername().equals("pauza")){

                }
                }*/



            }
        }
        else  //creating matches for swiss schedule ( list of matches is empty, only round entity exists)
        {
        //    ScheduleDTO scheduleDTO = ScheduleAdapter.adapt(round.getSchedule());
         //   scheduleService.calculateNextRoundOfSwissSchedule(scheduleDTO,tableService.createSwissScheduleTable(scheduleDTO));
        }



        return round;
    }



    @Override
    public void saveToXML(Schedule calySchedule, String fileName) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Schedule.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);



            File file = new File("Data/terminarze/"+fileName);

            //Writes XML file to file-system
            jaxbMarshaller.marshal(calySchedule, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }




}
