package LKManager.services;

import LKManager.LK.Terminarz;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

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

private final TerminarzService terminarzService;

    public wynikiServiceImpl(TerminarzService terminarzService) {
        this.terminarzService = terminarzService;
    }

  /*  @Override
    public Terminarz wyswietlWyniki(Integer runda) {

     return   jaxbXMLToObject("terminarz.xml");




    }*/

    @Override
    public void aktualizujWyniki(Integer runda, Terminarz terminarz, MatchService matchService, String nazwaPliku) throws DatatypeConfigurationException, ParserConfigurationException, JAXBException, SAXException, IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        XMLGregorianCalendar xmlGregorianCalendar;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        Duration d = DatatypeFactory.newInstance().newDuration(false, 0, 0, 7, 0, 0, 0);
        now.add(d);




        for (var item:terminarz.getTerminarz()
        ) {


            if(  item.getNr()==runda) {
                // item.mecze == mecze danej rundy


                for (var mecz : item.getMecze()
                ) {

                    //sprawdzanie czy  gospodarzem jest apuza
                    if (!(mecz.getUser().getUsername().equals("pauza") || mecz.getUser().getUsername().equals("pauza"))) {





                         /*                 mecz.setUserMatchResult1(11);
                    mecz.setOpponentMatchResult1(12);
                    mecz.setUserMatchResult2(21);
                    mecz.setOpponentMatchResult2(22);*/
                        var user = mecz.getUser();
                        var userTeamId = user.getTeamlist().get(0).getTeamId();
                        var oponent = mecz.getopponentUser();
                        var oponentTeamId = oponent.getTeamlist().get(0).getTeamId();

                        var rozegrane = matchService.findPlayedByUsername(user.getUsername());
//rozegrane.getMatches().stream().
                        //   filter(a->a.)
                        var meczeTurniejowe = rozegrane.getMatches().stream().
                                filter(a -> a.getDate().contains(item.getData().toString())).
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


                                //    mecz.setMatchResult1("2");
                                //    mecz.setMatchResult1(rozegranyMecz.getTeamlist().get(1).getGoals()+" : "+rozegranyMecz.getTeamlist().get(0).getGoals());
                            }

                        }
                        //           filter(a->a..equals("friendly")).
                        //                   filter(a->a.getTeamId()== mecz.getopponentUser().getTeamlist().get(0).getTeamId()).
                        //.getTeamId()==mecz.getopponentUser().getTeamlist().get(0).getTeamId()).

                        //  filter(a->a.getType().equals("friendly")).
                        //      filter(a->a.getDate().contains(item.getData().toString())).

                        int y = 0;


                    }


                    //   item.getMecze().

                }
                //   List<Match> matches = matchService.findPlayedByUsername(user.getUsername()).getMatches().stream().filter(a -> LocalDate.parse(a.getDate(), formatter).isAfter(LocalDate.now().minusDays(7))).collect(Collectors.toList());

            }
        }
zapiszDoXml(terminarz, nazwaPliku);

     // jaxbObjectToXML(terminarz);
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

            //tutaj chyba ścieżka ma być ta zakomentowana
            //Store XML to File lk-manager-data/src/main/java/LKManager/skladUPSG.xml

            File file = new File("Data/terminarze/"+nazwaPliku);
           // File file = new File("XMLData/terminarz.xml");

            //Writes XML file to file-system
            jaxbMarshaller.marshal(calyTerminarz, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
/*
    @Override
    public void zapiszDoXml() {
zapiszDoXml();
    }

    @Override
    public void WczytajZXml() {

    }*/

}
