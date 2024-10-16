package LKManager.services;

import LKManager.DAO_SQL.LeagueParticipantsDAO;
import LKManager.DAO_SQL.UserDAO;
import LKManager.Security.EmailEncryption;
import LKManager.Security.SpringSecurityConfig;
import LKManager.model.LeagueParticipants;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.RecordsAndDTO.UserMzDTO;
import LKManager.model.account.User;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.Role;
import LKManager.model.UserMZ.Team;
import LKManager.model.account.SignUpForm;
import LKManager.model.account.UserSettingsFormModel;
import LKManager.services.RedisService.RedisScheduleService;
import LKManager.services.RedisService.RedisUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

//@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserDAO userDAO;
   //private final UserService userService;
   private final MZUserService mzUserService;
   private final RedisUserService redisUserService;
  // private final SpringSecurityConfig springSecurityConfig;
 /*   @Autowired
    MZCache mzCache;*/
   private final LeagueParticipantsDAO leagueParticipantsDAO;
private final PasswordEncoder passwordEncoder;
private final GsonService gsonService;
private final RedisScheduleService redisScheduleService;
private final EntityManager entityManager;




    @Override
    @Transactional
    public User changeUserLeagueParticipation(UserSettingsFormModel userSettingsFormModel, LeagueParticipation leagueParticipation) {

        Optional<User> user= Optional.ofNullable(userDAO.findUserByName(userSettingsFormModel.getUser().getUsername()));


        switch (leagueParticipation)
        {
            case SUBBED, SIGNED:
            {

                if(user.isPresent())
                { User userToChange=user.get();
                    userToChange.setLeagueParticipation(leagueParticipation);
                    User changedUser=   userDAO.save(userToChange);
               //     redisUserService.updateUserInRedis(changedUser);
                    redisUserService.saveOrUpdateUserInUserLists(changedUser);
                    userDAO.flush();

                  Optional<LeagueParticipants> isAlreadyInList= leagueParticipantsDAO.findAll().stream().filter(p->p.getParticipantID().equals(changedUser.getId())).findFirst();
                  if(isAlreadyInList.isEmpty())
                  {
                      LeagueParticipants participant=    new LeagueParticipants(changedUser);

                      leagueParticipantsDAO.save(participant);//addUser(user.get());
                      //   leagueParticipantsDAO.addUser(participant.getParticipantID(), participant.getParticipantID());
                      return changedUser;
                  }
                  else
                  {
                      return changedUser;
                  }


                }

                break;
            }
            case UNSIGNED:
            {
                if(user.isPresent())
                { User userToChange=user.get();
                    userToChange.setLeagueParticipation(leagueParticipation);
                    User changedUser=   userDAO.save(userToChange);
                 //   redisUserService.updateUserInRedis(changedUser);
                    redisUserService.saveOrUpdateUserInUserLists(changedUser);

                    Optional<LeagueParticipants> participant=    leagueParticipantsDAO.findById(userSettingsFormModel.getUser().getUserId());
                  if(  participant.isPresent())leagueParticipantsDAO.delete(participant.get());

                    return changedUser;
                }

            }
        }




        return null;
    }


 /*   @Transactional
    private boolean removeLeagueParticipant(String username) {
        // LeagueParticipants participant =leagueParticipantsDAO.findById(userId).orElse(null);




    }*/

    @Override
    public Optional<MZUserData> findMzUserById(Long userId) {
        return userDAO.findMzUserById(userId);
    }

    @Override
    public UserDataDTO getUserById(Long id) {
        Optional<User>user=   userDAO.getUserById(id);

        if(user.isEmpty())
        {
            return null;
        }
        else
            return new UserDataDTO(user.get());
    }

    @Override
    @Transactional
    public boolean setMZUser(String user, String mzUsername) {
        try {
            MZUserData userInMZ=  userDAO.findMZUserByMZname(mzUsername);
            if(userInMZ==null)userInMZ= mzUserService.findByUsernameInManagerzone(mzUsername);


            if(userInMZ.getTeamlist().size()>1) {  userInMZ.getTeamlist().remove(1);}
            userInMZ.getTeamlist().get(0).setUser(userInMZ);



            User userInDB = userDAO.findUserByName(user);
            userInDB.setMzUser(userInMZ);
            userInDB.setRole(Role.ACTIVATED_CLUB_USER);
userDAO.saveUser(userInDB);
Optional<User> savedUser=userDAO.findById(userInDB.getId());
if(savedUser.isPresent())
{
   // redisUserService.updateUserInRedis(savedUser.get());
    redisUserService.saveOrUpdateUserInUserLists(savedUser.get());
}
            System.out.println("true");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("false");
            return false;
        }
    }

    @Override
    @Transactional
    public void setLeagueSignedUnsigned() {
         final int   BATCH_SIZE=50;
        Pageable pageable = PageRequest.of(0, BATCH_SIZE);
        Page<User> page;

        do {
            page = userDAO.findByLeagueParticipation(LeagueParticipation.SIGNED, pageable);
            List<User> usersToChange = page.getContent();


            usersToChange.forEach(u -> u.setLeagueParticipation(LeagueParticipation.UNSIGNED));
            List<User> savedUsers = userDAO.saveAll(usersToChange);
            savedUsers.forEach(u -> redisUserService.saveOrUpdateUserInUserLists(u));

            pageable = page.nextPageable();
        } while (page.hasNext());



/*
        List<User>usersToChange=userDAO.getLeagueSignedUsers();
           usersToChange.forEach(u->u.setLeagueParticipation(LeagueParticipation.UNSIGNED));
   List<User>savedUsers=     userDAO.saveAll(usersToChange);
        savedUsers.forEach(u->redisUserService.editUserInUserLists(u));*/

    }

    @Override
    public boolean setUsersEmail(Long userId, String email) {
       Optional<User> userTochange= userDAO.getUserById(userId);
        if(userTochange.isPresent()) {
            userTochange.get().setEmail(email);
            UserDataDTO savedUser = this.addUser(userTochange.get());
            if (savedUser != null) {
                return true;
            }
        }
       return false;

    }

    @Override
    @Transactional
    public UserDataDTO addUser(User user) {
       User savedUser= userDAO.saveUser(user);
       List<UserDataDTO> usersInRedis=redisUserService.saveOrUpdateUserInUserLists(savedUser);
      if( usersInRedis.size() !=0)
      {
          return usersInRedis.stream().filter(u->u.getUserId().equals(user.getId())).findFirst().orElse(null);
      }
else
      {
          return null;
      }

    }

    @Override
    public String getUsersEmail(Long id) throws Exception {
        String email= userDAO.getUserEmail(id);
     if(email!=null)
        return EmailEncryption.decrypt(email);
     else return null;
    }

    @Override
    public UserDataDTO getUserByUsername(String username) {
      List<UserDataDTO> redisUsers=redisUserService.getUsers(true,true);


      if(redisUsers.size()!=0)
      {
          UserDataDTO pause = redisUsers.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        if( pause==null)
        {
            User user=   userDAO.findUserByName(username);

            if(user==null)
            {
                return null;
            }
            else
                return new UserDataDTO(user);
        }
        else
        {
            return pause;
        }

      }
      else
      {
        User user=   userDAO.findUserByName(username);

          if(user==null)
          {
              return null;
          }
          else
              return new UserDataDTO(user);
      }

    }
    @Transactional
    private UserMzDTO saveMZUser(MZUserData playerMZ) {
        //dwustronny dostęp: user->team, team->user

            playerMZ.getTeamlist().get(0).setUser(playerMZ);

            //user is already in a database, but is marked as deleted
           MZUserData playerInDB=this.userDAO.findMZUserByMZname(playerMZ.getUsername());
            if(playerInDB!=null)
            {
                MZUserData playerToUpdate= playerInDB;
            //    playerToUpdate.setRole(true);

           return      this.userDAO.saveMZUser(playerToUpdate);

              //todo zapis do redisa robić przy zmianie w ustawieniach konta
            /*    try{

                    return   redisUserService.addUserToRedis(playerToUpdate);

                }
                catch (Exception e)
                {
                    return null;
                }*/
            }
            else // user does not exist in a databes -> adding
            {
                return this.userDAO.saveMZUser(playerMZ);
                //todo zapis do redisa robić przy zmianie w ustawieniach konta
            /*    try{
                    return   redisUserService.addUserToRedis(playerMZ);

                }
                catch (Exception e)
                {
                    return null;
                }*
            }




        }
       catch(Exception e)
       {
           return null;
       }
*/

            }
    }

    @Override
    @Transactional
    public void deactivateUser(String chosenUser, List<String> chosenUsers)
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

                    List<User>users= userDAO.findUsers_ActiveWithoutPause();


              Optional<User> userToDeactivate=  users.stream()
                        .filter(a->a.getUsername().equals(chosenUser)
                        ).findFirst();

                    if(!userToDeactivate.isEmpty()
                    )
                    {
                        //customdao nie usuwa tylko ustawia deleted na true
                    //    ( (CustomUserDAO) userDAO).delete(playerMZ);

                           userDAO.deactivateUserById(userToDeactivate.get().getId());
                           redisUserService.deactivateUser(userToDeactivate.get().getId());




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
//z checkboxow
            else   if(chosenUsers!= null)
            {
                List<User>users= userDAO.findUsers_ActiveWithoutPause();

                for (var item: chosenUsers
                ) {

                    Optional<User> userToDeactivate=  users.stream()
                            .filter(a->a.getUsername().equals(item)
                            ).findFirst();
                  //todo sprawdzić tego ifa
                    if(!userToDeactivate.isEmpty()
                    )
                    {
                        try {
                         //   ( (CustomUserDAO) userDAO).delete(userInDb);
                             userDAO.deactivateUserById(userToDeactivate.get().getId());
                             redisUserService.deactivateUser(userToDeactivate.get().getId());
                        } catch (Exception e) {
                            System.out.println("error while deleting");
                           //todo \/
                          // e.redirectToErrorPage();
                        }
                    }
                    else
                    {
                        User user= userDAO.findUserByName(item);
if(user== null)
{
    //todo redirect error nie ma takiego usera w bazie
}
else { // jest taki user ale usunięty (to się może zdarzyć jak Redis będzie nieaktualny)
    //aktualizowanie redisa
    redisUserService.deactivateUser(user.getId());
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
@Override
@Transactional
public UserDataDTO activateUser(String username)
{
    try{
        User user= userDAO.findUserByName(username);
        user.setRole(Role.ACTIVATED_CLUB_USER);
        User savedUeser= userDAO.saveUser(user);


      //return  redisUserService.addUserToRedis(savedUeser);
return redisUserService.saveOrUpdateUserInUserLists(savedUeser).stream().filter(u->u.getUserId().equals(savedUeser.getId())).findFirst().orElse(null);
    }
catch (Exception e)
{
    return null;
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
    public List<UserDataDTO> findAllMZUsers(boolean active, boolean withPause){
        System.out.println("Trying to find users in redis");

        List<UserDataDTO> userDataDTO= redisUserService.getUsers(active,withPause);

        if(active==false)
        {
            if (withPause==true)
            {
                if(userDataDTO.isEmpty())
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<User> users= userDAO.findUsers_DeactivatedWithPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis findUsers_DeletedWithPause");
                        //    String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
                        return   redisUserService.addAllUsers(users, User.class,active,withPause);
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
                if(userDataDTO.isEmpty())
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<User> users= userDAO.findUsers_DeactivatedWithoutPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis findUsers_DeletedWithoutPause");
                        //    String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
                        return   redisUserService.addAllUsers(users, User.class,active,withPause);
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

                if(userDataDTO.isEmpty())
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<User> users= userDAO.findUsers_ActiveWithPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis findUsers_NotDeletedWithPause");
                        //    String userDataDTOString= redisService.AddAllUsers_NotDeletedWithoutPause(users);
                      return   redisUserService.addAllUsers(users, User.class,true,true);
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
                if(userDataDTO.isEmpty())
                {
                    System.out.println("...not found. Trying in db");
                    //todo zrobic redirect ze nie polaczono z db
                    System.out.println("trying find users in db");
                    List<User> users= userDAO.findUsers_ActiveWithoutPause();
                    if(!users.isEmpty())
                    {
                        /** ****************************
                         * todo uncomment if need to use cache
                         mzCache.setUsers(users);
                         */
                        System.out.println("found users in db");
                        System.out.println("updating redis Users_NotDeletedWithoutPause");
                List<UserDataDTO>l=     redisUserService.addAllUsers(users, User.class,true,false);
                        return l;
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
    @Transactional
    public UserDataDTO addUser(SignUpForm userFromForm) {


User userToAdd= new User();
userToAdd.setUsername(userFromForm.getUsername());
userToAdd.setEmail(userFromForm.getEmail());
//bez hasła tylko w dodawaniu "temp" graczy z zakładki user u admina
if(userFromForm.getUsername().contains("temp_")||userFromForm.getPassword()==null)
{

    userToAdd.setPassword(SpringSecurityConfig.passwordEncoder().encode("temp123" ));
userToAdd.setRole(Role.ACTIVATED_CLUB_USER);
}
else
{
    userToAdd.setPassword(userFromForm.getPassword());
}

userToAdd.setRole(Role.UNACTIVATED_CLUB_USER);
userToAdd.setReliability(0);
userToAdd.setLeagueParticipation(LeagueParticipation.UNSIGNED);


        User addedUser = null;



//jeśli tempuser, to przypoisuje od razu mz team, zeby nie dodawać ręcznie
        if(userFromForm.getUsername().contains("temp_")) {
            this.setMZUser(addedUser.getUsername(), addedUser.getUsername().trim().substring(5));
            //  return redisUserService.addUserToRedis(user1);
            System.out.println("subs="+addedUser.getUsername().trim().substring(5));
         //    addedUser = userDAO.saveUser(userToAdd);
        }
        else
        {
            addedUser  = userDAO.saveUser(userToAdd);
        }
   //     user1=userDAO.findUserByName(addedUser.getUsername());

redisUserService.saveOrUpdateUserInUserLists(addedUser);

        User finalUser = addedUser;
        System.out.println("here u1="+addedUser.getUsername()+" uf= "+finalUser.getUsername());
        return redisUserService.getUsers(true,true).stream().filter(u->u.getUserId().equals(finalUser.getId())).findFirst().orElse(null);
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

   /*     //czy jest o takim id w mz
        UserData playerMZ = mzUserService.findByUsernameInManagerzone(userToAdd.getUsername().trim());
        //    var gracze=lkUserService.wczytajGraczyZXML();
        if (playerMZ != null) {
            Optional<UserData> userInDB = userDAO.findById(playerMZ.getMZuser_id());


            if (userInDB.isEmpty()) { //there is no this user in the database
                try {
                //    playerMZ.getTeamlist().get(0).setUser(playerMZ);
                    playerMZ.setEmail(userToAdd.getEmail());
                    playerMZ.setRole(Role.UNACTIVATED_CLUB_USER);
                 //   playerMZ.setPassword(this.generatePassword());
                    UserData addedUser = userDAO.saveUser(playerMZ);
                    System.out.println("added user to database");
                 //   redisService.addUserToUserLists(addedUser);
                    return redisUserService.addUserToRedis(addedUser);


                } catch (Exception e) {
                    System.out.println("error in user adding");
                    return null;
                }

            } else   if(userInDB.get().getRole()==Role.DEACTIVATED_USER)
               {
                   //todo !!! w prod wersji to ma nie działać, tylko ma przekierowywać do taki gracz już istnieje zapomniales hasla(?)
            //   bo  taki user już istnieje \/ to jest tylko póki istnieją "ręcznie" tworzeni userzy bez maila i hasła

                   try {
                       System.out.println("user was found deleted");
                       UserData playerToUpdate = userInDB.get();
                       playerToUpdate.setRole(Role.UNACTIVATED_CLUB_USER);
                       playerToUpdate.setPassword(userToAdd.getPassword());
                       playerToUpdate.setEmail(userToAdd.getEmail());
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
*/
/////////////////////////////////////////////////////////////////////////

     /*   MZUserData playerMZ = mzUserService.findByUsernameInManagerzone(userToAdd.getUsername().trim());
        //    var gracze=lkUserService.wczytajGraczyZXML();
        if (playerMZ != null) {
            Optional<User> userInDB = userDAO.findById(playerMZ.getMZuser_id());
            if (userInDB.isEmpty()) { //there is no this user in the database
                try {


                    MZUserData addedUser = userDAO.saveMZUser(playerMZ);
                    System.out.println("added user to database");
                    //   redisService.addUserToUserLists(addedUser);
                    return redisUserService.addUserToRedis(addedUser);


                } catch (Exception e) {
                    System.out.println("error in user adding");
                    return null;
                }

            }
                //todo !!! w prod wersji to ma nie działać, tylko ma przekierowywać do taki gracz już istnieje zapomniales hasla(?)
                //   bo  taki user już istnieje \/ to jest tylko póki istnieją "ręcznie" tworzeni userzy bez maila i hasła

                try {
                    System.out.println("user was found deleted");
                    User playerToUpdate = userInDB.get();

                    User addedUser = userDAO.saveMZUser(playerToUpdate);

                    return redisUserService.addUserToRedis(addedUser);
                } catch (Exception e) {
                    System.out.println("error in updating deleted user");
                    return null;

            }


        }else return null;
*/

////////////////////////////////////////////////////////
/*
        MZUserData playerMZ = mzUserService.findByUsernameInManagerzone(userToAdd.getUsername().trim());
        //    var gracze=lkUserService.wczytajGraczyZXML();
        if (playerMZ != null) {
            Optional<MZUserData> userInDB = userDAO.findById(playerMZ.getMZuser_id());
            if (userInDB.isEmpty()) { //there is no this user in the database
                try {

                    playerMZ.setEmail(userToAdd.getEmail());
                   playerMZ.setRole(Role.UNACTIVATED_CLUB_USER);
                       playerMZ.setPassword(this.generatePassword());
                    MZUserData addedUser = userDAO.saveUser(playerMZ);
                    System.out.println("added user to database");
                    //   redisService.addUserToUserLists(addedUser);
                    return redisUserService.addUserToRedis(addedUser);


                } catch (Exception e) {
                    System.out.println("error in user adding");
                    return null;
                }

            } else if (userInDB.get().getRole() == Role.DEACTIVATED_USER) {
                //todo !!! w prod wersji to ma nie działać, tylko ma przekierowywać do taki gracz już istnieje zapomniales hasla(?)
                //   bo  taki user już istnieje \/ to jest tylko póki istnieją "ręcznie" tworzeni userzy bez maila i hasła

                try {
                    System.out.println("user was found deleted");
                    MZUserData playerToUpdate = userInDB.get();
                    playerToUpdate.setRole(Role.UNACTIVATED_CLUB_USER);
                    playerToUpdate.setPassword(userToAdd.getPassword());
                    playerToUpdate.setEmail(userToAdd.getEmail());
                    //     playerMZ.getTeamlist().get(0).setUser(playerMZ);
                    MZUserData addedUser = userDAO.saveUser(playerToUpdate);

                    //  redisService.addUserToUserLists(addedUser);
                    //   UserDataDTO editedUser=  redisService.updateUserInRedis(addedUser);
                    //  redisService.editUserInUserLists(addedUser);
                    return redisUserService.addUserToRedis(addedUser);
                } catch (Exception e) {
                    System.out.println("error in updating deleted user");
                    return null;
                }
            } else {
                return null;
            }


        }else return null;*/
    }


    @Override
    @Transactional
    public User getPauseObject() {
       // Optional<UserDataDTO> pauseInRedis = redisUserService.getUsers(true, true).stream().filter(u->u.getUsername().equals("pauza")).findFirst();


        User pauseUserData=  userDAO.findUserByName("pauza");
        if(pauseUserData!=null)
        {


            System.out.println("added pause object from database");
            return pauseUserData;
        }
        else
        {

            MZUserData tempMZUser= new MZUserData();
            Team tempTeam= new Team();
            tempTeam.setTeamName(" ");
            tempTeam.setTeamId(0);
            tempMZUser.setTeamlist(Collections.singletonList(tempTeam));
            tempMZUser.setUsername("pauza");
            tempMZUser.setMZuser_id(0L);

            userDAO.saveMZUser(tempMZUser);

Optional<MZUserData> savedPauseMZobject=userDAO.findMzUserById(0L);

            User newPauseUser = new User();

            newPauseUser.setUsername("pauza");





            tempTeam.setUser(savedPauseMZobject.get());

            newPauseUser.setMzUser(savedPauseMZobject.get());
            newPauseUser.setReliability(-9999);
            newPauseUser.setLeagueParticipation(LeagueParticipation.UNSIGNED);
            newPauseUser.setRole(Role.ACTIVATED_CLUB_USER);
            newPauseUser.setEmail("pauza@pauza");
newPauseUser.setPassword("");

      /*      Session session = sessionFactory.getCurrentSession();
            try {
                session.beginTransaction();
                session.saveOrUpdate(newPauseUser); // Użyj saveOrUpdate, aby obsłużyć nowe i istniejące obiekty
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
                throw e; // Rzuć wyjątek dalej, aby go obsłużyć wyżej
            }*/

          User user=  userDAO.save(newPauseUser);
         /* User addedUser=     this.userDAO.saveUser(newPauseUser);
            System.out.println("created new pause object");

            User user1=userDAO.findUserByName(addedUser.getUsername());
             redisUserService.addUserToRedis(user1);
            entityManager.refresh(newPauseUser);*/

            return user;
        }







        /** ****************************
         * todo uncomment if need to use cache

         if(mzCache.getUsers().size()!=0)
         mzCache.addUser(tempuser);
         */
    }

    @Override
    public MZUserData getMZUserById(Long userId) {

        //todo dodać tu pobieranie z redis jeśli jest w redis
       return userDAO.findMzUserById(userId).get();


    }

@Override
   public MZUserData getMZUserDataByUsername (String username)
    {
        return userDAO.findMZUserByMZname(username);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findUserByName(username);
    }



    public void authenticateUser(User user)
    {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // Ustawienie kontekstu bezpieczeństwa
        SecurityContextHolder.getContext().setAuthentication(authentication);
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





