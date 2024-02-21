package LKManager.DAO;

import LKManager.LK.Schedule;
import LKManager.model.MatchesMz.Match;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class MatchDAOImpl implements CustomMatchDAO {

    private final EntityManager entityManager;
    @Autowired
    public MatchDAOImpl(EntityManager entityManager, RoundDAO roundDAO) {
        this.entityManager = entityManager;
        this.roundDAO = roundDAO;
    }
private final RoundDAO roundDAO;
    @Autowired
    private SessionFactory sessionFactory;



    @Transactional
    public List<Match> findAll() {
        //todo to jest zle naprawic

    /*    Session s= sessionFactory.openSession();

        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Terminarz> cq = cb.createQuery(Terminarz.class);
        Root<Terminarz> rootEntry = cq.from(Terminarz.class);
        CriteriaQuery<Terminarz> all = cq.select(rootEntry);

        TypedQuery<Terminarz> allQuery = s.createQuery(all);
        //todo zamknac sesje
        return allQuery.getResultStream().collect(Collectors.toList());*/
    /*    Session s= sessionFactory.openSession();
        List<Terminarz> allQuery= null;
try{
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<Terminarz> cq = cb.createQuery(Terminarz.class);
    Root<Terminarz> rootEntry = cq.from(Terminarz.class);
    CriteriaQuery<Terminarz> all = cq.select(rootEntry);

     allQuery = s.createQuery(all).getResultStream().collect(Collectors.toList());
     s.getTransaction().commit();

}
catch (Exception e)
{

}
finally {
    s.getSession().close();
}*/
        List<Match> allQuery= null;
        Session s= sessionFactory.openSession();
        try{
            s.beginTransaction();
            Query query = s.createQuery(" from Match ");
        allQuery=   query.getResultList();
        s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
        }
return allQuery;
        //todo zamknac sesje
     //   return allQuery;
    }



    @Transactional
    public Match save(Match match) {





/*

terminarz.getRundy().forEach(a->
{
    a.setTerminarz(terminarz);
    a.getMecze().forEach(b->
    {
        b.setRunda(a);

    });
});*/


//save? sprobowac todo
       // entityManager.merge(terminarz);
      //  entityManager.persist(terminarz);



 //     Transaction tx = null;


        Session s =sessionFactory.openSession();
     try {

s.beginTransaction();

     //      tx = s.beginTransaction();
            s.save(match);
    //        s.persist(terminarz);
s.getTransaction().commit();
       //   tx.commit();


        }

        catch (Exception e) {
        if (s.getTransaction()!=null)
          {s.getTransaction().rollback();
          e.printStackTrace();}
            int y=0;
        } finally {
        s.close();
        }
  /*
try {  s = sessionFactory.getCurrentSession();
}
  catch (Exception e)
  {
       s   = sessionFactory.openSession();

  }
finally {

    s.saveOrUpdate(terminarz);
    s.close();
}
*/
        return match;
    }


    public void delete(Schedule object) throws JAXBException, IOException, ParserConfigurationException, SAXException {
//todo ?
    }
   /* public void deleteByName(String objectName) throws JAXBException, IOException, ParserConfigurationException, SAXException {
       var terminarzDoUsunieciaObj= findByTerminarzName(objectName);

// todo jesli nie znajduje po nazwie albo >1
        Session s = sessionFactory.openSession();
        try {
            var terminarz= s.get(Terminarz.class, terminarzDoUsunieciaObj.getId());
            s.beginTransaction();
            s.delete(terminarz);
            s.getTransaction().commit();




        }
        catch (Exception e){

        }
        finally {
            s.close();
        }

    }
  */
   public void deleteById(Long id) {
//todo ?
    }
@Transactional
@Override
public void updateMatchesResults(Schedule schedule, Integer roundNumber, List<Long> matchIds, List<String> userMatchResults1, List<String> userMatchResults2, List<String> opponentMatchResults1, List<String> opponentMatchResults2) {

/*
Round round=roundDAO.findRoundWitchMatches(schedule.getName(), roundNumber);
round.getMatches(). forEach(a->{
    if(a.getUserMatchResult1()!=)

    });
*/
    for (int i = 0; i < matchIds.size(); i++) {

        //   var b = session.load(Match.class, matchIds.get(i));
        Session session = sessionFactory.openSession();
        try {


            session.beginTransaction();
            //      var a=     s.load(Terminarz.class, 104L);

            Query query = session.createQuery(" update Match set " +
                    "userMatchResult1=:userMatchResult1, " +
                    "userMatchResult2=:userMatchResult2," +
                    "opponentMatchResult1=:opponentMatchResult1," +
                    "opponentMatchResult2=:opponentMatchResult2" +
                    " where id=:matchId");

            String tempUserResult1="";
            String tempUserResult2="";
            String tempOpponenResult1="";
            String tempOpponentResult2="";
           if(userMatchResults1.size()!=0 && opponentMatchResults1.size()!=0)
           {
               tempUserResult1=userMatchResults1.get(i);
               tempOpponenResult1=opponentMatchResults1.get(i);
           }
               if(userMatchResults2.size()!=0 && opponentMatchResults2.size()!=0)
               {
                   tempUserResult2=userMatchResults2.get(i);
                   tempOpponentResult2=opponentMatchResults2.get(i);
               }
            query.setParameter("userMatchResult1", tempUserResult1);
            query.setParameter("userMatchResult2", tempUserResult2);
            query.setParameter("opponentMatchResult1", tempOpponenResult1);
            query.setParameter("opponentMatchResult2", tempOpponentResult2);
            query.setParameter("matchId", matchIds.get(i));




query.executeUpdate();
            session.getTransaction().commit();
            //  s.update( terminarz.getRundy().get(runda-1));
         //   System.out.println("po ppppppppp");

//todo dodać info, że nie zapisano wyniku bo w wyniku podano tylko bramki 1 albo 2 gracza
        } catch (Exception e) {

        } finally {
            session.close();
        }

    }


}
@Transactional
public void updateMatchResult(Long matchId,String userMatchResult1,String userMatchResult2,String opponentMatchResult1,String opponentMatchResult2  )
{
/*    Session session =sessionFactory.openSession();

    Transaction tx = null;

    var b=      session.load(Match.class,matchId)   ;

    try {



        tx = session.beginTransaction();
        //      var a=     s.load(Terminarz.class, 104L);

        Query query= session.createQuery(" update Match m set " +
                "e=m.userMatchResult1=:userMatchResult1," +
                "e=m.userMatchResult2=:userMatchResult2," +
                "e=m.opponentMatchResult1=:opponentMatchResult1," +
                "e=m.opponentMatchResult2=:opponentMatchResult2," +
                " where m.id=:matchId");

query.setParameter("userMatchResult1",userMatchResult1);
        query.setParameter("userMatchResult2",userMatchResult2);
        query.setParameter("opponentMatchResult1",opponentMatchResult1);
        query.setParameter("opponentMatchResult2",opponentMatchResult2);
        query.setParameter("matchId",matchId);





        session.getTransaction().commit();
        //  s.update( terminarz.getRundy().get(runda-1));





    }

    catch (Exception e) {
        if (tx!=null) tx.rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }*/
}

    public void saveRound(Schedule schedule, int round) {

/*
       terminarz.getRundy().forEach(a->
        {
            a.setTerminarz(terminarz);
            a.getMecze().forEach(b->
            {
                b.setRunda(a);

            });
        });
*/

 /*       terminarz.getRundy().get(runda-1).getMecze().
                forEach(a->a.setRunda(
                        terminarz.getRundy().get(runda-1)
                ));
*/
        Session s =sessionFactory.openSession();

        Transaction tx = null;

       var b=      s.load(Schedule.class,106L)   ;

        try {



            tx = s.beginTransaction();
 //      var a=     s.load(Terminarz.class, 104L);

       s.update(schedule.getRounds().get(round-1));
          //  s.update( terminarz.getRundy().get(runda-1));
            tx.commit();




        }

        catch (Exception e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }

    }

    public void saveResults(Integer round, Schedule schedule1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException {



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        Duration d = DatatypeFactory.newInstance().newDuration(false, 0, 0, 7, 0, 0, 0);
        now.add(d);





        //  terminarzDAOimpl.save(terminarz);
        //   terminarz= entityManager.find(Terminarz.class,104L);

//var runda1=entityManager.find(Runda.class, 176L);

        //    var dd = entityManager.createQuery("   SELECT * FROM Match \n" );
        /*+
                        "        inner join Runda r on r.runda_id=m.runda\n" +
                        "        inner join Terminarz t on t.terminarz_id=r.terminarz_id\n" +
                        "        where t.nazwa=\"Liga UPSG s3\" and r.nr_rundy=1", Runda.class);

         */
        //.setParameter("id", primaryDoctorId)
        //     .getSingleResult();

      /*  SELECT * FROM Match m
        inner join Runda r on r.runda_id=m.runda
        inner join Terminarz t on t.terminarz_id=r.terminarz_id
        where t.nazwa="Liga UPSG s3" and r.nr_rundy=1*/
/*
        SELECT * FROM Runda r
        inner join Terminarz t on t.terminarz_id=r.terminarz_id
        where a.nazwa="Liga UPSG s3" and r.nr_rundy=1
        inner join lkm_dev.mecze m on r.runda_id=m.runda
        inner join lkm_dev.users u on u.user_id= m.przeciwnik
        inner join lkm_dev.users z on z.user_id= m.user
*/

        Session s =sessionFactory.openSession();
        var schedule=      s.load(Schedule.class,106L)   ;


        for (var roundOfSchedule: schedule.getRounds()
        ) {


            if(  roundOfSchedule.getNr()== round) {



                for (var match : roundOfSchedule.getMatches()
                ) {


                    //sprawdzanie czy  gospodarzem jest apuza
                    if (!(match.getUserData().getUsername().equals("pauza") || match.getUserData().getUsername().equals("pauza"))) {





                        var user = match.getUserData();
                        var userTeamId = user.getTeamlist().get(0).getTeamId();
                        var oponent = match.getOpponentUserData();
                        var oponentTeamId = oponent.getTeamlist().get(0).getTeamId();

                        var played = matchService.findPlayedByUser(user);

                        var playedMZMatches = played.getMatches().stream().
                                filter(a -> a.getDate()!=null).//.contains(item.getData().toString())).
                                        filter(a ->

                                            a.getType().equals("friendly")



                                        ).collect(Collectors.toList());

       /*           &&
                        ( a.getTeam().getTeamId()== mecz.getopponentUser().getTeamlist().get(0).getTeamId()
                                || a.getTeam().getTeamId()==  mecz.getopponentUser().getTeamlist().get(1).getTeamId()
                        )
*/
                        //todo sprawzic czy id oponenta to id z terminarza
                        for (var playedMatch : playedMZMatches
                        ) {


                          var user1=  mzUserService.findByTeamId(playedMatch.getTeamlist().get(0).getTeamId());
                            var user2=   mzUserService.findByTeamId(playedMatch.getTeamlist().get(1).getTeamId());
 /*if(user1.getUsername()==user.getUsername() &&user2.getUsername()==oponent.getUsername()
 ||
 )*/




                                                     //tutaj user ma id =0 oponent 1
                            if (user1.getUserId().equals(user.getUserId()))
                            {
                                //sprawdzanie czy prawidlowy przeciwnik
                                if (playedMatch.getTeamlist().get(1).getTeamId() == match.getOpponentUserData().getTeamlist().get(0).getTeamId()) {
                                    //aktualizacja
                                    //  mecz.setMatchResult1("1");
                                    match.setUserMatchResult1(playedMatch.getTeamlist().get(0).getGoals());
                                    match.setOpponentMatchResult1(playedMatch.getTeamlist().get(1).getGoals());
                                }


                            }
                            //tutaj user ma id 1  oponent =0
                            else if (user2.getUserId().equals(user.getUserId())) {
                                //sprawdzanie czy prawidlowy przeciwnik
                                if (playedMatch.getTeamlist().get(0).getTeamId() == match.getOpponentUserData().getTeamlist().get(0).getTeamId()) {
                                    //aktualizacja
                                    //  mecz.setMatchResult1("1");
                                    match.setUserMatchResult2(playedMatch.getTeamlist().get(1).getGoals());
                                    match.setOpponentMatchResult2(playedMatch.getTeamlist().get(0).getGoals());
                                }


                            }

                        }

                    }



                }

            }
        }


        s.beginTransaction();
        s.update( schedule.getRounds().get(round-1));
        s.getTransaction().commit();
        s.close();
int o=0;



    }


    public Match findByMatchId(long id) {
        Session s = sessionFactory.openSession();
        Match match = new Match();
        try {
            s.beginTransaction();
            match=s.get(Match.class,id);
            s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
            return match;
        }
    }

 /*   @Transactional
    @Override
    public Match findByTerminarzName(String name) {
       Session s = sessionFactory.openSession();
        Match match = new Match();
       try {
           s.beginTransaction();

           String hql = "  FROM Match m where m.Name=:name";

           Query query = s.createQuery(hql);
           query.setParameter("name",name);
         terminarz  = (Terminarz)query.getSingleResult();
           s.getTransaction().commit();
       }
       catch (Exception e)
       {

       }
       finally {
           s.close();
           return terminarz;
       }


    }*/

/*    public Terminarz findLastById() {
        Session s = sessionFactory.openSession();
        Terminarz terminarz2=null;
        try {

            s.beginTransaction();


            String hql = " from Terminarz t where t.id=" +
                    "(select max(t.id) from Terminarz t)";

            Query query = s.createQuery(hql);
             terminarz2 = (Terminarz) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
e.printStackTrace();
        } finally {
            s.close();
            return terminarz2;
        }

    }*/


    @Transactional
    public List<Match> findAllByScheduleIdAndRoundId(long scheduleId, int roundId) {
        Session s = sessionFactory.openSession();
        List<Match> matches=new ArrayList<>();
        try {

            s.beginTransaction();

            System.out.println("|/ findAllByTerminarzIdAndRundaId");
            String hql = "Select  m from Match m  join  " +
                    // " UserData u on u.userId=m.user  join  "+
                    "Round r on r.id=m.round.id   join "+
                   "Schedule s on s.id=r.schedule "+
                    " join fetch UserData u on u.userId=m.userData "   +
                    " join fetch UserData p on p.userId=m.opponentUserData "   +
                    "where (s.id=:scheduleId and r.nr=:roundNumber)";



            Query query = s.createQuery(hql);

            query.setParameter("scheduleId",scheduleId);
            //numer rundy zaczyna sie od 1 id w bazie od 0
            query.setParameter("roundNumber",roundId+1)      ;
            matches = query.getResultList();
            for (var match:matches
                 ) {
           //     Hibernate.initialize(mecz.getUser());
             //   Hibernate.initialize(mecz.getopponentUser());
            //    Hibernate.initialize((mecz.getUser().getTeamlist()));
              //  Hibernate.initialize((mecz.getopponentUser().getTeamlist()));
            }

            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return matches;
        }
    }





   public List<Match> findAllbyScheduleId(long id)
    {
        Session s = sessionFactory.openSession();
        List<Match> matches=new ArrayList<>();
        try {

            s.beginTransaction();

            System.out.println("|/ findAllByTerminarzId");
            String hql = "Select  m from Match m  join  " +
                    // " UserData u on u.userId=m.user  join  "+
                    "Round r on r.id=m.round.id   join "+
                    "Schedule s on s.id=r.schedule "+
                    " join fetch UserData u on u.userId=m.userData "   +
                    " join fetch UserData p on p.userId=m.opponentUserData "   +
                    "where (s.id=:scheduleId )";



            Query query = s.createQuery(hql);

            query.setParameter("scheduleId",id);
            //numer rundy zaczyna sie od 1 id w bazie od 0
           // query.setParameter("numerRundy",rundaId+1)      ;
            matches = query.getResultList();
            for (var match:matches
            ) {
                //     Hibernate.initialize(mecz.getUser());
                //   Hibernate.initialize(mecz.getopponentUser());
                //    Hibernate.initialize((mecz.getUser().getTeamlist()));
                //  Hibernate.initialize((mecz.getopponentUser().getTeamlist()));
            }

            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return matches;
        }
    }








}
