package LKManager.services;

import LKManager.LK.PlayerSummary;
import LKManager.model.RecordsAndDTO.MatchDTO;
import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.Table;
import LKManager.services.Comparators.GraczPodsumowanieComparatorGoalDifference;
import LKManager.services.Comparators.GraczPodsumowanieComparatorGoalLost;
import LKManager.services.Comparators.GraczPodsumowanieComparatorGoalScored;
import LKManager.services.Comparators.GraczPodsumowanieComparatorPoints;
import LKManager.services.RedisService.RedisTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {
 /*   @Autowired
    MZCache mzCache;*/
    @Autowired
    ScheduleService scheduleService;

private final RedisTableService redisTableService;

    public TableServiceImpl(RedisTableService redisTableService) {
        this.redisTableService = redisTableService;
    }

    @Override
    @Transactional
    public Table createTable(long chosenscheduleId)
    {


//schedules are in cache
        /** ****************************
         * todo uncomment if need to use cache

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
            */
        Table table = null;
            try {

            //    ScheduleDTO schedule= scheduleService.getSchedule_ByName(chosenscheduleName);
                ScheduleDTO schedule= scheduleService.getSchedule_ById(chosenscheduleId);
               if(schedule!=null)
                {
                //    mzCache.updateScheduleMatchesCache(schedule);

                    table= new Table(schedule.getName());
                       table.setPlayerSummaries(     this.calculateSwissTable(scheduleService.getAllMatchesOfSchedule(schedule)));//(matchDAOimpl.findAllbyScheduleId(schedule.getId()));
                }

            }
            catch (Exception e )
            {
                System.out.println("nie można pobrać wyników, spróbuj później");
            }



      //  }


        return table;
    }






    @Override
    @Transactional
    public Table createTable(ScheduleDTO chosenschedule)
    {


        Table table = null;
        try {

            //    ScheduleDTO schedule= scheduleService.getSchedule_ByName(chosenscheduleName);
            ScheduleDTO schedule= chosenschedule;
            if(schedule!=null)
            {
                //    mzCache.updateScheduleMatchesCache(schedule);

                table= new Table(schedule.getName());
                table.setPlayerSummaries(     this.calculateTable(scheduleService.getAllMatchesOfSchedule(schedule)));//(matchDAOimpl.findAllbyScheduleId(schedule.getId()));
            }

        }
        catch (Exception e )
        {
            System.out.println("nie można pobrać wyników, spróbuj później");
        }



        //  }


        return table;
    }

    @Override
    @Transactional
    public Table createSwissScheduleTable(ScheduleDTO chosenschedule)
    {


        Table table = null;
        try {

            //    ScheduleDTO schedule= scheduleService.getSchedule_ByName(chosenscheduleName);
            ScheduleDTO schedule= chosenschedule;
            if(schedule!=null)
            {
                //    mzCache.updateScheduleMatchesCache(schedule);

                table= new Table(schedule.getName());
                table.setPlayerSummaries(     this.calculateSwissTable(scheduleService.getAllMatchesOfSchedule(schedule)));//(matchDAOimpl.findAllbyScheduleId(schedule.getId()));
            }

        }
        catch (Exception e )
        {
            System.out.println("nie można pobrać wyników, spróbuj później");
        }



        //  }
redisTableService.setTable(table);

        return table;
    }

    @Transactional
    private List<PlayerSummary> calculateTable(List<MatchDTO>matches) {
    //    Table table = new Table();
        List<PlayerSummary> playerSummaries= new ArrayList<>();

        List<UserDataDTO> users=new  ArrayList<>();

        for (var item: matches
        ) {

            users.add(item.getUserDataDTO());
            users.add(item.getOpponentUserDataDTO());
        }
        List<UserDataDTO> distinctUsers = users.stream()
                .collect(Collectors.toMap(UserDataDTO::getUsername, Function.identity(), (existing, replacement) -> existing))
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
                    tempGracz.setPlayer(a);

                 //   table.getPlayerSummaries().add(tempGracz);
                    playerSummaries.add(tempGracz);
                }

            });


            for (MatchDTO match:matches
            ){
              /*  var   user = table.getPlayerSummaries().stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getUserDataDTO().getUserId())).findFirst().orElse(null);
                var   userOp = table.getPlayerSummaries().stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getOpponentUserDataDTO().getUserId())).findFirst().orElse(null);
*/
                var   user = playerSummaries.stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getUserDataDTO().getUserId())).findFirst().orElse(null);
                var   userOp = playerSummaries.stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getOpponentUserDataDTO().getUserId())).findFirst().orElse(null);

                checkResult(match,user,userOp);

            }
        }

        else
        {
            //todo ze brak danych z db
        }
///////////////////////////////////////////////
        //sortowanie po punktach
      /*  table.getPlayerSummaries().sort(new GraczPodsumowanieComparatorPoints());
        var gracze= table.getPlayerSummaries();*/
        playerSummaries.sort(new GraczPodsumowanieComparatorPoints());
        var gracze= playerSummaries;
      //  boolean nowaRownosc= false;

