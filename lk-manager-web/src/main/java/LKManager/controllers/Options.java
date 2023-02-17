package LKManager.controllers;

import LKManager.services.TeamTM;
import LKManager.algorytmPary;
import LKManager.controllers.LKsystemSzwajcarski.Mecz;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.MatchService;
import LKManager.services.UserService;

import org.json.simple.parser.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Controller
public class Options implements Serializable {
    private final UserService userService;
    private final MatchService matchService;

    //  private List<UserData> skladTM = new ArrayList<>();

    public Options(UserService userService, MatchService matchService) {
        this.userService = userService;
        this.matchService = matchService;
    }


    public class GrajekPrzeciwnicy
    {
        Integer id;
         Map<Integer, Integer> przeciwnikWaga = new HashMap<>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Map<Integer, Integer> getPrzeciwnikWaga() {
            return przeciwnikWaga;
        }

        public void setPrzeciwnikWaga(Map<Integer, Integer> przeciwnikWaga) {
            this.przeciwnikWaga = przeciwnikWaga;
        }
    }

    @RequestMapping("/projects")
    @ResponseStatus(value = HttpStatus.OK)
    public String add(Model model) throws ParserConfigurationException, IOException, SAXException, JAXBException, ParseException, ClassNotFoundException {
        //  var team = new TeamTM(userService).LoadCalyUPSG();


//var team = new TeamTM(userService).LoadLKUPSGV();


////////////////////////////////////////////////////////////////////
//            INICJALIZACJA GRAJKOW
// ////////////////////////////////////////////////
        List<GrajekTurniejowy> listaGrajekTurniejowy = new ArrayList<>();
        try {//jest plik
            FileInputStream fis2 = new FileInputStream("gracze.tmp");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            listaGrajekTurniejowy = (List<GrajekTurniejowy>) ois2.readObject();
            ois2.close();


        } catch (Exception e) {
            var team = new TeamTM(userService).LoadTMRzeszow();
            //  team.addAll(new TeamTM(userService).LoadCalyUPSG())
            for (var i : team
            ) {
                var a = new GrajekTurniejowy(i);

                listaGrajekTurniejowy.add(a);
            }


            if (listaGrajekTurniejowy.size() % 2 != 0)//jesli nieparzysta liczba graczy to dodaje wolny
            {
                Team tempTeam = new Team();
                tempTeam.setTeamName("");
                List<Team> tempteamList = new ArrayList<>();
                tempteamList.add(tempTeam);
                UserData ud = new UserData("wolny los", 0, "", "", tempteamList);
                GrajekTurniejowy wl = new GrajekTurniejowy(ud);
                wl.punkty = -99;

                listaGrajekTurniejowy.add(wl);
            }
////////////////////////////////////////////////////////////////////
//           zapis graczy do pliku
// ////////////////////////////////////////////////
            FileOutputStream fos2 = new FileOutputStream("gracze.tmp");
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(listaGrajekTurniejowy);
            oos2.close();
        }


////////////////////////////////////////////////////////////////////
//            INICJALIZACJA TWORZENIE LISTY MECZY
// ////////////////////////////////////////////////
        List<Mecz> meczeList = new ArrayList<>();
/*

        fr.close();
        ObjectMapper mapper1 = new ObjectMapper();
       var map = mapper1.readValue(fr,new TypeReference<List<GrajekTurniejowy>>(){});*/


/*
        String pathGrajki = "Jsony/grajki.json";
        String pathmecze = "Jsony/mecze.json";

        List<GrajekTurniejowy> grajki;



        try {
            FileReader reader= new FileReader(pathGrajki);
            ObjectMapper mapper = new ObjectMapper();
            grajki = mapper.readValue(reader, ArrayList.class);
            FileReader reader1= new FileReader(pathmecze);
            reader1= new FileReader(pathmecze);
         //   meczeList=mapper.readValue(reader1,ArrayList.class);

            FileReader readerMecze= new FileReader(pathmecze);
            ObjectMapper mapper2 = new ObjectMapper();
         //   meczeList=mapper2.readValue(reader1,ArrayList.class);
       meczeList = mapper2.readValue(new File("Jsony/mecze.json"), new TypeReference<List<Mecz>>() {});
            File bob= new File("Jsony/mecze.json");
       // meczeList = mapper.readValue(new File(bob.toString(), new TypeReference<List<Mecz>>(){});
      // meczeList=mapper.readValue(new File("Jsony/mecze.json"), new TypeReference<List<Mecz>>() {});


            System.out.println(meczeList.get(0));
            int r=9;
              //  System.out.println(meczeList);
         //   }



        } catch(Exception e){
            grajki= new ArrayList<>();
        }


        JSONArray array = new JSONArray();
   if(grajki.isEmpty())
     {

*/


//listaGrajekTurniejowy.remove(4);


// LOSOWANIEWYNIKOWQ MECZÓW
        //     JSONArray ja = new JSONArray("[true, \"lorem ipsum\", 215]");
        //   List<Mecz> b=
        //  if()
//  https://www.baeldung.com/java-org-json


////////////////////////////////////////////////////////////////////
//            POBIERANIE Z PLIKU MACIERZY GRACZY I ZAPISANYCH MECZY
// ////////////////////////////////////////////////
        try {//jest plik
            FileInputStream fis = new FileInputStream("mecze.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            meczeList = (List<Mecz>) ois.readObject();
            ois.close();


        } catch (Exception e) {
            ////////////////////////////////////////////////////////////////////
//           nie było pliku neczy, tworzenie pierwszych plików
// ////////////////////////////////////////////////
              /*  meczeList=     ZaktualizujWyniki(meczeList);

                meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,1);
                listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                meczeList=     ZaktualizujWyniki(meczeList);
*/
            Macierze m = new Macierze(listaGrajekTurniejowy);
            m.ZrobBazowaMacierz(listaGrajekTurniejowy.size());

            FileOutputStream fos1 = new FileOutputStream("bazoweUstawieniaMacierzy.tmp");
            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
            oos1.writeObject(m);
            oos1.close();

/*
           algorytmKomiwojazer terminarz = new algorytmKomiwojazer(m);
          terminarz.wykonaj();


            FileOutputStream fos = new FileOutputStream("mecze.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(meczeList);
            oos.close();
*/

        }
////////////////////////////////////////////////////////////////////
//            POBIERANIE Z PLIKU MACIERZY GRACZY I ZAPISANYCH MECZY
// ////////////////////////////////////////////////
        FileInputStream fis1 = new FileInputStream("bazoweUstawieniaMacierzy.tmp");
        ObjectInputStream ois1 = new ObjectInputStream(fis1);
        Macierze m = (Macierze) ois1.readObject();
        ois1.close();


        //

         m = new Macierze(listaGrajekTurniejowy);
        m.ZrobBazowaMacierz(listaGrajekTurniejowy.size());
        //

        //tworzenie tablicy do algorytmu

GrajekPrzeciwnicy[] tab= new GrajekPrzeciwnicy[listaGrajekTurniejowy.size()];
  //      GrajekPrzeciwnicy[] tab2= new GrajekPrzeciwnicy[listaGrajekTurniejowy.size()];
   /*     for (int i = 0; i < listaGrajekTurniejowy.size(); i++) {
            tab[i]= new GrajekPrzeciwnicy();
        }*/
        for(var i = 0; i < listaGrajekTurniejowy.size(); i++ )
        {
          /*  GrajekPrzeciwnicy g = new GrajekPrzeciwnicy();*/
            tab[i]= new GrajekPrzeciwnicy();
            tab[i].id=i;
            for( var j = 0; j < listaGrajekTurniejowy.size(); j++ )
            {
                if(m.A[i][j])
                {
                    tab[i].przeciwnikWaga.put(j, m.W[i][j]);
                }



            }


        }



//sortowanie wg wag
        for (var grajek:tab
        ) {
            //    Set<Map.Entry<Integer,Integer>> entrySet= grajek.przeciwnikWaga.entrySet();
            //     List<Map.Entry<Integer,Integer>> list= new ArrayList<>(entrySet);
            //   Collections.sort(list, new SortByValue());
            //   grajek.przeciwnikWaga.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
            //  grajek.przeciwnikWaga= new ArrayList<>(map.values());
            //Collections.sort(list.);

            var  result=   grajek.getPrzeciwnikWaga().entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            grajek.setPrzeciwnikWaga(result);

        }



    /*    for (int i = 0; i <tab.length ; i++) {
            tab2[i].id=i+100;
        }
*/
        List<Integer> wejscie= new ArrayList<>();

        for (int i = 0; i < tab.length; i++) {
         //   for (int j = 0; j < tab[i].getPrzeciwnikWaga().size(); j++) {

            for (int j=tab[i].getPrzeciwnikWaga().size()-1 ; j >= 0; j--) {
                wejscie.add(i);
                wejscie.add(((Integer) tab[i].getPrzeciwnikWaga().keySet().toArray()[j]+listaGrajekTurniejowy.size()));
            }
        }
        for (int y:wejscie
             ) {
            System.out.println(y);
        }

/*
class SortByValue implements Comparator <Map.Entry<Integer, Integer>>
{

    @Override
    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}*/

       algorytmPary alg = new algorytmPary(wejscie, listaGrajekTurniejowy);
meczeList=alg.ZrobTerminarz(1);

        meczeList=     ZaktualizujWyniki(meczeList);

        ///////////////////////////////////////////////  next
        m.AktalizujMacierze(listaGrajekTurniejowy,m);

        GrajekPrzeciwnicy[] tab1= new GrajekPrzeciwnicy[listaGrajekTurniejowy.size()];
        //      GrajekPrzeciwnicy[] tab2= new GrajekPrzeciwnicy[listaGrajekTurniejowy.size()];
   /*     for (int i = 0; i < listaGrajekTurniejowy.size(); i++) {
            tab[i]= new GrajekPrzeciwnicy();
        }*/
        for(var i = 0; i < listaGrajekTurniejowy.size(); i++ )
        {
            /*  GrajekPrzeciwnicy g = new GrajekPrzeciwnicy();*/
            tab1[i]= new GrajekPrzeciwnicy();
            tab1[i].id=i;
            for( var j = 0; j < listaGrajekTurniejowy.size(); j++ )
            {
                if(m.A[i][j])
                {
                    tab1[i].przeciwnikWaga.put(j, m.W[i][j]);
                }



            }


        }



//sortowanie wg wag
        for (var grajek:tab1
        ) {
            //    Set<Map.Entry<Integer,Integer>> entrySet= grajek.przeciwnikWaga.entrySet();
            //     List<Map.Entry<Integer,Integer>> list= new ArrayList<>(entrySet);
            //   Collections.sort(list, new SortByValue());
            //   grajek.przeciwnikWaga.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
            //  grajek.przeciwnikWaga= new ArrayList<>(map.values());
            //Collections.sort(list.);

            var  result=   grajek.getPrzeciwnikWaga().entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            grajek.setPrzeciwnikWaga(result);

        }



    /*    for (int i = 0; i <tab.length ; i++) {
            tab2[i].id=i+100;
        }
*/
        List<Integer> wejscie1= new ArrayList<>();

        for (int i = 0; i < tab1.length; i++) {
            //   for (int j = 0; j < tab[i].getPrzeciwnikWaga().size(); j++) {

            for (int j=tab1[i].getPrzeciwnikWaga().size()-1 ; j >= 0; j--) {
                wejscie1.add(i);
                wejscie1.add(((Integer) tab1[i].getPrzeciwnikWaga().keySet().toArray()[j]+listaGrajekTurniejowy.size()));
            }
        }
        for (int y:wejscie1
        ) {
            System.out.println(y);
        }

        algorytmPary alg1 = new algorytmPary(wejscie1, listaGrajekTurniejowy);
       // meczeList.clear();
       var nowemecze=alg1.ZrobTerminarz(2);

meczeList.addAll(nowemecze);



///////////////next///////////////////////////////





    //    Collections.sort(employeeById);



//symulacja
                //    meczeList=     ZaktualizujWyniki(meczeList);
//sortowanie

                /*      listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                     // Macierze m= new Macierze(listaGrajekTurniejowy);
              //aktualizowanie macierzy
                      m.AktalizujMacierze(listaGrajekTurniejowy,m);
              //obliczanie nowego terminarza   w oparciu o macierze
                      algorytmKomiwojazer terminarz = new algorytmKomiwojazer(m);
                      terminarz.wykonaj();
              //aktualizowanie listy meczy
                      for (int i = 0; i < terminarz.getS().length; i++) {
              if(i%2!=0)
              {
                  var a = listaGrajekTurniejowy.get(terminarz.getS()[i-1]);
                  var b        =listaGrajekTurniejowy.get(terminarz.getS()[i]);
                  meczeList.add(new Mecz(a,b,1));
                  a.listaPrzeciwnikow.add(b.grajek.getUsername());
                 b.listaPrzeciwnikow.add(a.grajek.getUsername());
              }
                      }

                      meczeList=     ZaktualizujWyniki(meczeList);

              */
                ////////////////////////////kolejne iteracje
                /*     listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                     m.AktalizujMacierze(listaGrajekTurniejowy,m);
                     algorytmKomiwojazer terminarz1 = new algorytmKomiwojazer(m);
                     terminarz1.wykonaj();
                     for (int i = 0; i < terminarz1.getS().length; i++) {
                         if(i%2!=0)
                         {
                             var a = listaGrajekTurniejowy.get(terminarz1.getS()[i-1]);
                             var b        =listaGrajekTurniejowy.get(terminarz1.getS()[i]);
                             meczeList.add(new Mecz(a,b,2));
                             a.listaPrzeciwnikow.add(b.grajek.getUsername());
                             b.listaPrzeciwnikow.add(a.grajek.getUsername());
                         }
                     }
                     meczeList=     ZaktualizujWyniki(meczeList);
                     ////////////////////////////kolejne iteracje
                   listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                     m.AktalizujMacierze(listaGrajekTurniejowy,m);
                     algorytmKomiwojazer terminarz2 = new algorytmKomiwojazer(m);
                     terminarz2.wykonaj();
                     for (int i = 0; i < terminarz2.getS().length; i++) {
                         if(i%2!=0)
                         {
                             var a = listaGrajekTurniejowy.get(terminarz2.getS()[i-1]);
                             var b        =listaGrajekTurniejowy.get(terminarz2.getS()[i]);
                             meczeList.add(new Mecz(a,b,3));
                             a.listaPrzeciwnikow.add(b.grajek.getUsername());
                             b.listaPrzeciwnikow.add(a.grajek.getUsername());
                         }
                     }
                     meczeList=     ZaktualizujWyniki(meczeList);

             */
                ////////////////////////////kolejne iteracje
                /*     listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                     m.AktalizujMacierze(listaGrajekTurniejowy,m);
                     algorytmKomiwojazer terminarz3 = new algorytmKomiwojazer(m);
                     terminarz3.wykonaj();
                     for (int i = 0; i < terminarz3.getS().length; i++) {
                         if(i%2!=0)
                         {
                             var a = listaGrajekTurniejowy.get(terminarz3.getS()[i-1]);
                             var b        =listaGrajekTurniejowy.get(terminarz3.getS()[i]);
                             meczeList.add(new Mecz(a,b,4));
                             a.listaPrzeciwnikow.add(b.grajek.getUsername());
                             b.listaPrzeciwnikow.add(a.grajek.getUsername());
                         }
                     }
                     meczeList=     ZaktualizujWyniki(meczeList);

                     ////////////////////////////kolejne iteracje
                     listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                     m.AktalizujMacierze(listaGrajekTurniejowy,m);
                     algorytmKomiwojazer terminarz5 = new algorytmKomiwojazer(m);
                     terminarz5.wykonaj();
                     for (int i = 0; i < terminarz5.getS().length; i++) {
                         if(i%2!=0)
                         {
                             var a = listaGrajekTurniejowy.get(terminarz5.getS()[i-1]);
                             var b        =listaGrajekTurniejowy.get(terminarz5.getS()[i]);
                             meczeList.add(new Mecz(a,b,5));
                             a.listaPrzeciwnikow.add(b.grajek.getUsername());
                             b.listaPrzeciwnikow.add(a.grajek.getUsername());
                         }
                     }
                     meczeList=     ZaktualizujWyniki(meczeList);

                     ////////////////////////////kolejne iteracje
                     listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                     m.AktalizujMacierze(listaGrajekTurniejowy,m);
                     algorytmKomiwojazer terminarz6 = new algorytmKomiwojazer(m);
                     terminarz6.wykonaj();
                     for (int i = 0; i < terminarz6.getS().length; i++) {
                         if(i%2!=0)
                         {
                             var a = listaGrajekTurniejowy.get(terminarz6.getS()[i-1]);
                             var b        =listaGrajekTurniejowy.get(terminarz6.getS()[i]);
                             meczeList.add(new Mecz(a,b,6));
                             a.listaPrzeciwnikow.add(b.grajek.getUsername());
                             b.listaPrzeciwnikow.add(a.grajek.getUsername());
                         }
                     }
                     meczeList=     ZaktualizujWyniki(meczeList);*/

                ////////////////////////////kolejne iteracje
                /*       listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                       m.AktalizujMacierze(listaGrajekTurniejowy,m);
                       algorytmKomiwojazer terminarz7 = new algorytmKomiwojazer(m);
                       terminarz7.wykonaj();
                       for (int i = 0; i < terminarz7.getS().length; i++) {
                           if(i%2!=0)
                           {
                               var a = listaGrajekTurniejowy.get(terminarz7.getS()[i-1]);
                               var b        =listaGrajekTurniejowy.get(terminarz7.getS()[i]);
                               meczeList.add(new Mecz(a,b,7));
                               a.listaPrzeciwnikow.add(b.grajek.getUsername());
                               b.listaPrzeciwnikow.add(a.grajek.getUsername());
                           }
                       }
                       meczeList=     ZaktualizujWyniki(meczeList);

                       ////////////////////////////kolejne iteracje
                       listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                       m.AktalizujMacierze(listaGrajekTurniejowy,m);
                       algorytmKomiwojazer terminarz8 = new algorytmKomiwojazer(m);
                       terminarz8.wykonaj();
                       for (int i = 0; i < terminarz8.getS().length; i++) {
                           if(i%2!=0)
                           {
                               var a = listaGrajekTurniejowy.get(terminarz8.getS()[i-1]);
                               var b        =listaGrajekTurniejowy.get(terminarz8.getS()[i]);
                               meczeList.add(new Mecz(a,b,8));
                               a.listaPrzeciwnikow.add(b.grajek.getUsername());
                               b.listaPrzeciwnikow.add(a.grajek.getUsername());
                           }
                       }
                       meczeList=     ZaktualizujWyniki(meczeList);

                       ////////////////////////////kolejne iteracje
                       listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                       m.AktalizujMacierze(listaGrajekTurniejowy,m);
                       algorytmKomiwojazer terminarz9 = new algorytmKomiwojazer(m);
                       terminarz9.wykonaj();
                       for (int i = 0; i < terminarz9.getS().length; i++) {
                           if(i%2!=0)
                           {
                               var a = listaGrajekTurniejowy.get(terminarz9.getS()[i-1]);
                               var b        =listaGrajekTurniejowy.get(terminarz9.getS()[i]);
                               meczeList.add(new Mecz(a,b,9));
                               a.listaPrzeciwnikow.add(b.grajek.getUsername());
                               b.listaPrzeciwnikow.add(a.grajek.getUsername());
                           }
                       }
                       meczeList=     ZaktualizujWyniki(meczeList);

               */

                // String pathGrajki1 = "Jsony/grajki.json";
                //    FileReader fr = new FileReader(pathGrajki1);

                //String out = "";


/*
       JSONArray grajkiJson = new JSONArray();
       JSONArray meczeJson ;

       //   JSONObject jo = new JSONObject(listaGrajekTurniejowy.get(0));
       //  String bob="bob";


          meczeJson = new JSONArray(Arrays.asList(meczeList));


         grajkiJson.put(listaGrajekTurniejowy);

      //   JSONArray jsonA = new JSONArray(meczeList);

    //    mapper = new ObjectMapper();

       FileWriter a = new FileWriter(pathGrajki);
         FileWriter b = new FileWriter(pathmecze);
       a.write(grajkiJson.toString());
       b.write(meczeJson.toString());
       a.flush();
       b.flush();
       a.close();
       b.close();*/
/*

       String jsonArray = mapper.writeValueAsString(listaGrajekTurniejowy);

*/


                //  }


                /*  meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,2);

                    meczeList=     ZaktualizujWyniki(meczeList);
                    listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                    meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,3);

                    meczeList=     ZaktualizujWyniki(meczeList);
                    listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                    meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,4);
                    meczeList=     ZaktualizujWyniki(meczeList);
                    listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                    meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,5);
                    meczeList=     ZaktualizujWyniki(meczeList);
                    listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                    meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,6);
                    meczeList=     ZaktualizujWyniki(meczeList);
                    listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);*/
                /*   meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,7);
                  meczeList=     ZaktualizujWyniki(meczeList);
                  listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                  meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,8);
                  meczeList=     ZaktualizujWyniki(meczeList);
                  listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                  meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,9);
                  meczeList=     ZaktualizujWyniki(meczeList);
                  listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
                 meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,10);
                  meczeList=     ZaktualizujWyniki(meczeList);
                 meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,11);
                  meczeList=     ZaktualizujWyniki(meczeList);
                  meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,12);
                  meczeList=     ZaktualizujWyniki(meczeList);
                  meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,13);
                  meczeList=     ZaktualizujWyniki(meczeList);
                  meczeList=    ustalMecze(listaGrajekTurniejowy, meczeList,14);*/


//listaGrajekTurniejowy.get(0).punkty
                //    var b=meczeList.get(0). grajekTurniejowy1.punkty;
                        model.addAttribute("mecze", meczeList);
        model.addAttribute("grajki", listaGrajekTurniejowy);

//model.addAttribute()
        return "Options/index";
    }


public class Macierze implements Serializable
{
    private   int n;
    private boolean[] visited;
   private  boolean[][] A;
  private   int[][] W;
private  List<GrajekTurniejowy> listaGrajekTurniejowy;

    public Macierze(List<GrajekTurniejowy> listaGrajekTurniejowy) {
        this.n = listaGrajekTurniejowy.size();
        this.listaGrajekTurniejowy = listaGrajekTurniejowy;





    }

    public void ZrobBazowaMacierz(int n) throws IOException {
        visited= new boolean [n];
        A = new boolean[n][n];
        W = new int[n][n];

        for(var i = 0; i < n; i++ )
        {
            A [ i ] = new boolean [ n ];
            W [ i ] = new int [ n ];
            for( var j = 0; j < n; j++ )
            {


               if(i==j)// grajek=grajek - nie moze sam ze sobą
                {
                    A [ i ][ j ]=A [ j ][ i ] = false;
                  //  W [ i ][ j ] = W [ j ][ i ]= 255;
                }
/*else if(j%2!=0&&j-i==1 ||i%2!=0&&i-j==1)
               {
                  // int yiu=0;
                   A [ i ][ j ]=A [ j ][ i ] = false;


               //         yiu=0;

               }*/
                else
                {
                    A [ i ][ j ] =A [ j ][ i ]= true;
                    W [ i ][ j ] =W [ j ][ i ]= abs(i-j);
                }
            }

            visited [ i ] = false;
        }



    }



    public int getN() {
        return n;
    }

    public boolean[] getVisited() {
        return visited;
    }

    public void setVisited(boolean[] visited) {
        this.visited = visited;
    }

    public boolean[][] getA() {
        return A;
    }

    public void setA(boolean[][] a) {
        A = a;
    }

    public int[][] getW() {
        return W;
    }

    public void setW(int[][] w) {
        W = w;
    }

    protected void AktalizujMacierze(List<GrajekTurniejowy> lgt, Macierze m) {
        this.listaGrajekTurniejowy= lgt;
        this.n=lgt.size();
       visited= new boolean [n];
        A = m.getA();
         W = m.getW();

        for(var i = 0; i < n; i++ )
        {
            System.out.println("aktualizuje macierze i-" +i);
          //  A [ i ] = new boolean [ n ];
           // W [ i ] = new int [ n ];
            for( var j = 0; j < n; j++ )
            {
                System.out.println("aktualizuje macierze j-" +j);
                //to bylo wyznaczone w bazowej
               if(i==j)// grajek=grajek - nie moze sam ze sobą
                {
                   A [ i ][ j ] = false;
                 //   W [ i ][ j ] = 255;*/
                }
               else if(listaGrajekTurniejowy.get(j).listaPrzeciwnikow.contains(listaGrajekTurniejowy.get(i).grajek.getUsername())||
                       listaGrajekTurniejowy.get(i).listaPrzeciwnikow.contains(listaGrajekTurniejowy.get(j).grajek.getUsername()
                       ))
                {
                   A [ i ][ j ] = false;
             //       W [ i ][ j ] = 255;
                  A [ j ][ i ] = false;
              //      W [ j ][ i ] = 255;
                }
               else
                {
                    int abs =abs(i-j);
               /*     if(abs>10)
                    {
                        A [ i ][ j ] = false;
                       // W [ i ][ j ] = abs(i-j);
                        A [ j ][ i ] = false;
                     //   W [ j ][i ] = abs(i-j);
                    }
                    else
                    {*/
                     //   A [ i ][ j ] = true;
                        W [ i ][ j ] = abs(i-j);
                    //    A [ j ][ i ] = true;
                        W [ j ][i ] = abs(i-j);
               //     }

                }
            }
            visited [ i ] = false;
        }
m.A=this.A;
m.W= this.W;
int i=9;
    }
}



    @RequestMapping({"Options.html", "options", "options.html", "Options.html"} )
    public String index(Model model) throws IOException, JAXBException, ParserConfigurationException, SAXException {




        return "Options/index";
    }

    protected List<Mecz> ZaktualizujWyniki(List<Mecz> meczeList) {
        for (int k = 0; k< meczeList.size(); k++)
        {
            var mecz = meczeList.get(k);
if(mecz.getWynik1()=="" && mecz.getWynik2()=="" )
{
    //todo
    //zaladuj mecz po username
    //filtrowanie po dadzie wynik-> Match.user goals Match.opponent.goals
    //tutaj lisowanie wyniku mecz 0:0-3:3
    var user = mecz.getGrajekTurniejowy1();
   Integer userW1= LosujwynikMeczu();
    Integer userW2= LosujwynikMeczu();
    ////////////////////////////
 if(mecz.getGrajekTurniejowy2().grajek.getUsername()=="wolny los")
{
    mecz.setWynik1("wolny los -3:0");
    mecz.getGrajekTurniejowy1().punkty+=3;
}
 else
 {
     if(userW1==userW2)
     {
         user.punkty++;
         mecz.getGrajekTurniejowy2().punkty++;
         //todo
         //zamienic na wynik na podstawie xmla
         mecz.setWynik1(userW1.toString()+" : "+ userW2);
     }
     else if(userW1>userW2)
     {
         mecz.setWynik1(userW1 +" : "+ userW2);
         user.punkty+=3;
     }
     else if(userW1<userW2)
     {
         mecz.setWynik1(userW1 +" : "+ userW2);
         mecz.getGrajekTurniejowy2().punkty+=3;
     }
     else
     {
    //    int i=9;
         //nie bylo meczu
     }
 }


}

else
{
//int i=9;
}
        }
        return meczeList;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Mecz> ustalMecze(List<GrajekTurniejowy> listaGrajekTurniejowy, List<Mecz> meczeList, int runda) {
       // meczeList=   TworzTerminarz2(listaGrajekTurniejowy, meczeList, runda);


        boolean bylWolnyGlos=false;
if(runda!=1)
{
    for (var grajek:listaGrajekTurniejowy
         ) {

 if(grajek.wolnyLos)
 {
bylWolnyGlos=true;
break;
 }

    }
}


        listaGrajekTurniejowy=SortujGrajkow(listaGrajekTurniejowy);
if(bylWolnyGlos)
{
    if(!listaGrajekTurniejowy.get(listaGrajekTurniejowy.size() - 1).wolnyLos)
    {
        meczeList=   TworzTerminarz2(listaGrajekTurniejowy, meczeList, runda);
    }
       else
    {
        for(int i=listaGrajekTurniejowy.size();  i >0; i--)
        {
            if(!listaGrajekTurniejowy.get(i - 1).wolnyLos)
            {
                GrajekTurniejowy temp=listaGrajekTurniejowy.get(i-1);
                listaGrajekTurniejowy.remove(i-1);
                listaGrajekTurniejowy.add(temp);
                meczeList=   TworzTerminarz2(listaGrajekTurniejowy, meczeList, runda);
                if(i==1)
                {
                    int yyyyy=9;
                }
                break;

            }


/*     if(i==1)
            {
                int j=9;
            }*/



        }

    }

}
else
{

    meczeList=   TworzTerminarz2(listaGrajekTurniejowy, meczeList, runda);
}




        return meczeList;
    }
    private List<Mecz> TworzTerminarz2(List<GrajekTurniejowy> listaGrajekTurniejowy, List<Mecz> meczeList, int runda) {

      /*  if(listaGrajekTurniejowy.size()%2!=0)//z wolnym losem
        {
           //todo
*/
            szukaj(listaGrajekTurniejowy, meczeList,runda);
/*
int i=9;
        }
else //bez wolnego losu
        {

            for (int j = 0; j< listaGrajekTurniejowy.size(); j++ ) {
                int idPrzeciwnika= SprawdzPrzeciwnika(listaGrajekTurniejowy.get(j),j,listaGrajekTurniejowy);





            }



        }
*/







   return meczeList;
    }

    private void szukaj(List<GrajekTurniejowy> listaGrajekTurniejowy, List<Mecz> meczeList, int runda) {
        Team tempTeam= new Team();
        tempTeam.setTeamName("");
        List<Team> tempteamList =  new ArrayList<>();
        tempteamList.add(tempTeam);
        UserData ud =  new UserData("wolny los", 0, "", "",tempteamList );
        GrajekTurniejowy  wl = new GrajekTurniejowy(ud);
        wl.punkty=0;
        if(listaGrajekTurniejowy.size()%2!=0)//jesli nieparzysta liczba graczy to dodaje wolny
        {


           listaGrajekTurniejowy.add(wl);
        }






        List<GrajekTurniejowy>  tempListaGrajekTurniejowy=new ArrayList<>();
        tempListaGrajekTurniejowy.addAll(listaGrajekTurniejowy);
           /*     for (var grajek: listaGrajekTurniejowy
                ) {*/
        boolean UdaloSie =false;

        List<Mecz> tempMeczeList= new ArrayList<>();

        while(!UdaloSie)
        {
            while(!tempListaGrajekTurniejowy.isEmpty())
            {
                   /*     if(tempListaGrajekTurniejowy.size()!=1)//niepotrzebne chyba
                            {*/
                //
                int idPrzeciwnika= SprawdzPrzeciwnika(tempListaGrajekTurniejowy.get(0),0, tempListaGrajekTurniejowy);
                //ostatniemu zabraklo różnej pary
                if(idPrzeciwnika==-2)
                {
                    tempListaGrajekTurniejowy.clear();
                    tempListaGrajekTurniejowy.addAll(listaGrajekTurniejowy);
                    tempMeczeList.clear();
                    // ustawianie meczu dla 2 ostatnich (poza wolnym losem)
                    for(int i=listaGrajekTurniejowy.size()-2;  i >=0; i--)
                    {
                        if(!listaGrajekTurniejowy.get(listaGrajekTurniejowy.size()-1).listaPrzeciwnikow.contains(listaGrajekTurniejowy.get(i).grajek.getUsername()))
                        {
                            tempMeczeList.add(new Mecz(listaGrajekTurniejowy.get(listaGrajekTurniejowy.size()-1),listaGrajekTurniejowy.get(i),runda));

                            tempListaGrajekTurniejowy.remove(listaGrajekTurniejowy.get(listaGrajekTurniejowy.size()-1));
                            tempListaGrajekTurniejowy.remove(listaGrajekTurniejowy.get(i));
                           break;
                        }
                    }

                   //todo tutaj trzeba sprawdzic czy ostatni moze miec wolny los!!!!
                   // int idPrzeciwnika= SprawdzPrzeciwnika(tempListaGrajekTurniejowy.get(0),0, tempListaGrajekTurniejowy);



                    UdaloSie=false;
               //     break;

                }
                else if(idPrzeciwnika==-1)
                {
                    int rr=0;
                }
                else
                {
                    tempMeczeList.add(new Mecz(tempListaGrajekTurniejowy.get(0),tempListaGrajekTurniejowy.get(idPrzeciwnika),runda));
                    tempListaGrajekTurniejowy.remove(idPrzeciwnika);
                    tempListaGrajekTurniejowy.remove(tempListaGrajekTurniejowy.get(0));
                }



                //  break;
                //     szukaj(listaGrajekTurniejowy, meczeList);
 /*                           }
                        else{
                            //
                            tempListaGrajekTurniejowy.get(0).wolnyLos = true;
                            tempListaGrajekTurniejowy.get(0).listaPrzeciwnikow.add(wl.getGrajek().getUsername());
                            meczeList.add(  new Mecz(tempListaGrajekTurniejowy.get(0), wl, runda));
                            tempListaGrajekTurniejowy.remove(tempListaGrajekTurniejowy.get(0));
                        }*/

            }

meczeList.addAll(tempMeczeList);
            UdaloSie=true;


        }



                       //

                       /*   }*/

                       int y=9;



    }

    int SprawdzPrzeciwnika(GrajekTurniejowy grajek, int sprawdzanyIdPrzeciwnika,List<GrajekTurniejowy>listagrajkow)
    {
int id=-1;
        if(listagrajkow.size()<2)
        {
            return -1;
        }
        else
        {
            if(grajek.listaPrzeciwnikow.contains(listagrajkow.get(sprawdzanyIdPrzeciwnika+1).grajek.getUsername()))
            {
             if(listagrajkow.size()==2)
                 return -2;
             else
                id=SprawdzPrzeciwnika(grajek, sprawdzanyIdPrzeciwnika+1,listagrajkow);

            }
            else
            { grajek.listaPrzeciwnikow.add(listagrajkow.get(sprawdzanyIdPrzeciwnika+1).grajek.getUsername());
                listagrajkow.get(sprawdzanyIdPrzeciwnika+1).listaPrzeciwnikow.add(grajek.getGrajek().getUsername());
                return   sprawdzanyIdPrzeciwnika+1;
            }
        }

        return id;

//return -1;

    }

    private List<Mecz> TworzTerminarz(List<GrajekTurniejowy> listaGrajekTurniejowy, List<Mecz> meczeList, int runda) {
        List<Team> tempteamList =  new ArrayList<>();
        Team tempTeam= new Team();
        tempTeam.setTeamName("");
        tempteamList.add(tempTeam);
        UserData ud =  new UserData("wolny los", 0, "", "",tempteamList );
        GrajekTurniejowy wl = new GrajekTurniejowy(ud);
        //////////////////////////////////////////////////////////////////

        if(listaGrajekTurniejowy.size()%2!=0)
        {
           /* for (int j=0;j<listaGrajekTurniejowy.size();j++ ) {

                if(j%2==0) {
                    if(j==listaGrajekTurniejowy.size())
                        meczeList.add(  new Mecz(listaGrajekTurniejowy.get(j), wl, 1));
                    else
                        meczeList.add(new Mecz(listaGrajekTurniejowy.get(j), listaGrajekTurniejowy.get(j+1), 1));

                }
            }*/

            for (int j = 1; j<= listaGrajekTurniejowy.size(); j+=2 ) {

            try
            {
                meczeList.add(new Mecz(listaGrajekTurniejowy.get(j), listaGrajekTurniejowy.get(j-1), runda));

            }
            catch (Exception e)
            {

                listaGrajekTurniejowy.get(j-1).wolnyLos = true;
                meczeList.add(  new Mecz(listaGrajekTurniejowy.get(j-1), wl, runda));


            }




            }
            return meczeList;
        }
        else
        {
            for (int j = 1; j< listaGrajekTurniejowy.size(); j+=2 ) {



                    meczeList.add(new Mecz(listaGrajekTurniejowy.get(j), listaGrajekTurniejowy.get(j-1), runda ));


            }



        }
        return meczeList;
    }


    List <GrajekTurniejowy> SortujGrajkow( List <GrajekTurniejowy> grajekTurniejowyList)
    {
        grajekTurniejowyList.sort( (GrajekTurniejowy d1,GrajekTurniejowy d2)->
        {
            return d2.punkty-d1.punkty;
        }
        );
        return  grajekTurniejowyList;
    }


    public   class GrajekTurniejowy implements  Serializable
    {
        UserData grajek;
        boolean wolnyLos= false;

        int punkty=0;
List<String> listaPrzeciwnikow = new ArrayList<>();
        public GrajekTurniejowy(UserData grajek) {
            this.grajek = grajek;
        }

        public GrajekTurniejowy(UserData grajek, boolean wolnyLos, int punkty, List<String> listaPrzeciwnikow) {
            this.grajek = grajek;
            this.wolnyLos = wolnyLos;
            this.punkty = punkty;
            this.listaPrzeciwnikow = listaPrzeciwnikow;
        }

        public UserData getGrajek() {
            return grajek;
        }

        public List<String> getListaPrzeciwnikow() {
            return listaPrzeciwnikow;
        }

        public void setListaPrzeciwnikow(List<String> listaPrzeciwnikow) {
            this.listaPrzeciwnikow = listaPrzeciwnikow;
        }

        public void setGrajek(UserData grajek) {
            this.grajek = grajek;
        }

        public boolean isWolnyLos() {
            return wolnyLos;
        }

        public void setWolnyLos(boolean wolnyLos) {
            this.wolnyLos = wolnyLos;
        }

        public int getPunkty() {
            return punkty;
        }

        public void setPunkty(int punkty) {
            this.punkty = punkty;
        }
    }

    Integer LosujwynikMeczu()
    {
        var wyniki = new Integer[]{0,1,3};
        Integer rnd = new Random().nextInt(wyniki.length);
        return  wyniki[rnd];
    }




}
