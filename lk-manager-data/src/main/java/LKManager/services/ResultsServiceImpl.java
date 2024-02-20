package LKManager.services;

import LKManager.DAO.CustomScheduleDAOImpl;
import LKManager.DAO.RoundDAO;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
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
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultsServiceImpl implements ResultsService {


private final MZUserService mzUserService;
    private final CustomScheduleDAOImpl customScheduleDAOimpl;
 private final    MatchService matchService;
private final RoundDAO roundDAO;
private MZCache mzCache;
private final ScheduleService scheduleService;
  private final EntityManager entityManager;






    @Override
    public Round updateResults(Integer roundNumber,  Schedule schedule) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException {


        //todo tu zamienic zeby najpierw szukalo w cache
        Round round = roundDAO.findRoundWitchMatches(schedule.getName(),roundNumber);

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
        roundDAO.save(round);

mzCache.updateRound(round, schedule);


     //   terminarzDAO.saveRound(terminarz, runda);
//zapiszDoXml(terminarz, nazwaPliku);


      //  rundaDAO.saveRoundResults(round,);



//terminarzDAO.saveResults(roundNumber, terminarz,matchService, mzUserService);
return round;
    }
@Transactional
    private  Round getResultsFromMZ(Integer roundNumber, String scheduleName) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        Duration d = DatatypeFactory.newInstance().newDuration(false, 0, 0, 7, 0, 0, 0);
        now.add(d);

      //  Runda round= rundaDAO.findRound(scheduleName,roundNumber);

        //todo jak wyzej nie dzała to nizej sprobowac
      Round round= roundDAO.findRoundWitchMatches(scheduleName,roundNumber);






                for (var mecz : round.getMatches()
                ) {

                    //sprawdzanie czy  gospodarzem jest apuza
                    if (!(mecz.getUserData().getUsername().equals("pauza") || mecz.getUserData().getUsername().equals("pauza"))) {





                        UserData user = mecz.getUserData();
                        var userTeamId = user.getTeamlist().get(0).getTeamId();
                        var oponent = mecz.getOpponentUserData();
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
                                    if (rozegranyMecz.getTeamlist().get(1).getTeamId() == mecz.getOpponentUserData().getTeamlist().get(0).getTeamId()) {
                                        //aktualizacja
                                        //  mecz.setMatchResult1("1");


                                        mecz.setUserMatchResult1(rozegranyMecz.getTeamlist().get(0).getGoals());
                                        mecz.setOpponentMatchResult1(rozegranyMecz.getTeamlist().get(1).getGoals());
                                    }


                                }
                                //tutaj user ma id 1  oponent =0
                                else if (rozegranyMecz.getTeamlist().get(1).getTeamId() == userTeamId) {
                                    //sprawdzanie czy prawidlowy przeciwnik
                                    if (rozegranyMecz.getTeamlist().get(0).getTeamId() == mecz.getOpponentUserData().getTeamlist().get(0).getTeamId()) {
                                        //aktualizacja
                                        //  mecz.setMatchResult1("1");
                                        mecz.setUserMatchResult2(rozegranyMecz.getTeamlist().get(1).getGoals());
                                        mecz.setOpponentMatchResult2(rozegranyMecz.getTeamlist().get(0).getGoals());
                                    }


                                }

                            }
                        }

                    }



                }

            return   round;



    }

    private void extracted2(Integer runda, Round round, MatchService matchService) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {




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
