package LKManager.services.RedisService;

import LKManager.DAO_SQL.UserDAO;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.account.User;
import LKManager.model.account.Role;
import LKManager.model.UserMZ.MZUserData;
import LKManager.services.Adapters.UserAdapter;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Data
public class RedisUserService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final GsonService gsonService;
  private final   ValueOperations<String, Object> valueOperations;
   private final ListOperations<String, String> listOperations;
    private final RedisTemplate<String, String > redisListTemplate;

    private final UserDAO UserDAO;
    public RedisUserService(RedisTemplate<String, Object> redisTemplate, GsonService gsonService, RedisTemplate<String, String> redisListTemplate, UserDAO UserDAO) {
        this.redisTemplate = redisTemplate;
        this.gsonService = gsonService;
        this.redisListTemplate = redisListTemplate;
        this.UserDAO = UserDAO;
        this.valueOperations=	this.getRedisTemplate().opsForValue();
        this.listOperations = this.getRedisListTemplate().opsForList();

    }


public boolean setActivationToken(String token, String username )
{
    try {
        valueOperations.set(token,username );
        redisTemplate.expire(token, 3, TimeUnit.HOURS);
        return true;
    }
    catch (Exception e)
    {
        return false;
    }
}
public Long getActivationTokenUsername(String token)
{
    try{


        String jsonUser=  (String) valueOperations.get(token);
        redisTemplate.delete(token);
        return gsonService.jsonToObject(jsonUser,Long.class);
    }
    catch (Exception e)
    {
        return null;
    }
}
    public <T> List<UserDataDTO> addAllUsers(List<T> users, Class<T> userType, boolean active,boolean withPause)
    {

        List<String> usersJson;
        List<UserDataDTO> usersDTO;
        if(userType.equals(User.class))
        {

            usersDTO=users.stream().map(user->UserAdapter.convertUserToUserDataDTO((User) user )).collect(Collectors.toList());

        /*    usersDTO=users.stream().map( user ->
             new UserDataDTO(UserAdapter.adapt((UserData) user)).collect(Collectors.toList());

*/

        } else if (userType.equals(UserDataDTO.class)) {
            usersDTO= (List<UserDataDTO>) users;


        }else return null;

        if(usersDTO!=null)
        {
        //    ValueOperations<String, Object> valueOperations =	this.getRedisTemplate().opsForValue();

            usersJson = gsonService.listToJson(usersDTO);
            if(active==false)
            {
                if (withPause==true)
                {
                  //  valueOperations.set("usersDeletedWithPause",usersJson);
                    redisTemplate.delete("usersDeactivatedWithPause");
                    listOperations.leftPushAll("usersDeactivatedWithPause",usersJson);
                    redisTemplate.expire("usersDeactivatedWithPause",2, TimeUnit.HOURS);
                    return this.getUsers(false,true);
                }
                else
                {
                //    valueOperations.set("usersDeletedWithoutPause",usersJson);

                    redisTemplate.delete("usersDeactivatedWithoutPause");
                    listOperations.leftPushAll("usersDeactivatedWithoutPause",usersJson);
                    redisTemplate.expire("usersDeactivatedWithoutPause",2, TimeUnit.HOURS);
                    return this.getUsers(false,false);
                }
            }
            else {
                if (withPause == true) {
                    redisTemplate.delete("usersActiveWithPause");
                    listOperations.leftPushAll("usersActiveWithPause",usersJson);
                    redisTemplate.expire("usersActiveWithPause",30, TimeUnit.DAYS);
                 //   valueOperations.set("usersNotDeletedWithPause",usersJson);
                    return this.getUsers(true,true);

                } else {
                    redisTemplate.delete("usersActiveWithoutPause");
                    listOperations.leftPushAll("usersActiveWithoutPause",usersJson);
                    redisTemplate.expire("usersActiveWithoutPause",30, TimeUnit.DAYS);
                  //  valueOperations.set("usersNotDeletedWithoutPause",usersJson);
                    return this.getUsers(true,false);


                }
            }


        }
        else {return null;}


    }

    public List<UserDataDTO> getUsers(boolean active,boolean withPause)
    {
        if(active==true)
        {
            if (withPause==true)
            {
                List<String> jsonStringUsers = listOperations.range("usersActiveWithPause",0,-1);
                //todo tu może być błąd?

                if(!jsonStringUsers.isEmpty())
                {
                    redisTemplate.expire("usersActiveWithPause",30, TimeUnit.DAYS);
                    //   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);// gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());
                   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers,UserDataDTO.class);


                    System.out.println(listOperations.range("usersActiveWithPause",0,-1));
                    return usersFromRedis;
                 //   return jsonStringUsers;
                }
                //  else return null;
                else return new ArrayList<UserDataDTO>();
            }
            else
            {
           //     List<String> jsonStringUsers = listOperations.range("usersDeletedWithoutPause",0,-1);
                List<String> jsonStringUsers = listOperations.range("usersActiveWithoutPause",0,-1);

                if(!jsonStringUsers.isEmpty())
                {
                    redisTemplate.expire("usersActiveWithoutPause",30, TimeUnit.DAYS);
                    //   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);// gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());
                    List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers,UserDataDTO.class);


                    return usersFromRedis;
                  //  return jsonStringUsers;
                }
                //  else return null;
                else return new ArrayList<UserDataDTO>();
            }
        }
        else {
            if (withPause == true) {


            //    var a =



             //   String jsonStringUsers = gsonService.objectToJson( valueOperations.get("usersNotDeletedWithPause"));
          //      List<String> jsonStringUsers =  listOperations.range("usersNotDeletedWithPause",0,-1);
                List<String> jsonStringUsers =  listOperations.range("usersDeactivatedWithPause",0,-1);

                if(!jsonStringUsers.isEmpty())
                {
                    redisTemplate.expire("usersDeactivatedWithPause",30, TimeUnit.DAYS);
                 //   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);// gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());
                    List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers,UserDataDTO.class);


                    System.out.println(listOperations.range("usersDeactivatedWithPause",0,-1));

                    return usersFromRedis;
               //     return jsonStringUsers;
                }
                //  else return null;
                else return new ArrayList<UserDataDTO>();



            } else {
          //      List<String> jsonStringUsers =  listOperations.range("usersNotDeletedWithoutPause",0,-1);
                List<String> jsonStringUsers =  listOperations.range("usersDeactivatedWithoutPause",0,-1);
                  if(!jsonStringUsers.isEmpty()) {
                      redisTemplate.expire("usersDeactivatedWithoutPause",30, TimeUnit.DAYS);
                    List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);//gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());


                    System.out.println(listOperations.range("usersDeactivatedWithoutPause",0,-1));
                    return usersFromRedis;
               //       return jsonStringUsers;
                }
              //  else return null;
                  else return new ArrayList<UserDataDTO>();
            }
        }
    }





