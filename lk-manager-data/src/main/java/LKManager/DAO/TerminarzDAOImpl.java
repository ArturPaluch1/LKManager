package LKManager.DAO;

import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import LKManager.services.MZUserService;
import LKManager.services.MatchService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
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

    @Override
    @Transactional
    public void save(Terminarz terminarz) {







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

        Session s =sessionFactory.openSession();

 //     Transaction tx = null;

        try {
s.beginTransaction();




     //      tx = s.beginTransaction();
            s.saveOrUpdate(terminarz);
s.getTransaction().commit();
       //   tx.commit();




        }

        catch (Exception e) {
          if (s.getTransaction()!=null)
          {s.getTransaction().rollback();
          e.printStackTrace();}
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
    }


    @Override
    public void saveRound(Terminarz terminarz, int runda) {

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

    }

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
        var terminarz=      s.load(Terminarz.class,106L)   ;


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


        s.beginTransaction();
        s.update( terminarz.getRundy().get(runda-1));
        s.getTransaction().commit();
        s.close();
int o=0;



    }
}
