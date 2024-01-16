package LKManager.services;

import LKManager.DAO.RoundDAOImpl;
import LKManager.DAO.CustomScheduleDAOImpl;
import LKManager.LK.Round;
import LKManager.LK.Schedule;
import LKManager.model.UserMZ.UserData;
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
public class ResultsServiceImpl implements ResultsService {


private final MZUserService mzUserService;
    private final CustomScheduleDAOImpl customScheduleDAOimpl;
private final RoundDAOImpl rundaDAO;

private final ScheduleService scheduleService;
  private final EntityManager entityManager;
    public ResultsServiceImpl(MZUserService mzUserService, CustomScheduleDAOImpl customScheduleDAOimpl, RoundDAOImpl rundaDAO, ScheduleService scheduleService, EntityManager entityManager, CustomScheduleDAOImpl terminarzDAO) {
        this.mzUserService = mzUserService;
        this.customScheduleDAOimpl = customScheduleDAOimpl;
        this.rundaDAO = rundaDAO;


        this.scheduleService = scheduleService;
        this.entityManager = entityManager;
        this.terminarzDAO = terminarzDAO;
    }

private final CustomScheduleDAOImpl terminarzDAO;



    @Override
    public void updateResults(Integer roundNumber, Round round, MatchService matchService, String scheduleName) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException {


        extracted(roundNumber, scheduleName, matchService );


     //   terminarzDAO.saveRound(terminarz, runda);
//zapiszDoXml(terminarz, nazwaPliku);


      //  rundaDAO.saveRoundResults(round,);



//terminarzDAO.saveResults(roundNumber, terminarz,matchService, mzUserService);

    }
@Transactional
    private  void extracted(Integer roundNumber, String scheduleName, MatchService matchService) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        Duration d = DatatypeFactory.newInstance().newDuration(false, 0, 0, 7, 0, 0, 0);
        now.add(d);

      //  Runda round= rundaDAO.findRound(scheduleName,roundNumber);

        //todo jak wyzej nie dzała to nizej sprobowac
      Round round= rundaDAO.findRoundWitchMatches(scheduleName,roundNumber);






                for (var mecz : round.getMatches()
                ) {

                    //sprawdzanie czy  gospodarzem jest apuza
                    if (!(mecz.getUser().getUsername().equals("pauza") || mecz.getUser().getUsername().equals("pauza"))) {





                        UserData user = mecz.getUser();
                        var userTeamId = user.getTeamlist().get(0).getTeamId();
                        var oponent = mecz.getopponentUser();
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
                                    if (rozegranyMecz.getTeamlist().get(1).getTeamId() == mecz.getopponentUser().getTeamlist().get(0).getTeamId()) {
                                        //aktualizacja
                                        //  mecz.setMatchResult1("1");


                                        mecz.setUserMatchResult1(String.valueOf(rozegranyMecz.getTeamlist().get(0).getGoals()));
                                        mecz.setOpponentMatchResult1(String.valueOf(rozegranyMecz.getTeamlist().get(1).getGoals()));
                                    }


                                }
                                //tutaj user ma id 1  oponent =0
                                else if (rozegranyMecz.getTeamlist().get(1).getTeamId() == userTeamId) {
                                    //sprawdzanie czy prawidlowy przeciwnik
                                    if (rozegranyMecz.getTeamlist().get(0).getTeamId() == mecz.getopponentUser().getTeamlist().get(0).getTeamId()) {
                                        //aktualizacja
                                        //  mecz.setMatchResult1("1");
                                        mecz.setUserMatchResult2(String.valueOf(rozegranyMecz.getTeamlist().get(1).getGoals()));
                                        mecz.setOpponentMatchResult2(String.valueOf(rozegranyMecz.getTeamlist().get(0).getGoals()));
                                    }


                                }

                            }
                        }

                    }



                }

               rundaDAO.saveRound(round);



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