//todo to chjyba usunąć, bo lepiej chyba pobrać z redis całą listę i wyszukać niż zapisywać dodatkowo w redis każdego usera oddzielnie
    public UserDataDTO addUserToRedis(Object user )
    {

       String userJson;
        if (user instanceof User) {

        userJson  = mapUserToJsonString(UserAdapter.convertUserToUserDataDTO((User) user));
     valueOperations.set("user:" + ((User)user).getUsername(), userJson);
            redisTemplate.expire("user:" + ((User)user).getUsername(),1, TimeUnit.DAYS);

            System.out.println("addeduser to redis");
            this.addUserToUserLists((User) user);


            return this.getUserByUsername(((User) user).getUsername());
        }
        else if (user instanceof UserDataDTO) {
            userJson  = mapUserToJsonString( user);
            valueOperations.set("user:" + ((UserDataDTO)user).getUserId(), userJson);
            redisTemplate.expire("user:" + ((UserDataDTO)user).getUsername(),1, TimeUnit.DAYS);
            System.out.println("addeduser to redis");
            this.addUserToUserLists((UserDataDTO)user);


            return this.getUserByUsername(((UserDataDTO) user).getUsername());
        } else {
return null;
        }








    }

    private UserDataDTO getUserByUsername(String username) {
      String jsonUser=  (String) valueOperations.get("user:"+username);
        redisTemplate.expire("user:"+username,2, TimeUnit.DAYS);
        return gsonService.jsonToObject(jsonUser,UserDataDTO.class);
    }

    public MZUserData DeleteUserFromRedis()
    {
        return null;
    }


