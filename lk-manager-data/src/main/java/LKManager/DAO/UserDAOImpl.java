package LKManager.DAO;

import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import org.hibernate.Filter;
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
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository

public class UserDAOImpl implements UserDAO {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    MZCache mzCache;
    @Autowired
private EntityManager entityManager;
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

  //  @Override



    public List<UserData> findAllFromCache()
    {
        List<UserData> gracze=new ArrayList<>();
        if (mzCache.getUsers().size() != 0) {
            System.out.println("from user cache");
            gracze = mzCache.getUsers();

        }
        else gracze=null;
        return    gracze;
    }
@Transactional
    public List<UserData> findAll(boolean isDeleted) {


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

   // List<UserData> gracze= new ArrayList<>();

        System.out.println("from user db");

        List<UserData> allQuery =null;
        Session s = sessionFactory.openSession();
        try {

            Filter filter = s.enableFilter("deletedUserFilter");
            filter.setParameter("isDeleted", isDeleted);
            //     Filter filter2 = s.enableFilter("deletedTeamFilter");
            //    filter2.setParameter("isDeleted", isDeleted);

            s.beginTransaction();


            Query query = s.createQuery(" from UserData ");
            allQuery = query.getResultList();

            s.disableFilter("deletedUserFilter");
            //  s.disableFilter("deletedTeamFilter");

            s.getTransaction().commit();


            System.out.println("findall done");

        } catch (Exception e) {
            int y = 0;
        } finally {
            s.close();
            return allQuery;
        }


}

    @Override
    public List<UserData> findAll() {
       //todo cos zrobic z tym

        return null;
    }

    @Override
    @Transactional
    public UserData findByTeamId(int id) throws IOException, ParserConfigurationException, SAXException, JAXBException {
       Session s= sessionFactory.openSession();
        Team team = new Team();
        UserData user= new UserData();
       try{
     /*      s.beginTransaction();
            team= s.get(Team.class,id);

           s.getTransaction().commit();
        */
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
        //usuwanie hokeja
if(user.getTeamlist().size()>1)
    user.getTeamlist().remove(1);

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


//var t=findByTeamId(object.getTeamlist().get(0).getTeamId());



    deleteUser(object.getUserId().longValue());

    }

    @Override
    public void deleteById(Long id) {
  //      Integer id1=1;
        deleteUser(id);
    }

    private void deleteUser(Long id) {
        Session s = sessionFactory.openSession();
        try {

/********************************
 *    To jeśli by się usuwało encję.
 *    Trzeba by zanullować userów i opponentUserów w parentach (Match)
 *******************************/
            s.beginTransaction();
/*
             List<Match> matchesWithUserToDelete= null;

            System.out.println("here2");
            Query query = s.createQuery(" select m from Match m where (m.opponentUser.userId=:userId or m.user.userId=:userId) ");
query.setParameter("userId",id.intValue());
            System.out.println("here1");
            matchesWithUserToDelete=query.getResultList();


          for (Match match : matchesWithUserToDelete){
                if(match.getUser().getUserId().equals(id.intValue()))
                    match.setUser(null);
               else
                    match.setopponentUser(null);
                System.out.println("here2");
                s.update(match);
s.getTransaction().commit();
            }


            s.flush();

            matchesWithUserToDelete=   query.getResultList();

*/
 /**********************************/
            UserData userToDelete= s.get(UserData.class, id.intValue());
 //userToDelete.setDeleted();

/********************************
*       i potem usunąć Usera
 *******************************/
          s.delete(userToDelete);
/**********************************/

//s.update(userToDelete);

            s.getTransaction().commit();
        }
        catch (Exception e){

        }
        finally {
s.close();
        }
    }


}
