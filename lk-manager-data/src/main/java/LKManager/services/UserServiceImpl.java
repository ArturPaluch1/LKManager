package LKManager.services;

import LKManager.DAO_SQL.UserDAO;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.RedisService.RedisUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final   UserDAO userDAO;
   //private final UserService userService;
   private final MZUserService mzUserService;
   private final RedisUserService redisUserService;
 /*   @Autowired
    MZCache mzCache;*/

private final GsonService gsonService;




@Transactional
    private UserDataDTO saveUser(UserData playerMZ) {
        //dwustronny dostęp: user->team, team->user
        try{
            playerMZ.getTeamlist().get(0).setUser(playerMZ);

            //user is already in a database, but is marked as deleted
            Optional<UserData> playerInDB=this.userDAO.findById(playerMZ.getUserId());
            if(playerInDB.isPresent())
            {
                UserData playerToUpdate= playerInDB.get();
                playerToUpdate.setDeleted(false);

                this.userDAO.saveUser(playerToUpdate);
                try{
                    return   redisUserService.addUserToRedis(playerToUpdate);

                }
                catch (Exception e)
                {
                    return null;
                }
            }
            else // user does not exist in a databes -> adding
            {
                playerMZ=  this.userDAO.saveUser(playerMZ);
                try{
                    return   redisUserService.addUserToRedis(playerMZ);

                }
                catch (Exception e)
                {
                    return null;
                }
            }




        }
       catch(Exception e)
       {
           return null;
       }



    }

    @Override
    @Transactional
    public void deleteUser(String chosenUser, List<String> chosenUsers)
    {

/*
        //wpisany z input
        if(chosenUser!="")
        {
            List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();
           List<UserData>users= userDAO.findUsersFromCache_All();


            if(users==null)//users=userDAO.findAll(false);
                users=userDAO.findNotDeletedUsers();



            //czy jest o takim id w mz
            var playerMZ= mzUserService.findByUsername(chosenUser);

            if(!users.stream()
                    .filter(a->a.getUserId().equals(playerMZ.getUserId())
                    ).findFirst().isEmpty()
            )
            {
                //     this.userDAO.delete(playerMZ);
                ( (CustomUserDAO) userDAO).delete(playerMZ);
                //    mzCache.setUsers( userDAOImpl.findAll(false) );
                //        lkUserService.usunGraczaZXML(graczMZ);
            }
            else
            {
                //todo nie bylo takiego grajka
                int i=0;
            }

        }
//z checkboxow
        else   if(chosenUsers!= null)
        {
            List<UserData>users= userDAO.findUsersFromCache_All();
            if(users==null)//users=userDAO.findAll(false);
                users=userDAO.findNotDeletedUsers();
            //czy jest o takim id w mz

            for (var item: chosenUsers
            ) {
                var playerMZ= mzUserService.findByUsername(item);
                if(!users.stream()
                        .filter(a->a.getUserId().equals(playerMZ.getUserId())
                        ).findFirst().isEmpty()
                )
                {
                    //        lkUserService.usunGraczaZXML(gracz);

                    //   this.userDAO.delete(playerMZ);
                    ( (CustomUserDAO) userDAO).delete(playerMZ);
                }
                else
                {
                    int t=0;
                    //todo nie bylo takiego grajka
                    // tutaj nie powinno sie wydarzyc, bo przy dodawaniu sprawdzane jest czy
                    // jest taki w mz, a tutaj usuwane są z checkboxów ustawianych z bazy

                }
            }


        }
*/

  //      redisUserService.getRedisTemplate().getConnectionFactory().getConnection().flushDb();


            //wpisany z input
            if(chosenUser!="")
            {
                //todo to chyba nie jest konieczne, bo skoro został dodany sprawdzając czy jest w mz, to usuwając też powinien być
                var playerMZ= mzUserService.findByUsernameInManagerzone(chosenUser);
                if(playerMZ==null)
                {
                    //todo nie ma takiego usera w mzcie
                }
                else
                {
                    List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();



                    if(!users.stream()
                            .filter(a->a.getUserId().equals(playerMZ.getUserId())
                            ).findFirst().isEmpty()
                    )
                    {
                        //customdao nie usuwa tylko ustawia deleted na true
                    //    ( (CustomUserDAO) userDAO).delete(playerMZ);
                       try {
                           userDAO.deleteUserById(playerMZ.getUserId());
                           redisUserService.deleteUser(playerMZ.getUserId());
                       }
                       catch (Exception e)
                       {
                           System.out.println("error while deleting");
                       }
                    }
                    else
                    {
                        //todo nie bylo takiego grajka
                        int i=0;
                    }

                    /** ****************************
                     * todo uncomment if need to use cache
                    mzCache.setUsers( userDAO.findNotDeletedUsers() );

                    */
                }


            }
//z checkboxow
            else   if(chosenUsers!= null)
            {
                List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();

                for (var item: chosenUsers
                ) {

               Optional<UserData> userInDb=     users.stream()
                            .filter(a->a.getUsername().trim().toLowerCase().equals(item.trim().toLowerCase())
                            ).findFirst();
                  //todo sprawdzić tego ifa
                    if(!userInDb.isEmpty()
                    )
                    {
                        try {
                         //   ( (CustomUserDAO) userDAO).delete(userInDb);
                             userDAO.deleteUserById(userInDb.get().getUserId());
                             redisUserService.deleteUser(userInDb.get().getUserId());
                        } catch (Exception e) {
                            System.out.println("error while deleting");
                           //todo \/
                          // e.redirectToErrorPage();
                        }
                    }
                    else
                    {
                        UserData user=userDAO.findByName(item);
if(user== null)
{
    //todo redirect error nie ma takiego usera w bazie
}
else { // jest taki user ale usunięty (to się może zdarzyć jak Redis będzie nieaktualny)
    //aktualizowanie redisa
    redisUserService.deleteUser(user.getUserId());
}
                        //todo nie bylo takiego grajka
                        // tutaj nie powinno sie wydarzyc, bo przy dodawaniu sprawdzane jest czy
                        // jest taki w mz, a tutaj usuwane są z checkboxów ustawianych z bazy

                    }
                }
/** ****************************
 * todo uncomment if need to use cache
                mzCache.setUsers( userDAO.findNotDeletedUsers() );
 */
            }










    }

 /*   @Override
    public List<UserData> findAllUsersFromCache() {



        return mzCache.getUsers();
    }*/

 /*   @Override
    public List<UserDataDTO> findUsers_NotDeletedWithoutPause() {
        //próbowanie z cache
        *//** ****************************
         * todo uncomment if need to use cache
        System.out.println("trying find users in cache");
List<UserData> users=userDAO.findUsersFromCache_NotDeletedWithoutPause();
if(users.size()!=0)
{
    System.out.println("found users in cache");
    return users;
}
else
{*//*


        System.out.println("Trying to find users in redis");
        List<UserDataDTO> userDataDTO=redisService.GetUsers_NotDeletedWithoutPause();

if(userDataDTO.isEmpty())
{
    System.out.println("...not found. Trying in db");
    //todo zrobic redirect ze nie polaczono z db
    System.out.println("trying find users in db");
    List<UserData> users= userDAO.findUsers_NotDeletedWithoutPause();
    if(!users.isEmpty())
    {
        *//** ****************************
         * todo uncomment if need to use cache
         mzCache.setUsers(users);
         *//*
        System.out.println("found users in db");
       String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
      return  userDataDTO = gsonService.jsonToList(userDataDTOString,UserDataDTO.class);

    }
    else //no users in redis nor in db
    {
        return null;
    }
}
else
{
    return userDataDTO;
    System.out.println("found users in redis");
}







//}

    }
*/

    @Override
    public List<UserDataDTO> findAllUsers(boolean deleted,boolean withPause){
        System.out.println("Trying to find users in redis");

        List<UserDataDTO> userDataDTO= redisUserService.getUsers(deleted,withPause);

        if(deleted==true)
        {
            if (withPause==true)
            {
                if(userDataDTO==null)
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<UserData> users= userDAO.findUsers_DeletedWithPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis findUsers_DeletedWithPause");
                        //    String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
                        return   redisUserService.addAllUsers(users,UserData.class,deleted,withPause);
                        //       return   gsonService.jsonToList(userDataDTOString,UserDataDTO.class);

                    }
                    else //no users in redis nor in db
                    {
                        return null;
                    }
                }
                else
                {
                    System.out.println("found users in redis");
                    return userDataDTO;

                }

            }
            else
            {
                if(userDataDTO==null)
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<UserData> users= userDAO.findUsers_DeletedWithoutPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis findUsers_DeletedWithoutPause");
                        //    String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
                        return   redisUserService.addAllUsers(users,UserData.class,deleted,withPause);
                        //       return   gsonService.jsonToList(userDataDTOString,UserDataDTO.class);

                    }
                    else //no users in redis nor in db
                    {
                        return null;
                    }
                }
                else
                {
                    System.out.println("found users in redis");
                    return userDataDTO;

                }

            }
        }
        else
        {
            if (withPause==true)
            {
               /* System.out.println("Trying to find users in redis");

                List<UserDataDTO> userDataDTO=redisService.getUsers(false,true);*/

                if(userDataDTO==null)
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<UserData> users= userDAO.findUsers_NotDeletedWithPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis findUsers_NotDeletedWithPause");
                        //    String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
                      return   redisUserService.addAllUsers(users,UserData.class,false,true);
                 //       return   gsonService.jsonToList(userDataDTOString,UserDataDTO.class);

                    }
                    else //no users in redis nor in db
                    {
                        return null;
                    }
                }
                else
                {
                    System.out.println("found users in redis");
                    return userDataDTO;

                }




            }
            else
            {
                System.out.println("Trying to find users in redis");

             //   List<UserDataDTO> userDataDTO=redisService.getUsers(false,false);
                if(userDataDTO==null)
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<UserData> users= userDAO.findUsers_NotDeletedWithoutPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis Users_NotDeletedWithoutPause");
                    return redisUserService.addAllUsers(users,UserData.class,false,false);
                    //    return   gsonService.jsonToList(userDataDTOString,UserDataDTO.class);

                    }
                    else //no users in redis nor in db
                    {
                        return null;
                    }
                }
                else
                {
                    System.out.println("found users in redis");
                    return userDataDTO;

                }


            }
        }
    }