/*    private List<String> mapListUserDataToJsonString(List<MZUserData> users) {

        List<UserDataDTO> usersDTO = users.stream().map(UserAdapter::convertMZUserDataToUserMzDTO).collect(Collectors.toList());


        return gsonService.listToJson(usersDTO);
    }*/
    private String mapUserToJsonString(Object user) {
        if (user instanceof MZUserData) {


            return gsonService.objectToJson(UserAdapter.convertMZUserDataToUserMzDTO((MZUserData) user));
        } else if (user instanceof UserDataDTO) {
            return gsonService.objectToJson(user);
        } else {
            return null;
        }


    }

    public UserDataDTO findUserById(Long id) {

        UserDataDTO user=   gsonService.jsonToObject((String)valueOperations.get("user"+id),UserDataDTO.class);
        redisTemplate.expire("user"+id,2, TimeUnit.DAYS);
        return user;
    }

    /**
     * Adding user to Redis lists:  usersDeletedWithoutPause and usersDeletedWithPause
     *
     * @param userToAdd
     * @return List<UserDataDTO>
     */
    @Transactional
    private List<UserDataDTO> addUserToUserLists(Object userToAdd) {
if(userToAdd instanceof User)
{
    User MZUserDataToadd =(User) userToAdd;
    String usersJson;

    if(MZUserDataToadd.getRole()!=Role.DEACTIVATED_USER)
    {

        List<UserDataDTO> usersActiveWithPause=this.getUsers(true,true);
        usersActiveWithPause.add(UserAdapter.convertUserToUserDataDTO(MZUserDataToadd));
        this.addAllUsers(usersActiveWithPause,UserDataDTO.class,true,true);

    //    usersJson = gsonService.listToJson(usersDeletedWithPause);
  //      valueOperations.set("usersDeletedWithPause",usersJson);

        List<UserDataDTO>     usersActiveWithoutPause   =    this.getUsers(true,false);
        usersActiveWithoutPause.add(UserAdapter.convertUserToUserDataDTO(MZUserDataToadd));
        this.addAllUsers(usersActiveWithoutPause,UserDataDTO.class,true,false);

   //     usersJson = gsonService.listToJson(usersDeletedWithoutPause);
    //    valueOperations.set("usersDeletedWithoutPause",usersJson);
        return getUsers(true,true);
    }
    else {
        List<UserDataDTO>    usersDeactivatedWithPause=   this.getUsers(false,true);
        usersDeactivatedWithPause.add(UserAdapter.convertUserToUserDataDTO(MZUserDataToadd));
   // this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,true);
   //     usersJson = gsonService.listToJson(usersNotDeletedWithPause);
//valueOperations.set("usersNotDeletedWithPause",usersJson);
        this.addAllUsers(usersDeactivatedWithPause,UserDataDTO.class,false,true);

        List<UserDataDTO>    usersDeactivatedWithoutPause  =  this.getUsers(false,false);
        usersDeactivatedWithoutPause.add(UserAdapter.convertUserToUserDataDTO(MZUserDataToadd));

      //  this.addAllUsers(usersNotDeletedWithoutPause,UserDataDTO.class,false,false);
   //     usersJson = gsonService.listToJson(usersNotDeletedWithoutPause);
    //    valueOperations.set("usersNotDeletedWithoutPause",usersJson);
        this.addAllUsers(usersDeactivatedWithPause,UserDataDTO.class,false,false);
        return getUsers(false,true);
    }


} else if (userToAdd instanceof  UserDataDTO) {
    UserDataDTO userDataDTOtoAdd=(UserDataDTO) userToAdd;

    if(userDataDTOtoAdd.getRole()!=Role.DEACTIVATED_USER)
    {
        System.out.println("tu");
        List<UserDataDTO> usersActiveWithPause=this.getUsers(true,true);
        usersActiveWithPause.add(userDataDTOtoAdd);
        this.addAllUsers(usersActiveWithPause,UserDataDTO.class,true,true);

        List<UserDataDTO>     usersActiveWithoutPause   =    this.getUsers(true,false);
        usersActiveWithoutPause.add(userDataDTOtoAdd);
        this.addAllUsers(usersActiveWithoutPause,UserDataDTO.class,true,false);

        return getUsers(true,true);
    }
    else {
        List<UserDataDTO>    usersDeactivatedWithPause=   this.getUsers(false,true);
        usersDeactivatedWithPause.add(userDataDTOtoAdd);
        this.addAllUsers(usersDeactivatedWithPause,UserDataDTO.class,false,true);


        List<UserDataDTO>    usersDeactivatedWithoutPause  =  this.getUsers(false,false);
        usersDeactivatedWithoutPause.add(userDataDTOtoAdd);
        System.out.println("tam");
        this.addAllUsers(usersDeactivatedWithoutPause,UserDataDTO.class,false,false);

        return getUsers(false,true);
    }


}
else return null;







    }

    public UserDataDTO updateUserInRedis(User userToEdit) {




            //   UserDataDTO userInRedis=  this.findUserById(((UserData)userToEdit).getUserId());
            UserDataDTO   userInRedis=this.findUserById(( userToEdit.getId()));
            if(userInRedis==null)
            {
                userInRedis=this.addUserToRedis(userToEdit);
            }
            // UserDataDTO userToEditDTO=    UserAdapter.adapt((UserData) userToEdit);
            userInRedis.setRole(( userToEdit).getRole());
            userInRedis.setTeamName( userToEdit.getMzUser().getTeamlist().get(0).getTeamName());

            this.saveOrUpdateUserInUserLists(userToEdit);
            return   this.addUserToRedis(userInRedis);



  /*      if (userToEdit instanceof MZUserData) {


         //   UserDataDTO userInRedis=  this.findUserById(((UserData)userToEdit).getUserId());
            UserDataDTO   userInRedis=this.findUserById((((MZUserData) userToEdit).getMZuser_id()));
            if(userInRedis==null)
            {
                userInRedis=this.addUserToRedis((MZUserData)userToEdit);
            }
               // UserDataDTO userToEditDTO=    UserAdapter.adapt((UserData) userToEdit);
                userInRedis.setRole(((MZUserData) userToEdit).getRole());
                userInRedis.setTeamName(((MZUserData) userToEdit).getTeamlist().get(0).getTeamName());

            this.editUserInUserLists(userToEdit);
          return   this.addUserToRedis(userInRedis);

        } else if (userToEdit instanceof UserDataDTO) {

            UserDataDTO   userInRedis=this.findUserById((((UserDataDTO) userToEdit).getUserId()));
            if(userInRedis==null)
            {
                userInRedis=this.addUserToRedis((MZUserData)userToEdit);
            }
            userInRedis.setRole(((UserDataDTO) userToEdit).getRole());
            userInRedis.setTeamName(((UserDataDTO) userToEdit).getTeamName());
            this.editUserInUserLists(userToEdit);
        return     this.addUserToRedis(userInRedis);

        } else {
            return null;
        }
*/

    }

    public List<UserDataDTO> saveOrUpdateUserInUserLists(Object editedUser) {



        if (editedUser instanceof User) {
            if(((User)editedUser).getRole()!=Role.DEACTIVATED_USER
            )
            {






                List<UserDataDTO> usersActiveWithPause=this.getUsers(true,true);
                UserDataDTO  userInActiveWithPause= usersActiveWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                List<UserDataDTO>     usersActiveWithoutPause   =    this.getUsers(true,false);
                UserDataDTO  userInActiveWithoutPause= usersActiveWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                if(userInActiveWithPause==null||userInActiveWithoutPause==null)
                {
                    this.addUserToUserLists(editedUser);

                    //todo te 2 linijki nie wiem czy potrzebne , sprawdzic jeszcze raz
                  usersActiveWithPause=this.getUsers(true,true);
                    usersActiveWithoutPause   =    this.getUsers(true,false);


                    System.out.println("saveOrUpdateUserInUserLists - "+((User) editedUser).getUsername());

                    userInActiveWithPause= usersActiveWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                    userInActiveWithoutPause= usersActiveWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                }

                //  UserDataDTO  userInRedis= usersActiveWithPause.get(((UserData) editedUser).getUserId());
                userInActiveWithPause.setRole(((User) editedUser).getRole());
                userInActiveWithPause.setReliability(((User) editedUser).getReliability());
                userInActiveWithPause.setLeagueParticipation(((User) editedUser).getLeagueParticipation());
         //       userInActiveWithPause.setTeamName(((User) editedUser).getMzUser().getTeamlist().get(0).getTeamName());
                this.addAllUsers(usersActiveWithPause,UserDataDTO.class,true,true);


             
                //    UserDataDTO  userInRedis1= usersActiveWithoutPause.get(((UserData) editedUser).getUserId());
                userInActiveWithoutPause.setRole(((User) editedUser).getRole());
                userInActiveWithoutPause.setReliability(((User) editedUser).getReliability());
                userInActiveWithoutPause.setLeagueParticipation(((User) editedUser).getLeagueParticipation());
           //     userInActiveWithoutPause.setTeamName(((User) editedUser).getMzUser().getTeamlist().get(0).getTeamName());
                return        this.addAllUsers(usersActiveWithoutPause,UserDataDTO.class,true,false);
            }
            else {

                List<UserDataDTO>    usersDeactivatedWithPause=   this.getUsers(false,true);
                UserDataDTO  userInactiveWithPause= usersDeactivatedWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                List<UserDataDTO>    usersDeactivatedWithoutPause  =  this.getUsers(false,false);
                UserDataDTO  userInactiveWithoutPause= usersDeactivatedWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                if(userInactiveWithPause==null||userInactiveWithoutPause==null)
                {
                    this.addUserToUserLists(editedUser);
                    userInactiveWithPause= usersDeactivatedWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                    userInactiveWithoutPause= usersDeactivatedWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                }
                
                
         
          //      UserDataDTO  userInRedis= usersDeactivatedWithPause.get(((UserData) editedUser).getUserId());
                userInactiveWithPause.setRole(((User) editedUser).getRole());
                userInactiveWithPause.setReliability(((User) editedUser).getReliability());
                userInactiveWithPause.setLeagueParticipation(((User) editedUser).getLeagueParticipation());
        //        userInactiveWithPause.setTeamName(((User) editedUser).getMzUser().getTeamlist().get(0).getTeamName());
                this.addAllUsers(usersDeactivatedWithPause,UserDataDTO.class,false,true);

               
         
            //    UserDataDTO  userInRedis1= usersDeactivatedWithoutPause.get(((UserData) editedUser).getUserId());
                userInactiveWithoutPause.setLeagueParticipation(((User) editedUser).getLeagueParticipation());
                userInactiveWithoutPause.setRole(((User) editedUser).getRole());
                userInactiveWithoutPause.setReliability(((User) editedUser).getReliability());
           //     userInactiveWithoutPause.setTeamName(((User) editedUser).getMzUser().getTeamlist().get(0).getTeamName());
                return     this.addAllUsers(usersDeactivatedWithoutPause,UserDataDTO.class,false,false);

            }


        } else if (editedUser instanceof UserDataDTO) {

            if(((UserDataDTO)editedUser).getRole()!=Role.DEACTIVATED_USER
            )
            {
                List<UserDataDTO> usersActiveWithPause=this.getUsers(true,true);
                UserDataDTO  userInActiveWithPause= usersActiveWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                List<UserDataDTO>     usersActiveWithoutPause   =    this.getUsers(true,false);
                UserDataDTO  userInActiveWithoutPause= usersActiveWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                if(userInActiveWithPause==null||userInActiveWithoutPause==null)
                {
                    this.addUserToUserLists(editedUser);
                    userInActiveWithPause= usersActiveWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                    userInActiveWithoutPause= usersActiveWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                }

                  //      UserDataDTO  userInRedis= usersActiveWithPause.get(((UserDataDTO) editedUser).getUserId());
                userInActiveWithPause.setRole(((UserDataDTO) editedUser).getRole());
                userInActiveWithPause.setReliability(((UserDataDTO) editedUser).getReliability());
                userInActiveWithPause.setLeagueParticipation(((UserDataDTO) editedUser).getLeagueParticipation());
           //     userInActiveWithPause.setTeamName(((UserDataDTO) editedUser).getTeamName());
                this.addAllUsers(usersActiveWithPause,UserDataDTO.class,true,true);

                 //     UserDataDTO  userInRedis1= usersActiveWithoutPause.get(((UserDataDTO) editedUser).getUserId());
                userInActiveWithoutPause.setRole(((UserDataDTO) editedUser).getRole());
                userInActiveWithoutPause.setReliability(((UserDataDTO) editedUser).getReliability());
                userInActiveWithoutPause.setLeagueParticipation(((UserDataDTO) editedUser).getLeagueParticipation());
        //        userInActiveWithoutPause.setTeamName(((UserDataDTO) editedUser).getTeamName());
                return    this.addAllUsers(usersActiveWithoutPause,UserDataDTO.class,true,false);

            }
            else {
                List<UserDataDTO>    usersDeactivatedWithPause=   this.getUsers(false,true);
                UserDataDTO  userInactiveWithPause= usersDeactivatedWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                List<UserDataDTO>    usersDeactivatedWithoutPause  =  this.getUsers(false,false);
                UserDataDTO  userInactiveWithoutPause= usersDeactivatedWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                if(userInactiveWithPause==null||userInactiveWithoutPause==null)
                {
                    this.addUserToUserLists(editedUser);
                    userInactiveWithPause= usersDeactivatedWithPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);
                    userInactiveWithoutPause= usersDeactivatedWithoutPause.stream().filter(u->u.getUserId().equals(((User) editedUser).getId())).findFirst().orElse(null);

                }

             //      UserDataDTO  userInRedis= usersDeactivatedWithPause.get(((UserDataDTO) editedUser).getUserId());
                userInactiveWithPause.setRole(((UserDataDTO) editedUser).getRole());
                userInactiveWithPause.setReliability(((UserDataDTO) editedUser).getReliability());
                userInactiveWithPause.setLeagueParticipation(((UserDataDTO) editedUser).getLeagueParticipation());
           //     userInactiveWithPause.setTeamName(((UserDataDTO) editedUser).getTeamName());
                this.addAllUsers(usersDeactivatedWithPause,UserDataDTO.class,false,true);

              //   UserDataDTO  userInRedis1= usersDeactivatedWithoutPause.get(((UserDataDTO) editedUser).getUserId());
                userInactiveWithoutPause.setRole(((UserDataDTO) editedUser).getRole());
                userInactiveWithoutPause.setReliability(((UserDataDTO) editedUser).getReliability());
                userInactiveWithoutPause.setLeagueParticipation(((UserDataDTO) editedUser).getLeagueParticipation());
         //       userInactiveWithoutPause.setTeamName(((UserDataDTO) editedUser).getTeamName());
                return   this.addAllUsers(usersDeactivatedWithoutPause,UserDataDTO.class,false,false);

            }

        } else {
            return null;
        }




    }

    public UserDataDTO deactivateUser(Long userId) {


//todo dodać aktualizowanie/dodawanie do listy usuniętych jakby trzeba było dodawać. Dodać też




        UserDataDTO user = deactivatingUser(userId);
        if (user == null) return null;


        else //usuwanie z aktywnych
        {
            List<UserDataDTO> listOfActiveWithPause=  this.getUsers(true,true).stream().filter(u->!u.getUserId().equals(userId)).collect(Collectors.toList());
            this.addAllUsers(listOfActiveWithPause,UserDataDTO.class,true,true);

            List<UserDataDTO> listOfActiveWithoutPause=  this.getUsers(true,false).stream().filter(u->!u.getUserId().equals(userId)).collect(Collectors.toList());
            this.addAllUsers(listOfActiveWithoutPause,UserDataDTO.class,true,false);

        }


//todo to usunąć jeśli nie zapisuje pojedynczych userów
       return addUserToRedis(user);


    }



    private UserDataDTO deactivatingUser(Long userId) {
        //dodając ten sam key updatuje obecny zapis



        /**   todo   UserDataDTO  user = getUserById(userId);
         *      /\ pobiera pojedynczego, ale to jest praktycznie nieuzywane nie wiem usunac, czy poprawic ze dodając dodawanie/usuwanie razem z modyfikacją list?
        **/

        UserDataDTO  user = getUsers(true,false).stream().filter(u->u.getUserId().equals(userId)).findFirst().orElse(null);

        if(user==null)//nie ma tego usera dodanego do graczy w redis  -> może był usunięty w sql ale nie w redis? np z poziomy mssql?
        {
                //todo nie wiem co :P
             Optional<User> userInDB=   UserDAO.findById(userId);
             if(userInDB.isEmpty())//nie ma też w bd
             {
                 return null;
             }
             else  // nie ma w redis jest w db
             {

                 /**   todo  addUserToRedis(user.getUserId());
                  *      /\ to jak wcześniej. Zamiast dodawać encję usera jest dodaany do list
                  **/
                User changedUser= userInDB.get();
                     changedUser   .setRole(Role.DEACTIVATED_USER);
                  addUserToUserLists(changedUser);
                 user=UserAdapter.convertUserToUserDataDTO(changedUser);
              //  addUserToRedis(user.getUserId());


             }
        }
        else
        {
            user.setRole(Role.DEACTIVATED_USER);
            addUserToUserLists(user);

            /*List<UserDataDTO> listOfDeletedWithPause=  this.getUsers(false,true);//.stream().filter(user->!user.getUserId().equals(userId)).collect(Collectors.toList());
            listOfDeletedWithPause.add(user);
            this.addAllUsers(listOfDeletedWithPause,UserDataDTO.class,false,true);

            List<UserDataDTO> listOfDeletedWithoutPause=  this.getUsers(false,false);//.stream().filter(user->!user.getUserId().equals(userId)).collect(Collectors.toList());
            listOfDeletedWithoutPause.add(user);
            this.addAllUsers(listOfDeletedWithoutPause,UserDataDTO.class,false,false);*/


        }








        return user;
    }
}