//indeksy zmian punktów -> i=początek zmiany
        List<Integer> listaIndeksow= new ArrayList<>();
        listaIndeksow.add(0);
        for (int i = 0; i <gracze.size()-1 ; i++) {
            if(!(gracze.get(i+1).getTotalPoints().equals(gracze.get(i).getTotalPoints())))
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




//mzCache.setTable(table);

        return playerSummaries;
    }


    @Transactional
    private List<PlayerSummary> calculateSwissTable(List<MatchDTO>matches) {
        //    Table table = new Table();
        List<PlayerSummary> playerSummaries= new ArrayList<>();

        List<UserDataDTO> users=new  ArrayList<>();

        for (var item: matches
        ) {

            users.add(item.getUserDataDTO());
            users.add(item.getOpponentUserDataDTO());
        }
        List<UserDataDTO> distinctUsers = users.stream()
                .collect(Collectors.toMap(UserDataDTO::getUsername, Function.identity(), (existing, replacement) -> existing))
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
                    tempGracz.setPlayer(a);

                    //   table.getPlayerSummaries().add(tempGracz);
                    playerSummaries.add(tempGracz);
                }

            });


            for (MatchDTO match:matches
            ){
              /*  var   user = table.getPlayerSummaries().stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getUserDataDTO().getUserId())).findFirst().orElse(null);
                var   userOp = table.getPlayerSummaries().stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getOpponentUserDataDTO().getUserId())).findFirst().orElse(null);
*/
                var   user = playerSummaries.stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getUserDataDTO().getUserId())).findFirst().orElse(null);
                var   userOp = playerSummaries.stream().
                        filter(a->a.getPlayer().getUserId().equals(match.getOpponentUserDataDTO().getUserId())).findFirst().orElse(null);

                checkSwissResult(match,user,userOp);

            }
        }

        else
        {
            //todo ze brak danych z db
        }
///////////////////////////////////////////////
        //sortowanie po punktach
      /*  table.getPlayerSummaries().sort(new GraczPodsumowanieComparatorPoints());
        var gracze= table.getPlayerSummaries();*/
        playerSummaries.sort(new GraczPodsumowanieComparatorPoints());
        var gracze= playerSummaries;
        //  boolean nowaRownosc= false;

//indeksy zmian punktów -> i=początek zmiany
        List<Integer> listaIndeksow= new ArrayList<>();
        listaIndeksow.add(0);
        for (int i = 0; i <gracze.size()-1 ; i++) {
            if(!(gracze.get(i+1).getTotalPoints().equals(gracze.get(i).getTotalPoints())))
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




//mzCache.setTable(table);

        return playerSummaries;
    }

    private void checkResult(MatchDTO match, PlayerSummary user, PlayerSummary opponent) {
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
            user.addGoalsConceded(goalsOpponent1);
            user.addGoalsScored(goalsUser1);
            opponent.addGoalsScored(goalsOpponent1);
            opponent.addGoalsConceded(goalsUser1);
        }
        if(goalsUser2!=null && goalsOpponent2!=null)
        {
            managePoints(user,opponent,goalsUser2,goalsOpponent2);
            user.addGoalsConceded(goalsOpponent2);
            user.addGoalsScored(goalsUser2);
            opponent.addGoalsScored(goalsOpponent2);
            opponent.addGoalsConceded(goalsUser2);
        }






    }
    private void checkSwissResult(MatchDTO match, PlayerSummary user, PlayerSummary opponent) {
        //todo sprawdzic null/ ""
        Byte goalsUser1 = null;
        Byte goalsUser2=null;
        Byte goalsOpponent1=null;
        Byte goalsOpponent2=null;




            goalsUser1 = match.getUserMatchResult1();
            goalsUser2 = match.getUserMatchResult2();
            goalsOpponent1 = match.getOpponentMatchResult1();
            goalsOpponent2 = match.getOpponentMatchResult2();





            managePointsSwiss(user,opponent,goalsUser1,goalsOpponent1);
            user.addGoalsConceded(goalsOpponent1);
            user.addGoalsScored(goalsUser1);
            opponent.addGoalsScored(goalsOpponent1);
            opponent.addGoalsConceded(goalsUser1);


        managePointsSwiss(user,opponent,goalsUser2,goalsOpponent2);
            user.addGoalsConceded(goalsOpponent2);
            user.addGoalsScored(goalsUser2);
            opponent.addGoalsScored(goalsOpponent2);
            opponent.addGoalsConceded(goalsUser2);









    }

    private void  managePoints(PlayerSummary user, PlayerSummary opponent, int goalsUser, int goalsOpponent )
    {

        int difference= goalsUser-goalsOpponent;
        if(difference<0)
        {
            opponent.increaseTotalPoints(3);
        }
        else if(difference>0)
        {
            user.increaseTotalPoints(3);
        }
        else // remis nierozegrane ma byc sprawdzone wczesniej !
        {
            opponent.increaseTotalPoints(1);
            user.increaseTotalPoints(1);
        }


    }
    private void  managePointsSwiss(PlayerSummary user, PlayerSummary opponent, Byte goalsUser, Byte goalsOpponent )
    {
        if(goalsUser==null ||goalsOpponent==null)
        {
           /* if(user.getPlayer().getReliability()-opponent.getPlayer().getReliability()>0) {

                user.increaseTotalPoints(1);
            }
            else if(user.getPlayer().getReliability()-opponent.getPlayer().getReliability()<0) {
                opponent.increaseTotalPoints(1);

            }
            else {
              //  opponent.increaseTotalPoints(1);
            //    user.increaseTotalPoints(1);
            }*/

        }
        else {
            int difference= goalsUser-goalsOpponent;
            if(difference<0)
            {
                opponent.increaseTotalPoints(4);
                user.increaseTotalPoints(1);
            }
            else if(difference>0)
            {
                user.increaseTotalPoints(4);
                opponent.increaseTotalPoints(1);
            }
            else // remis nierozegrane ma byc sprawdzone wczesniej !
            {
                opponent.increaseTotalPoints(2);
                user.increaseTotalPoints(2);
            }

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
            if(!(gracze.get(ii+1).getDifference().equals(gracze.get(ii).getDifference())))
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
            if(!(gracze.get(ii+1).getGoalsScored().equals(gracze.get(ii).getGoalsScored())))
            {
                listaIndeksow1.add(ii+1);
            }

        }
        return listaIndeksow1;
    }


}



