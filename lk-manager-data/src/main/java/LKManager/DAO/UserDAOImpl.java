package LKManager.DAO;

import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Repository

public class UserDAOImpl implements UserDAO {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
private EntityManager entityManager;
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Set<UserData> findAll() {

        Session s= sessionFactory.openSession();
     //   Team team = new Team();
   /*     UserData user= new UserData();
        try{
            s.beginTransaction();
        s.

            s.getTransaction().commit();
            user=   s.get(UserData.class, team.getUser().getUserId());
            s.getTransaction().commit();
        }
        catch (Exception e)
        {

        }
        finally {
            s.close();
            return user ;
        }
*/
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<UserData> cq = cb.createQuery(UserData.class);
        Root<UserData> rootEntry = cq.from(UserData.class);
        CriteriaQuery<UserData> all = cq.select(rootEntry);

        TypedQuery<UserData> allQuery = s.createQuery(all);
        return allQuery.getResultStream().collect(Collectors.toSet());

    }

    @Override
    @Transactional
    public UserData findByTeamId(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException {
       Session s= sessionFactory.openSession();
        Team team = new Team();
        UserData user= new UserData();
       try{
           s.beginTransaction();
            team= s.get(Team.class,id);

           s.getTransaction().commit();
        user=   s.get(UserData.class, team.getUser().getUserId());
           s.getTransaction().commit();
       }
       catch (Exception e)
       {

       }
       finally {
           s.close();
           return user ;
       }


    }

    @Override
    @Transactional
    public UserData save(UserData user) {
     /*   for (var i:user
             ) {
            entityManager.persist(i);
        }
*/

user.getTeamlist().get(0).setUser(user);
//user.getTeamlist().get(1).setUser(user);

        System.out.println("+++"+user);

        user.getTeamlist().get(0).setUser(user);

        System.out.println("+++"+user.getTeamlist().get(0).getUser());

        Session s = sessionFactory.openSession();
        Transaction tx = null;
        try{
         tx=   s.beginTransaction();
          s.saveOrUpdate(user);
           tx.commit();
        }
        catch (Exception e)
        {
            if (tx!=null) tx.rollback();
               e.printStackTrace();
            int i=0;
        }



        s.close();
      //entityManager.persist(user);

        return user;
    }

    @Override
    public void delete(UserData object) throws JAXBException, IOException, ParserConfigurationException, SAXException {


var t=findByTeamId(object.getTeamlist().get(0).getTeamId());


        Session s = sessionFactory.openSession();
        try {
            var gracz= s.get(UserData.class, object.getUserId());
            s.beginTransaction();
            s.delete(gracz);
            s.getTransaction().commit();
        }
        catch (Exception e){

        }
        finally {
            s.close();
        }

    }

    @Override
    public void deleteById(Long id) {
        Session s = sessionFactory.openSession();
        try {

            s.beginTransaction();
            s.delete(id);
            s.getTransaction().commit();
        }
        catch (Exception e){

        }
        finally {
s.close();
        }
    }


}
