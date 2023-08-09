package LKManager.services;

import LKManager.DAO.TerminarzDAOImpl;
import LKManager.LK.Terminarz;
import org.springframework.stereotype.Service;
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
public class wynikiServiceImpl implements WynikiService {


private MZUserService mzUserService;
    private TerminarzDAOImpl terminarzDAOimpl;

private final TerminarzService terminarzService;
  private   EntityManager entityManager;
    public wynikiServiceImpl(MZUserService mzUserService, TerminarzDAOImpl terminarzDAOimpl, TerminarzService terminarzService, EntityManager entityManager, TerminarzDAOImpl terminarzDAO) {
        this.mzUserService = mzUserService;
        this.terminarzDAOimpl = terminarzDAOimpl;

        this.terminarzService = terminarzService;
        this.entityManager = entityManager;
        this.terminarzDAO = terminarzDAO;
    }

private TerminarzDAOImpl terminarzDAO;

    @Override
    public void aktualizujWyniki(Integer runda, Terminarz terminarz, MatchService matchService, String nazwaPliku) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException {

        extracted2(runda, terminarz, matchService );


     //   terminarzDAO.saveRound(terminarz, runda);
//zapiszDoXml(terminarz, nazwaPliku);
terminarzDAO.saveResults(runda, terminarz,matchService, mzUserService);

    }

    private static void extracted(Integer runda, Terminarz terminarz, MatchService matchService) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        Duration d = DatatypeFactory.newInstance().newDuration(false, 0, 0, 7, 0, 0, 0);
        now.add(d);


        for (var item: terminarz.getRundy()
        ) {


            if(  item.getNr()== runda) {



                for (var mecz : item.getMecze()
                ) {

                    //sprawdzanie czy  gospodarzem jest apuza
                    if (!(mecz.getUser().getUsername().equals("pauza") || mecz.getUser().getUsername().equals("pauza"))) {





                        var user = mecz.getUser();
                        var userTeamId = user.getTeamlist().get(0).getTeamId();
                        var oponent = mecz.getopponentUser();
                        var oponentTeamId = oponent.getTeamlist().get(0).getTeamId();

                        var rozegrane = matchService.findPlayedByUsername(user.getUsername());

                        var meczeTurniejowe = rozegrane.getMatches().stream().
                                filter(a -> a.getDate()!=null).//.contains(item.getData().toString())).
                                filter(a -> a.getType().equals("friendly")).collect(Collectors.toList());

                        //todo sprawzic czy id oponenta to id z terminarza
                        for (var rozegranyMecz : meczeTurniejowe
                        ) {
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
        }
    }

    private void extracted2(Integer runda, Terminarz terminarz, MatchService matchService) throws DatatypeConfigurationException, IOException, ParserConfigurationException, SAXException, JAXBException {

    }

    @Override
    public void zapiszDoXml(Terminarz calyTerminarz, String nazwaPliku) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Terminarz.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);



            File file = new File("Data/terminarze/"+nazwaPliku);

            //Writes XML file to file-system
            jaxbMarshaller.marshal(calyTerminarz, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
