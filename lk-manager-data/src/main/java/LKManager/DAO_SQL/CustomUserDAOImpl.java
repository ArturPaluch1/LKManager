package LKManager.DAO_SQL;

import LKManager.model.UserMZ.MZUserData;
import LKManager.model.UserMZ.Team;
import LKManager.model.account.Role;
import LKManager.model.account.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomUserDAOImpl implements CustomUserDAO {


    @Autowired
    SessionFactory sessionFactory;

    @PersistenceContext
    private EntityManager entityManager;
   /* @Autowired
    MZCache mzCache;*/

/*    @Autowired
private final EntityManager entityManager;*/

    //  @Override



/*    public List<UserData> findUsersFromCache_All()
    {
        System.out.println("all users - cache");
       return mzCache.getUsers();




    }*/

   /* @Override
    public List<UserData> findNotDeletedUsers() {
        return this.findAll(false);
    }*/
 /*   @Override
    public List<UserData> findUsersFromCache_NotDeletedWithoutPause() {
        System.out.println(" users !deleted !pauza - cache");
        return mzCache.getUsers().stream().filter(u->u.getDeleted()==false&& !u.getUsername().equals("pauza")).collect(Collectors.toList());
    }
    @Override
    public List<UserData> findUsersFromCache_NotDeletedWithPause() {
        System.out.println(" users !deleted !pauza - cache");
        return mzCache.getUsers().stream().filter(u->u.getDeleted()==false).collect(Collectors.toList());
    }*/



  /*  @Override
    public List<UserData> findUsers_NotDeletedWithoutPause() {


        return null;
    }

    @Override
    public List<UserData> findUsers_NotDeletedWithPause() {
        return null;
    }*/

    @Transactional
    private List<MZUserData> findAll(boolean isDeleted) {


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

        System.out.println("all user from  db");
//todo to chyba nieużywane, isDeleted jest przestarzałe, najpierw było activated a teraz zamienione na role
        List<MZUserData> allQuery =null;
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
            System.out.println("db user error");
        //   return  "redirect:/dodajTerminarz";
        } finally {
            s.close();
            return allQuery;
        }


}
/*
    @Override
    public List<UserData> findAll() {
       //todo cos zrobic z tym

        return null;
    }*/

  //  @Override
    @Transactional
    public MZUserData findMZUserByTeamId(int id)// throws IOException, ParserConfigurationException, SAXException, JAXBException
    {
       Session s= sessionFactory.openSession();
        Team team = new Team();
        MZUserData user= null;
       try{
     /*      s.beginTransaction();
            team= s.get(Team.class,id);

           s.getTransaction().commit();
        */
        user=   s.get(MZUserData.class, team.getUser().getMZuser_id());
           s.getTransaction().commit();
       }
       catch (Exception e)
       {
           System.out.println("db user error");
       }
       finally {
           s.close();
           return user ;
       }


    }




  @Override
    @Transactional
    public User saveUser(User user) {



/*user.getTeamlist().get(0).setUser(user);

        //usuwanie hokeja
if(user.getTeamlist().size()>1)
    user.getTeamlist().remove(1);

        System.out.println("+++"+user);

        user.getTeamlist().get(0).setUser(user);

        System.out.println("+++"+user.getTeamlist().get(0).getUser());


        try{

        entityManager.merge(user);


        }
        catch (Exception e)
        {
               e.printStackTrace();
            int i=0;
            System.out.println("db user error");
        }
finally {
       //     s.close();
            //entityManager.persist(user);

            return user;
        }

*/
try{
    entityManager.persist(user);
}
         catch (Exception e)
         {
         System.out.println(e);
         }





          return user;


    }

    @Override
    public MZUserData saveMZUser(MZUserData mzUser) {
       MZUserData result= entityManager.merge(mzUser);
       return result;
      //  return new UserMzDTO(result.getMZuser_id(),result.getUsername(),result.getTeamlist().get(0).getTeamId(),result.getTeamlist().get(0).getTeamName());
    }

    @Override
    public void deleteUser(User object) {


//var t=findByTeamId(object.getTeamlist().get(0).getTeamId());



    deleteUser(object.getId().longValue());

    }




    @Override
    public void deleteUserById(Long id) {
  //      Integer id1=1;
        deleteUser(id);
    }

    private void deleteUser(Long id) {
        Session s = sessionFactory.openSession();
        try {
            s.beginTransaction();
/*******************************
 *    To jeśli by się usuwało encję.
 *    Trzeba by zanullować userów i opponentUserów w parentach (Match)
 /*******************************/
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

            User userToDelete= s.get(User.class, id.intValue());
 userToDelete.setRole(Role.DEACTIVATED_USER);

/*******************************
*       i potem usunąć Usera
 ********************************/

      //    s.delete(userToDelete);
/*********************************/


//s.update(userToDelete);

            s.getTransaction().commit();
        }
        catch (Exception e){
            System.out.println("db user error");
        }
        finally {
s.close();
        }
    }


}
