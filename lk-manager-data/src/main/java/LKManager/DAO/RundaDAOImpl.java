package LKManager.DAO;

import LKManager.LK.Runda;
import LKManager.model.MatchesMz.Match;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RundaDAOImpl implements RundaDAO {

    private final EntityManager entityManager;
    @Autowired
    public RundaDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    private SessionFactory sessionFactory;



    @Override
    @Transactional
    public void saveRound(Runda round) {




        Session s =sessionFactory.openSession();


        //    var b=      s.load(Runda.class,round.getId())   ;

        try {
            //todo to przeniesc do match dao
            s.beginTransaction();
            for (Match match:round.getMecze()
                 ) {
                s.update(match);
            }
            System.out.println("transaction");


            //      var a=     s.load(Terminarz.class, 104L);
         //   Runda round1= s.load(Runda.class, 387L);
           // round1.setPlayed(true);

//round.setPlayed(true);




            //  s.update( terminarz.getRundy().get(runda-1));
            s.getTransaction().commit();


            System.out.println("po commicie");

        }

        catch (Exception e) {
            if (s.getTransaction()!=null) s.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("error po commicie");
        } finally {
            s.close();
        }

    }
   @Override
    public Runda findByTerminarzIdAndRundaId(long terminarzId, int rundaId) {
        Session s = sessionFactory.openSession();
        Runda runda = new Runda();
        try {

            s.beginTransaction();


            String hql = "Select r from Runda r  inner join "+
                    "Terminarz t on t.id=r.terminarz "+
                    "where t.id=:terminarzId and r.nr=:numerRundy ";

            Query query = s.createQuery(hql);
            query.setParameter("numerRundy",rundaId+1);
            query.setParameter("terminarzId",terminarzId);
            runda = (Runda) query.getSingleResult();
            s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
            return runda;
        }
    }

    @Override
    public List<Runda> findAllByTerminarzId(long terminarzId) {

        Session s = sessionFactory.openSession();
        List<Runda> rundy= new ArrayList<>();
        try {

            s.beginTransaction();


         //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
            String hql = "Select r from Runda r  inner join "+
                    "Terminarz t on t.id=r.terminarz "+
                    "where t.id=:terminarzId  order by r.data";

               Query query = s.createQuery(hql);
            query.setParameter("terminarzId",terminarzId);
            rundy = (List<Runda>) query.getResultList();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return rundy;
        }


    }

    @Override
    public List<Runda> findAllByTerminarzName(String wybranyTerminarz) {
        Session s = sessionFactory.openSession();
        List<Runda> rundy= new ArrayList<>();
        try {

            s.beginTransaction();


            //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
            String hql = "Select r from Runda r  inner join "+
                    "Terminarz t on t.id=r.terminarz "+
                    "where t.Name=:terminarzName  order by r.data";

            Query query = s.createQuery(hql);
            query.setParameter("terminarzName",wybranyTerminarz);
            rundy = (List<Runda>) query.getResultList();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return rundy;
        }
    }

    @Override
    public Runda findRound(String wybranyTerminarz, Integer numerRundy) {
        Session s = sessionFactory.openSession();
       Runda round= null;
        try {

            s.beginTransaction();


            //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
            String hql = "Select r from Runda r  inner join "+
                    "Terminarz t on t.id=r.terminarz "+
                    "where t.Name=:terminarzName and r.nr=:numerRundy order by r.data";

            Query query = s.createQuery(hql);
            query.setParameter("terminarzName",wybranyTerminarz);
            query.setParameter("numerRundy",numerRundy);
            round = (Runda) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return round;
        }
    }

    @Override
    public Runda findRoundWitchMatches(String wybranyTerminarz, Integer numerRundy) {
        Session s = sessionFactory.openSession();
     Runda round= null;
        try {

            s.beginTransaction();


            //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
           /* String hql = " Select r from Runda r " +
                    "left join fetch  r.mecze"+
                    " inner join "+
                    "Terminarz t on t.id=r.terminarz "+
                    "where t.Name=:terminarzName and r.nr=:numerRundy order by r.data";*/
            String hql = " Select r from Runda r " +
                    " join fetch  r.mecze"+
                    "  join fetch r.terminarz t"+

                    " where t.Name=:terminarzName and r.nr=:numerRundy order by r.data";
            Query query = s.createQuery(hql);
            query.setParameter("terminarzName",wybranyTerminarz);
            query.setParameter("numerRundy",numerRundy);
            round = (Runda) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return round;
        }
    }
    public Runda findLastById() {
        Session s = sessionFactory.openSession();
        Runda runda=null;
        try {

            s.beginTransaction();


            String hql = " from Runda r where r.id=" +
                    "(select max(r.id) from Runda r)";

            Query query = s.createQuery(hql);
             runda = (Runda) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
e.printStackTrace();
        } finally {
            s.close();
            return runda;
        }

    }

}
