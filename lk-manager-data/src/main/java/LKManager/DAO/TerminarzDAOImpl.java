package LKManager.DAO;

import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
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
public class TerminarzDAOImpl implements TerminarzDAO {

    private final EntityManager entityManager;

    @Autowired
    public TerminarzDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    private SessionFactory sessionFactory;



    @Transactional
    public List<Terminarz> findAll() {
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
        List<Terminarz> allQuery= null;
        Session s= sessionFactory.openSession();
        try{
            s.beginTransaction();
            Query query = s.createQuery(" from Terminarz ");
        allQuery=   query.getResultList();
        s.getTransaction().commit();
        }
        catch (Exception e)
        {
            System.out.println("db error");
        }
        finally {
            s.close();
        }
return allQuery;
        //todo zamknac sesje
     //   return allQuery;
    }



    @Override
    @Transactional
    public Terminarz save(Terminarz terminarz) {







terminarz.getRundy().forEach(a->
{
    a.setTerminarz(terminarz);
    a.getMecze().forEach(b->
    {
        b.setRunda(a);

    });
});




//save? sprobowac todo
       // entityManager.merge(terminarz);
      //  entityManager.persist(terminarz);



 //     Transaction tx = null;


        Session s =sessionFactory.openSession();
     try {

s.beginTransaction();

     //      tx = s.beginTransaction();
            s.saveOrUpdate(terminarz);
    //        s.persist(terminarz);
s.getTransaction().commit();
       //   tx.commit();


        }

        catch (Exception e) {
        if (s.getTransaction()!=null)
          {s.getTransaction().rollback();
          e.printStackTrace();}
            int y=0;
            System.out.println("db error");
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
        return terminarz;
    }


    public void delete(Terminarz object) throws JAXBException, IOException, ParserConfigurationException, SAXException {
//todo ?
    }
    public void deleteByName(String objectName) throws JAXBException, IOException, ParserConfigurationException, SAXException {
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
            System.out.println("db error");
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

    public void saveResults(Integer runda, Terminarz terminarz1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException {



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
        Terminarz terminarz= null;
      try{
           terminarz=      s.load(Terminarz.class,106L)   ;
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

                          var rozegrane = matchService.findPlayedByUser(user);

                          var meczeTurniejowe = rozegrane.getMatches().stream().
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
                          for (var rozegranyMecz : meczeTurniejowe
                          ) {


                              var user1=  mzUserService.findByTeamId(rozegranyMecz.getTeamlist().get(0).getTeamId());
                              var user2=   mzUserService.findByTeamId(rozegranyMecz.getTeamlist().get(1).getTeamId());
 /*if(user1.getUsername()==user.getUsername() &&user2.getUsername()==oponent.getUsername()
 ||
 )*/




                              //tutaj user ma id =0 oponent 1
                              if (user1.getUserId().equals(user.getUserId()))
                              {
                                  //sprawdzanie czy prawidlowy przeciwnik
                                  if (rozegranyMecz.getTeamlist().get(1).getTeamId() == mecz.getopponentUser().getTeamlist().get(0).getTeamId()) {
                                      //aktualizacja
                                      //  mecz.setMatchResult1("1");
                                      mecz.setUserMatchResult1(String.valueOf(rozegranyMecz.getTeamlist().get(0).getGoals()));
                                      mecz.setOpponentMatchResult1(String.valueOf(rozegranyMecz.getTeamlist().get(1).getGoals()));
                                  }


                              }
                              //tutaj user ma id 1  oponent =0
                              else if (user2.getUserId().equals(user.getUserId())) {
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
      catch (Exception e)
      {

      }
      finally {

          try{
              s.beginTransaction();
              s.update( terminarz.getRundy().get(runda-1));
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

    @Override
    public Terminarz findByTerminarzId(long id) {
        Session s = sessionFactory.openSession();
        Terminarz terminarz = new Terminarz();
        try {
            s.beginTransaction();
            terminarz=s.get(Terminarz.class,id);
            s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
            return terminarz;
        }
    }


  public List<UserData>  findAllParticipantsOfSchedule(String ScheduleName)
    {
        Session session = sessionFactory.openSession();
        List<UserData>users= null;
        try
        {

            session.beginTransaction();

            Query querryUser= session.createQuery(
                    "select u from  UserData u " +

                            "  inner join Match m on u.userId=m.user"+
                            " inner join Runda r on m.runda=r.id "+
                            " inner join Terminarz t on r.terminarz=t.id " +
                            "   where (t.Name=:ScheduleName and r.nr=1)");
            querryUser.setParameter("ScheduleName",ScheduleName );
             users= (ArrayList<UserData>) querryUser.getResultList();

           Query querryOpponent= session.createQuery(
                    "select u from  UserData u " +

                            "  inner join Match m on u.userId=m.opponentUser"+
                            " inner join Runda r on m.runda=r.id "+
                            " inner join Terminarz t on r.terminarz=t.id " +
                            "   where (t.Name=:ScheduleName and r.nr=1)");
            querryOpponent.setParameter("ScheduleName",ScheduleName );
            ArrayList<UserData> resultOpponent= (ArrayList<UserData>) querryOpponent.getResultList();
          //  users.add(result.getUser());
           users.addAll(resultOpponent);
         //  users.addAll(resultUser);
        //   users.stream().unordered().toList();
            session.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            session.close();

            return users;
        }
    }

    public List<Match> findAllMatchesByTerminarzName(String terminarzName)
    {
        Session session = sessionFactory.openSession();
        List<Match>mecze= new ArrayList<>();
        try
        {

            session.beginTransaction();

            Query querry= session.createQuery(
                    "select m from Match m "+
                            " inner join Runda r on m.runda=r.id "+
                            " inner join Terminarz t on r.terminarz=t.id " +
                            "   where t.Name=:terminarzName");
            querry.setParameter("terminarzName",terminarzName );
           mecze= querry.getResultList();
            session.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            session.close();

            return mecze;
        }


    }
    @Transactional
    @Override
    public Terminarz findByTerminarzName(String name) {
       Session s = sessionFactory.openSession();
       Terminarz terminarz = new Terminarz();
       try {
           s.beginTransaction();

           String hql = "  FROM Terminarz t where t.Name=:name";

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


    }

    public Terminarz findLastById() {


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

    }

}
