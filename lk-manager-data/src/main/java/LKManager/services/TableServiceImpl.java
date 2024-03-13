package LKManager.services;

import LKManager.LK.Comparators.GraczPodsumowanieComparatorGoalDifference;
import LKManager.LK.Comparators.GraczPodsumowanieComparatorGoalLost;
import LKManager.LK.Comparators.GraczPodsumowanieComparatorGoalScored;
import LKManager.LK.Comparators.GraczPodsumowanieComparatorPoints;
import LKManager.LK.PlayerSummary;
import LKManager.LK.Schedule;
import LKManager.LK.Table;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {
    @Autowired
    MZCache mzCache;
    @Autowired
    ScheduleService scheduleService;


    @Override
    @Transactional
    public Table createTable(String chosenscheduleName)
    {
        Table table = null;

//schedules are in cache
        if(mzCache.getSchedules().size()!=0)
        {


            String finalChosenScheduleName = chosenscheduleName;
            Schedule schedule =  mzCache.getSchedules().stream().filter(a->a.getName().equals(finalChosenScheduleName)).findFirst().orElse(null);
            List<Match> matches = new ArrayList<>();
            //in cache  exist chosen schedule
            if(schedule!=null)
            {
                if(schedule.checkMatchesInitialization())
                {


                    table=   this.calculateTable(scheduleService.getAllMatchesOfSchedule(schedule));


                    System.out.println("matches initialized , table created");
                }
                else
                {
                    //proba inicjalizacji
                    schedule=scheduleService.getSchedule_ById(schedule.getId());


                    if(!schedule.checkMatchesInitialization())
                    {
                        mzCache.updateScheduleMatchesCache(schedule);
                        table=   this.calculateTable(scheduleService.getAllMatchesOfSchedule(schedule));

                        System.out.println("matches initialized from db, cache updated, table created");
                    }
                    else
                    {
                        System.out.println("else");
                        int y=0;

                    }


                }
            }
            else
            {
                // todo  - probowanie pobrania z bazy danych
                //in cache do not exist chosen schedule
            }




        }
        //todo sprawdzić czy ten warunek w ogóle jest potrzebny, bo wcześniej też jest w scheduleService próbowane cache a potem z bazy
        else  //nie ma schedules w cache
        {
            try {

                Schedule schedule= scheduleService.getSchedule_ByName(chosenscheduleName);
                if(schedule!=null)
                {
                    mzCache.updateScheduleMatchesCache(schedule);
                    table= this.calculateTable(scheduleService.getAllMatchesOfSchedule(schedule));//(matchDAOimpl.findAllbyScheduleId(schedule.getId()));
                }

            }
            catch (Exception e )
            {
                System.out.println("nie można pobrać wyników, spróbuj później");
            }



        }

        return table;
    }









    @Transactional
    private Table calculateTable(List<Match>matches) {
        Table table = new Table();


        List<UserData> users=new  ArrayList<>();

        for (var item: matches
        ) {

            users.add(item.getUserData());
            users.add(item.getOpponentUserData());
        }
        List<UserData> distinctUsers = users.stream()
                .collect(Collectors.toMap(UserData::getUsername, Function.identity(), (existing, replacement) -> existing))
                .values().stream().toList();

// \/nie dzała jeśli obiekty mają te same property ale są innym obiektem
      //  distinctUsers=users.stream().distinct().toList();


      //  users.forEach(u-> u.getUsername());

      //  terminarzDAO.findAllParticipantsOfSchedule(wybranyTerminarz);




        if(distinctUsers.size()!=0 && matches.size()!=0)
        {
            distinctUsers.forEach(a->{
                if(!a.getUsername().equals("pauza"))
                {
                    var tempGracz= new PlayerSummary();
                    tempGracz.setGracz(a);

                    table.getPlayerSummaries().add(tempGracz);
                }

            });


            for (Match match:matches
            ){
                var   user = table.getPlayerSummaries().stream().
                        filter(a->a.getGracz().getUserId().equals(match.getUserData().getUserId())).findFirst().orElse(null);
                var   userOp = table.getPlayerSummaries().stream().
                        filter(a->a.getGracz().getUserId().equals(match.getOpponentUserData().getUserId())).findFirst().orElse(null);

                checkResult(match,user,userOp);

            }
        }

        else
        {
            //todo ze brak danych z db
        }
///////////////////////////////////////////////
        //sortowanie po punktach
        table.getPlayerSummaries().sort(new GraczPodsumowanieComparatorPoints());
        var gracze= table.getPlayerSummaries();
        boolean nowaRownosc= false;

//indeksy zmian punktów -> i=początek zmiany
        List<Integer> listaIndeksow= new ArrayList<>();
        listaIndeksow.add(0);
        for (int i = 0; i <gracze.size()-1 ; i++) {
            if(!(gracze.get(i+1).getSumaPunktow().equals(gracze.get(i).getSumaPunktow())))
            {
                listaIndeksow.add(i+1);
            }

        }

        // sprawdzanie czy punkty są równe i sortowanie po różnicy
        List<PlayerSummary> tempstring=new ArrayList<>();
        for (int i = 0; i <listaIndeksow.size() ; i++) {
            //sortowanie końcówki listy (ze względu na porównanie ostatniego indeksu z i+1)
            if(i==listaIndeksow.size()-1)
            {
                //  var  posortowane=  gracze.subList(listaIndeksow.get(listaIndeksow.size()-1),gracze.size());
                var  posortowane=  gracze.subList(listaIndeksow.get(i),(gracze.size()));
                posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());

                sortGoalsDifference(tempstring, posortowane);


                //sortowanie po strzelonych
                //  posortowane.sort(new GraczPodsumowanieComparatorGoalScored());

            }
            //sortowanie normalnie
            else
            {
                var  posortowane=  gracze.subList(listaIndeksow.get(i),listaIndeksow.get(i+1));
                posortowane.sort(new GraczPodsumowanieComparatorGoalDifference());

                sortGoalsDifference(tempstring, posortowane);


                //sortowanie po strzelonych
                //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
                //  tempstring.addAll(posortowane);
                //sortowanie posortowane  po straconych
            }






        }




mzCache.setTable(table);

        return table;
    }




    private void checkResult(Match match, PlayerSummary user, PlayerSummary opponent) {
        //todo sprawdzic null/ ""
        Byte goalsUser1 = null;
        Byte goalsUser2=null;
        Byte goalsOpponent1=null;
        Byte goalsOpponent2=null;

        if(!(match.getUserMatchResult1()==null || match.getUserMatchResult1()==null))
            goalsUser1 = match.getUserMatchResult1();
        if (!(match.getUserMatchResult2()==null ||  match.getUserMatchResult2()==null))
            goalsUser2 = match.getUserMatchResult2();
        if(!(match.getOpponentMatchResult1()==null ||  match.getOpponentMatchResult1()==null))
            goalsOpponent1 = match.getOpponentMatchResult1();
        if(!(match.getOpponentMatchResult2()==null || match.getOpponentMatchResult2()==null))
            goalsOpponent2 = match.getOpponentMatchResult2();




        if(goalsUser1!=null && goalsOpponent1!=null)
        {
            managePoints(user,opponent,goalsUser1,goalsOpponent1);
            user.addGoleStracone(goalsOpponent1);
            user.addGoleStrzelone(goalsUser1);
            opponent.addGoleStrzelone(goalsOpponent1);
            opponent.addGoleStracone(goalsUser1);
        }
        if(goalsUser2!=null && goalsOpponent2!=null)
        {
            managePoints(user,opponent,goalsUser2,goalsOpponent2);
            user.addGoleStracone(goalsOpponent2);
            user.addGoleStrzelone(goalsUser2);
            opponent.addGoleStrzelone(goalsOpponent2);
            opponent.addGoleStracone(goalsUser2);
        }






    }


    private void  managePoints(PlayerSummary user, PlayerSummary opponent, int goalsUser, int goalsOpponent )
    {

        int difference= goalsUser-goalsOpponent;
        if(difference<0)
        {
            opponent.zwiekszSumePunktow(3);
        }
        else if(difference>0)
        {
            user.zwiekszSumePunktow(3);
        }
        else // remis nierozegrane ma byc sprawdzone wczesniej !
        {
            opponent.zwiekszSumePunktow(1);
            user.zwiekszSumePunktow(1);
        }


    }

    protected void sortGoalsDifference(List<PlayerSummary> tempstring, List<PlayerSummary> posortowane) {
        var listaIndeksowPowtorzonychRoznic = znajdzIndeksyPowtorzonychRoznic(posortowane);

        for (int j = 0; j < listaIndeksowPowtorzonychRoznic.size(); j++) {
            //sortowanie końcówki listy (ze względu na porównanie ostatniego indeksu z i+1)
            if (j == listaIndeksowPowtorzonychRoznic.size() - 1) {



                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychRoznic.get(j), (posortowane.size()));
                // sortowanie po strzelonych
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalScored());

                sortujStracone(tempstring, posortowaneRoznice);
                //  tempstring.addAll(posortowaneRoznice);
            } else {
                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychRoznic.get(j), listaIndeksowPowtorzonychRoznic.get(j + 1));
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalScored());

                sortujStracone(tempstring, posortowaneRoznice);
                //sortowanie po strzelonych
                //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
                //   tempstring.addAll(posortowaneRoznice);
                //sortowanie posortowane  po straconych
            }
        }
    }

    protected void sortujStracone(List<PlayerSummary> tempstring, List<PlayerSummary> posortowane) {
        var listaIndeksowPowtorzonychStrzelonych = znajdzIndeksyPowtorzonychStrzelonych(posortowane);

        for (int j = 0; j < listaIndeksowPowtorzonychStrzelonych.size(); j++) {
            //sortowanie końcówki listy (ze względu na porównanie ostatniego indeksu z i+1)
            if (j == listaIndeksowPowtorzonychStrzelonych.size() - 1) {

                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychStrzelonych.get(j), (posortowane.size()));
                // sortowanie po strzelonych
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalLost());
                tempstring.addAll(posortowaneRoznice);
            } else {
                var posortowaneRoznice = posortowane.subList(listaIndeksowPowtorzonychStrzelonych.get(j), listaIndeksowPowtorzonychStrzelonych.get(j + 1));
                posortowaneRoznice.sort(new GraczPodsumowanieComparatorGoalLost());
                //sortowanie po strzelonych
                //   posortowane.sort(new GraczPodsumowanieComparatorGoalScored());
                tempstring.addAll(posortowaneRoznice);
                //sortowanie posortowane  po straconych
            }
        }
    }




    protected List<Integer> znajdzIndeksyPowtorzonychRoznic(List<PlayerSummary> gracze) {
        List<Integer> listaIndeksow1= new ArrayList<>();
        listaIndeksow1.add(0);
        for (int ii = 0; ii < gracze.size()-1 ; ii++) {
            if(!(gracze.get(ii+1).getRoznica().equals(gracze.get(ii).getRoznica())))
            {
                listaIndeksow1.add(ii+1);
            }

        }
        return listaIndeksow1;
    }
    protected List<Integer> znajdzIndeksyPowtorzonychStrzelonych(List<PlayerSummary> gracze) {
        List<Integer> listaIndeksow1= new ArrayList<>();
        listaIndeksow1.add(0);
        for (int ii = 0; ii < gracze.size()-1 ; ii++) {
            if(!(gracze.get(ii+1).getGoleStrzelone().equals(gracze.get(ii).getGoleStrzelone())))
            {
                listaIndeksow1.add(ii+1);
            }

        }
        return listaIndeksow1;
    }


}



