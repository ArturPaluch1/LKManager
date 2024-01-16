package LKManager.DAO;

import LKManager.LK.Round;
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
public class RoundDAOImpl implements RoundDAO {

    private final EntityManager entityManager;
    @Autowired
    public RoundDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    private SessionFactory sessionFactory;



    @Override
    @Transactional
    public void saveRound(Round round) {




        Session s =sessionFactory.openSession();


        //    var b=      s.load(Runda.class,round.getId())   ;

        try {
            //todo to przeniesc do match dao
            s.beginTransaction();
            for (Match match:round.getMatches()
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
    public Round findByScheduleIdAndRoundId(long scheduleId, int roundId) {
        Session s = sessionFactory.openSession();
        Round round = new Round();
        try {

            s.beginTransaction();


            String hql = "Select r from Round r  inner join "+
                    "Schedule s on s.id=r.schedule "+
                    "where s.id=:scheduleId and r.nr=:roundNumber ";

            Query query = s.createQuery(hql);
            query.setParameter("roundNumber",roundId+1);
            query.setParameter("scheduleId",scheduleId);
            round = (Round) query.getSingleResult();
            s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
            return round;
        }
    }

    @Override
    public List<Round> findAllByScheduleId(long scheduleId) {

        Session s = sessionFactory.openSession();
        List<Round> rounds= new ArrayList<>();
        try {

            s.beginTransaction();


         //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
            String hql = "Select r from Round r  inner join "+
                    "Schedule s on s.id=r.schedule "+
                    "where s.id=:scheduleId  order by r.date";

               Query query = s.createQuery(hql);
            query.setParameter("scheduleId",scheduleId);
            rounds = (List<Round>) query.getResultList();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return rounds;
        }


    }

    @Override
    public List<Round> findAllByScheduleName(String chosenScheduleName) {
        Session s = sessionFactory.openSession();
        List<Round> rounds= new ArrayList<>();
        try {

            s.beginTransaction();


            //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
            String hql = "Select r from Round r  inner join "+
                    "Schedule s on s.id=r.schedule "+
                    "where s.name=:scheduleName  order by r.date";

            Query query = s.createQuery(hql);
            query.setParameter("scheduleName",chosenScheduleName);
            rounds = (List<Round>) query.getResultList();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return rounds;
        }
    }

    @Override
    public Round findRound(String chosenScheduleName, Integer roundNumber) {
        Session s = sessionFactory.openSession();
       Round round= null;
        try {

            s.beginTransaction();


            //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
            String hql = "Select r from Round r  inner join "+
                    "Schedule s on s.id=r.schedule "+
                    "where s.Name=:scheduleName and r.nr=:roundNumber order by r.date";

            Query query = s.createQuery(hql);
            query.setParameter("scheduleName",chosenScheduleName);
            query.setParameter("roundNumber",roundNumber);
            round = (Round) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return round;
        }
    }

    @Override
    public Round findRoundWitchMatches(String chosenScheduleName, Integer roundNumber) {
        Session s = sessionFactory.openSession();
     Round round= null;
        try {

            s.beginTransaction();


            //   String hql = " from Runda r  where r.id=(select max(r.id) from Runda r)";
           /* String hql = " Select r from Runda r " +
                    "left join fetch  r.mecze"+
                    " inner join "+
                    "Terminarz t on t.id=r.terminarz "+
                    "where t.Name=:terminarzName and r.nr=:numerRundy order by r.date";*/
            String hql = " Select r from Round r " +
                    " join fetch  r.matches"+
                    "  join fetch r.schedule s"+

                    " where s.name=:scheduleName and r.nr=:roundNumber order by r.date";
            Query query = s.createQuery(hql);
            query.setParameter("scheduleName",chosenScheduleName);
            query.setParameter("roundNumber",roundNumber);
            round = (Round) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            return round;
        }
    }
    public Round findLastById() {
        Session s = sessionFactory.openSession();
        Round round =null;
        try {

            s.beginTransaction();


            String hql = " from Round r where r.id=" +
                    "(select max(r.id) from Round r)";

            Query query = s.createQuery(hql);
             round = (Round) query.getSingleResult();
            s.getTransaction().commit();


        } catch (Exception e) {
e.printStackTrace();
        } finally {
            s.close();
            return round;
        }

    }

}