/*
    @Override
    public List<UserDataDTO> findUsers_NotDeletedWithPause(){
        */
/** ****************************
         * todo uncomment if need to use cache
        //próbowanie z cache
        System.out.println("trying find users in cache");
        List<UserData> users =userDAO.findUsersFromCache_NotDeletedWithPause();
     //  users.get(5);

        if (users.size() != 0) {
            System.out.println("found users in cache");
            return users;
        } else {
         *//*

            //todo zrobic redirect ze nie polaczono z db
            System.out.println("trying find users in db");

        List<UserData> users = userDAO.findUsers_NotDeletedWithPause();

*/
/**
 * ***************************
 * todo uncomment if need to use cache
 * mzCache.setUsers(users);
 *//*

            System.out.println("found users in db");
            return users;
   //     }
    }
*/

    @Override
//@Transactional
    public UserDataDTO addUser(String userToAdd) {
   /*     //czy jest o takim id w mz
        var playerMZ= mzUserService.findByUsernameINManagerzone(userToAdd);
        //    var gracze=lkUserService.wczytajGraczyZXML();

        if(playerMZ==null)
        {
            //todo return że nie ma takiego usera w MZ
            System.out.println("nie ma takiego usera w mz");
            return null;
        }
        else
        {
            if(playerMZ.getTeamlist().get(0).getTeamName().equals(""))
            {
                System.out.println("nie ma takiej druzyny w mz");
                return null;
            }


            List<UserData>users= userDAO.findUsersFromCache_All();
            if(users==null)
                //  users=userDAO.findAll(false);
                users=userDAO.findNotDeletedUsers();

            users= users.stream().sorted(
                    (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
            ).collect(Collectors.toList());


            if(users!=null)
            {
                if(!users.stream()
                        .filter(a->a.getUserId().equals(playerMZ.getUserId())
                        ).findFirst().isEmpty()
                )
                {
                    //todo taki gracz juz byl dodany
                    int i=0;
                }
                else
                {
                    //     lkUserService.dodajGraczaDoXML(graczMZ);
                 //   playerMZ.getTeamlist().get(0).setUser(playerMZ);
                    //       this.userDAO.save(playerMZ);
              var  savedUser=    this.saveUser(playerMZ);
              */
        /** ****************************
         * todo uncomment if need to use cache
         mzCache.addUser(playerMZ);
         */

        //czy jest o takim id w mz
        UserData playerMZ = mzUserService.findByUsernameInManagerzone(userToAdd.trim());
        //    var gracze=lkUserService.wczytajGraczyZXML();
        if (playerMZ != null) {
            Optional<UserData> userInDB = userDAO.findById(playerMZ.getUserId());


            if (userInDB.isEmpty()) { //there is no this user in the database
                try {
                    playerMZ.getTeamlist().get(0).setUser(playerMZ);
                    UserData addedUser = userDAO.saveUser(playerMZ);
                    System.out.println("added user to database");
                 //   redisService.addUserToUserLists(addedUser);
                    return redisUserService.addUserToRedis(addedUser);


                } catch (Exception e) {
                    System.out.println("error in adding");
                    return null;
                }

            } else   if(userInDB.get().getDeleted()==true)
               {
                   try {
                       System.out.println("user was found deleted");
                       UserData playerToUpdate = userInDB.get();
                       playerToUpdate.setDeleted(false);
                       //     playerMZ.getTeamlist().get(0).setUser(playerMZ);
                       UserData addedUser = userDAO.saveUser(playerToUpdate);

                       //  redisService.addUserToUserLists(addedUser);
                       //   UserDataDTO editedUser=  redisService.updateUserInRedis(addedUser);
                       //  redisService.editUserInUserLists(addedUser);
                       return redisUserService.addUserToRedis(addedUser);
                   }
                    catch(Exception e)
                   {
                       System.out.println("error in updating deleted user");
                return null;
                   }
               }
               else {
                   return null;
               }

        } else {
            return null;
        }


    }

    @Override
    public UserData getPauseObject() {
        Optional<UserData> pauseUserData=  userDAO.findById(0);
        if(pauseUserData.isPresent())
        {


            System.out.println("added pause object from database");
            return pauseUserData.get();
        }
        else
        {
            UserData newPauseUserData = new UserData();
            newPauseUserData.setUserId(0);
            newPauseUserData.setUsername("pauza");
            Team tempTeam= new Team();
            tempTeam.setTeamName(" ");
            tempTeam.setTeamId(0);
            tempTeam.setUser(newPauseUserData);
            List<Team> tempTeams= new ArrayList<>();
            tempTeams.add(tempTeam);
            newPauseUserData.setTeamlist(tempTeams);
            newPauseUserData.setReliability(0);

            this.userDAO.saveUser(newPauseUserData);
            System.out.println("created new pause object");
            return newPauseUserData;
        }







        /** ****************************
         * todo uncomment if need to use cache

         if(mzCache.getUsers().size()!=0)
         mzCache.addUser(tempuser);
         */
    }

    @Override
    public UserData getUserById(Integer userId) {

        //todo dodać tu pobieranie z redis jeśli jest w redis
       return userDAO.findById(userId).orElse(null);


    }

@Override
   public UserData getUserDataByUsername(String username)
    {
        return userDAO.findByName(username);

    }
  /*  @Override
    public UserDataDTO findUserById(Integer id) {
 UserDataDTO userFromRedis= redisService.findUserById(id);
        if(userFromRedis!=null)
        {
return userFromRedis;
        }
        else
        {
            UserData userFromDatabase=userDAO.findById(id).get();
            if(userFromDatabase!=null)
            {

              return   new UserDataDTO(userFromDatabase);

            }
            else
                return null;
        }

    }*/
}





