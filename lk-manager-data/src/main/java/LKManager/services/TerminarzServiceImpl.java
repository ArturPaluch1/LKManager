package LKManager.services;

import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TerminarzServiceImpl implements TerminarzService {






    @Override
    public void utworzTerminarz(XMLGregorianCalendar data, List<UserData> grajki) throws DatatypeConfigurationException {

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
                XMLGregorianCalendar tempData = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                tempData.setYear(calyTerminarz.get(calyTerminarz.size() - 1).getData().getYear());
                tempData.setMonth(calyTerminarz.get(calyTerminarz.size() - 1).getData().getMonth());
                tempData.setDay(calyTerminarz.get(calyTerminarz.size() - 1).getData().getDay());
                tempData.add(d);

                runda = new Runda(j, tempData);
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


               // tempMatch.setUserName(listyGrajkow.getGrajkiA().get(i).getUsername());
             //   tempMatch.setopponentUser(listyGrajkow.getGrajkiB().get(i).getUsername());
                runda.getMecze().add(tempMatch);

            }
runda.setStatus(Runda.status.nierozegrana);
            calyTerminarz.add(runda);
            System.out.println("=======" + runda.getNr() + " === " + runda.getData());
            listyGrajkow.przesunListy();
            /////////////////////////////////////////////
        }



///////////////////////////////////////////zwykly plik
 /*       FileOutputStream fos2 = new FileOutputStream("terminarz.tmp");
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(calyTerminarz);
        oos2.close();

*/


        /////////////// zapis termnarza do xml    //////////////////////
       Terminarz terminarz = new Terminarz(calyTerminarz);
        jaxbObjectToXML(terminarz);

/////////////////////////////////////////////



    }

    @Override
    public Terminarz wczytajTerminarz(String sciezka) throws JAXBException {
        return jaxbXMLToObject(sciezka);
    }



    protected void dodajPauzeDlaParzystosci(List<UserData> grajki) {
        if (grajki.size() % 2 != 0) {
            UserData tempuser = new UserData();
            tempuser.setUsername("pauza");
            Team tempTeam= new Team();
            tempTeam.setTeamName(" ");
            tempTeam.setTeamId(0);
            tempuser.setTeamlist(tempTeam);
            grajki.add(tempuser);
        }
    }

    protected void jaxbObjectToXML(Terminarz calyTerminarz) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Terminarz.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Store XML to File
            ///////////////////////////////////////////////
           // File targetFile = new File("/Data/skladUPSG.xml");
         /*   InputStream in = this.getClass().getPackageName()
                    getResourceAsStream("/Data/terminarz.xml");
             File file1 = new File(in.toString());
            OutputStream file = new FileOutputStream(file1);
*/
 //  Path source = Paths.get(this.getClass().getResource("/").getPath());
    //        Path newFolder = Paths.get(source.toAbsolutePath() + "/newFolder/");

        //    testText = new String(this.getClass().getResourceAsStream("/test.txt").readAllBytes());
         //   Files.createDirectories(newFolder);
           // File file = new File("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
     /////////////////////////////////////////////////////////////////
         //   File file = new File("lk-manager-web/src/main/java/LKManager/XMLData/terminarz.xml");
            File file = new File("Data/terminarz.xml");

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

        File file = new File(String.valueOf(terminarz));

          if (file.exists()) {

                terminarz1 = (Terminarz) unmarshaller.unmarshal(file);




                System.out.println(terminarz1.getTerminarz());
                return terminarz1;
            }
            else
            {
                return null;
            }




    }

    class ListyGrajkow {
        private List<UserData> grajkiPrzesunieteA;//= new ArrayList<>();
        private List<UserData> grajkiPrzesunieteB;//= new ArrayList<>();
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

            //////////////

            // grajkiA.clear();
            //   grajkiB.clear();
//            for (int i = 0; i < grajkiPrzesunieteA.size(); i++) {
//                grajkiA.set(i,grajkiPrzesunieteA.get(i));
//                grajkiB.set(i,grajkiPrzesunieteB.get(i));
//            }

            grajkiA = grajkiPrzesunieteA;
            grajkiB = grajkiPrzesunieteB;
            //    grajkiPrzesunieteA.clear();
            //    grajkiPrzesunieteB.clear();


            //    return new listyGrajkow();
        }
    }
}
