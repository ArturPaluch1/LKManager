package LKManager.DAO_SQL;

import LKManager.model.MatchesMz.Match;
import LKManager.model.Schedule;
import LKManager.model.UserMZ.MZUserData;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public  class CustomScheduleDAOImpl implements CustomScheduleDAO{

    private final EntityManager entityManager;


    @Autowired
    public CustomScheduleDAOImpl(EntityManager entityManager,  SessionFactory sessionFactory) {
        this.entityManager = entityManager;

        this.sessionFactory = sessionFactory;
    }


    private final SessionFactory sessionFactory;


    public void refresh(Schedule schedule) {

        Session session = sessionFactory.getCurrentSession();
        session.refresh(schedule);
    }

@Transactional
    public List<Schedule> findAll1() {
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
        List<Schedule> allQuery= null;
        Session s= sessionFactory.openSession();

        try{
            s.beginTransaction();
            Query query = s.createQuery(" select  s from Schedule  s left JOIN FETCH s.rounds r" //+
               /*    "   join fetch Runda r on t.id=r.terminarz "+
                            "   left outer join Match m on r.id=m.runda "+
                            "  left outer join UserData u on m.user=u.userId "

*/

            );

        /*    Query query = s.createQuery(
                    "      select distinct t    "+
                    "      FROM Terminarz t "+
                    "        left JOIN FETCH t.rundy  r "//+
                //    "       JOIN FETCH r.mecze m "//+
                 //   "       JOIN FETCH m.user AS u "+
                 //   "       JOIN FETCH u.teamlist AS te "

            );

            Query query2 = s.createQuery(
                    "      select distinct m    "+
                            "      FROM Match m  where m.runda in:rundy"//+
                    //        "        left JOIN FETCH m.user u   "//+
                    //    "       JOIN FETCH r.mecze m "//+
                    //   "       JOIN FETCH m.user AS u "+
                    //   "       JOIN FETCH u.teamlist AS te "

            );*/

            System.out.println("----------------------");
        allQuery=  query.getResultList();

           // query2.setParameter("rundy",allQuery.get())


        s.getTransaction().commit();
        }
        catch (Exception e)
        {
            System.out.println("db terminarz error");
        }
        finally {
            System.out.println("close");
            s.close();
        }
return allQuery.stream().toList();
        //todo zamknac sesje
     //   return allQuery;
    }




  //  @Transactional
    @Override
    @Transactional
    public Schedule saveSchedule(Schedule schedule) {



     /*   try(Session s = sessionFactory.openSession()){

            s.beginTransaction();
*/


try{
          /*  schedule.getRounds().forEach(a->
            {
                a.setSchedule(schedule);
                a.getMatches().forEach(b->
                {
                    b.setRound(a);

                });
            });*/
    if(schedule.getRounds().size()!=0)
    {
        schedule.getRounds().forEach(round -> {
            round.setSchedule(schedule); // Ustawienie Schedule w Round
            if(round.getMatches().size()!=0)
            round.getMatches().forEach(match -> {

                match.setRound(round); // Ustawienie Round w Match
            });
        });
    }

    System.out.println("opening session");
    entityManager.persist(schedule);
    //     sessionFactory.openSession().saveOrUpdate(schedule);

    } catch (Exception e) {
        System.out.println("Błąd aktualizacji terminarza: " + e.getMessage());
        e.printStackTrace();
    }
finally {
    System.out.println("finally");
    //sessionFactory.getCurrentSession().close();
}



    /*        s.saveOrUpdate(schedule);

s.getTransaction().commit();*/



/*


        }

        catch (Exception e) {
            System.out.println("Błąd aktualizacji terminarza: " + e.getMessage());
            e.printStackTrace();
        }
*/



        return schedule;
    }



    public void delete(Schedule object)  {
//todo ?
    }

    public boolean deleteByName(String objectName) {
 objectName=  objectName.replace(" ","_");

       var scheduleToDelete= findByScheduleName(objectName);

// todo jesli nie znajduje po nazwie albo >1
        Session s = sessionFactory.openSession();
        try {
            var terminarz= s.get(Schedule.class, scheduleToDelete.getId());
            s.beginTransaction();
            s.delete(terminarz);
            s.getTransaction().commit();

return true;


        }
        catch (Exception e){
            System.out.println("db delete terminarz error");
            return false;
        }
        finally {
            s.close();
        }

    }
    public void deleteById(Long id) {
//todo ?
    }


   /* @Override
    public void saveRound(Terminarz terminarz, int runda) {

       terminarz.getRundy().forEach(a->
        {
            a.setTerminarz(terminarz);
            a.getMecze().forEach(b->
            {
                b.setRunda(a);

            });
        });

 *//*       terminarz.getRundy().get(runda-1).getMecze().
                forEach(a->a.setRunda(
                        terminarz.getRundy().get(runda-1)
                ));
*//*
        Session s =sessionFactory.openSession();

        Transaction tx = null;

       var b=      s.load(Terminarz.class,106L)   ;

        try {



            tx = s.beginTransaction();
 //      var a=     s.load(Terminarz.class, 104L);

       s.update(terminarz.getRundy().get(runda-1));
          //  s.update( terminarz.getRundy().get(runda-1));
            tx.commit();




        }

        catch (Exception e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }

    }*/

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
        Schedule schedule = null;
      try{
           schedule =      s.load(Schedule.class,106L)   ;
          for (var item: schedule.getRounds()
          ) {


              if(  item.getNr()== round) {



                  for (var match : item.getMatches()
                  ) {


                      //sprawdzanie czy  gospodarzem jest apuza
                      if (!(match.getMZUserData().getUsername().equals("pauza") || match.getMZUserData().getUsername().equals("pauza"))) {





                          var user = match.getMZUserData();
                          var userTeamId = user.getTeamlist().get(0).getTeamId();
                          var oponent = match.getOpponentMZUserData();
                          var oponentTeamId = oponent.getTeamlist().get(0).getTeamId();

                          var played = matchService.findPlayedByUser(user);

                          var playedMzMatches = played.getMatches().stream().
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
                          for (var playedMzMatch : playedMzMatches
                          ) {


                              var user1=  mzUserService.findByTeamId(playedMzMatch.getTeamlist().get(0).getTeamId());
                              var user2=   mzUserService.findByTeamId(playedMzMatch.getTeamlist().get(1).getTeamId());
 /*if(user1.getUsername()==user.getUsername() &&user2.getUsername()==oponent.getUsername()
 ||
 )*/




                              //tutaj user ma id =0 oponent 1
                              if (user1.getMZuser_id().equals(user.getMZuser_id()))
                              {
                                  //sprawdzanie czy prawidlowy przeciwnik
                                  if (playedMzMatch.getTeamlist().get(1).getTeamId() == match.getOpponentMZUserData().getTeamlist().get(0).getTeamId()) {
                                      //aktualizacja
                                      //  mecz.setMatchResult1("1");
                                      match.setUserMatchResult1(playedMzMatch.getTeamlist().get(0).getGoals());
                                      match.setOpponentMatchResult1(playedMzMatch.getTeamlist().get(1).getGoals());
                                  }


                              }
                              //tutaj user ma id 1  oponent =0
                              else if (user2.getMZuser_id().equals(user.getMZuser_id())) {
                                  //sprawdzanie czy prawidlowy przeciwnik
                                  if (playedMzMatch.getTeamlist().get(0).getTeamId() == match.getOpponentMZUserData().getTeamlist().get(0).getTeamId()) {
                                      //aktualizacja
                                      //  mecz.setMatchResult1("1");
                                      match.setUserMatchResult2(playedMzMatch.getTeamlist().get(1).getGoals());
                                      match.setOpponentMatchResult2(playedMzMatch.getTeamlist().get(0).getGoals());
                                  }


                              }

                          }

                      }



                  }

              }
          }

      }
      catch (Exception e)
      {

      }
      finally {

          try{
              s.beginTransaction();
              s.update( schedule.getRounds().get(round-1));
              s.getTransaction().commit();

          }
          catch (Exception e)
          {

          }
          finally {
              s.close();
          }
      }










    }


    public Schedule findByScheduleId(long id) {
        Session s = sessionFactory.openSession();
        Schedule schedule = new Schedule();
        try {
            s.beginTransaction();
            schedule =s.get(Schedule.class,id);
            s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
            return schedule;
        }
    }


  public List<MZUserData>  findAllParticipantsOfSchedule(String scheduleName)
    {
        scheduleName=scheduleName.replace(" ","_");
        Session session = sessionFactory.openSession();
        List<MZUserData>users= null;
        try
        {
            System.out.println("all participants:"+scheduleName);
            session.beginTransaction();

            Query querryUser= session.createQuery(
                    "select u from  MZUserData u " +

                            "  inner join Match m on u.MZuser_id=m.MZUserData"+
                            " inner join Round r on m.round=r.id "+
                            " inner join Schedule s on r.schedule=s.id " +
                            "   where (s.name=:ScheduleName and r.nr=1)");
            querryUser.setParameter("ScheduleName",scheduleName );
             users= (ArrayList<MZUserData>) querryUser.getResultList();

           Query querryOpponent= session.createQuery(
                    "select u from  MZUserData u " +

                            "  inner join Match m on u.MZuser_id=m.opponentMZUserData"+
                            " inner join Round r on m.round=r.id "+
                            " inner join Schedule s on r.schedule=s.id " +
                            "   where (s.name=:ScheduleName and r.nr=1)");
            querryOpponent.setParameter("ScheduleName",scheduleName );
            ArrayList<MZUserData> resultOpponent= (ArrayList<MZUserData>) querryOpponent.getResultList();
          //  users.add(result.getUser());
           users.addAll(resultOpponent);
         //  users.addAll(resultUser);
        //   users.stream().unordered().toList();
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            System.out.println("błąd połączenia db  w users");
        }
        finally {
            session.close();

            return users;
        }
    }

    public List<Match> findAllMatchesByScheduleName(String scheduleName)
    {
        Session session= null;
        List<Match>mecze= new ArrayList<>();
        try{
             session = sessionFactory.openSession();



            session.beginTransaction();

            Query querry= session.createQuery(
                    "select m from Match m "+
                            " inner join Round r on m.round=r.id "+
                            " inner join Schedule s on r.schedule=s.id " +
                            "   where s.name=:scheduleName");
            querry.setParameter("scheduleName",scheduleName );
            mecze= querry.getResultList();
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            //todo redirect za duzo polaczen
            System.out.println("błąd połączenia db  w tabela");
        }


        finally {
            if(session.isOpen())
            session.close();

            return mecze;
        }


    }
    @Transactional

    public Schedule findByScheduleName(String name) {
       Session s = sessionFactory.openSession();
       Schedule schedule = new Schedule();
       try {
           s.beginTransaction();
           System.out.println("|/ findByScheduleName");
           String hql = "  FROM Schedule s where s.name=:name"/*+
                   "  join fetch Runda r on t.id=r.terminarz "+
           "   join fetch Match m on r.id=m.runda "+
                   " join fetch UserData u on u.userId=m.user"*/


                   ;

           Query query = s.createQuery(hql);
           query.setParameter("name",name);
         schedule = (Schedule)query.getSingleResult();
           s.getTransaction().commit();
       }
       catch (Exception e)
       {

       }
       finally {
           s.close();
           return schedule;
       }


    }

    public Schedule findLastOngoingOrFinishedById() {


        Session s = sessionFactory.openSession();
        Schedule schedule2 =null;
        try {

            s.beginTransaction();


            String hql = " from Schedule s where  s.startDate=" +
                    "(select max(s2.startDate) from Schedule s2 where  s2.scheduleStatus!=0)";

            Query query = s.createQuery(hql);
            List<Schedule> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
             schedule2 = results.get(0);
             if(schedule2==null)
                 return null;

            s.getTransaction().commit();


        } catch (Exception e) {
e.printStackTrace();
        } finally {
            s.close();
            return schedule2;
        }

    }

}
