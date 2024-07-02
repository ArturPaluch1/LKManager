package LKManager.services.RedisService;

import LKManager.DAO_SQL.UserDAO;
import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Adapters.UserAdapter;
import LKManager.services.GsonService;
import lombok.Data;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserDAO userDAO;
    public RedisUserService(RedisTemplate<String, Object> redisTemplate, GsonService gsonService, RedisTemplate<String, String> redisListTemplate, UserDAO userDAO) {
        this.redisTemplate = redisTemplate;
        this.gsonService = gsonService;
        this.redisListTemplate = redisListTemplate;
        this.userDAO = userDAO;
        this.valueOperations=	this.getRedisTemplate().opsForValue();
        this.listOperations = this.getRedisListTemplate().opsForList();

    }




    public <T> List<UserDataDTO> addAllUsers(List<T> users, Class<T> userType, boolean deleted,boolean withPause)
    {

        List<String> usersJson;
        List<UserDataDTO> usersDTO;
        if(userType.equals(UserData.class))
        {

            usersDTO=users.stream().map(user->UserAdapter.convertUserDataToUserDataDTO((UserData)user )).collect(Collectors.toList());

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
            if(deleted==true)
            {
                if (withPause==true)
                {
                  //  valueOperations.set("usersDeletedWithPause",usersJson);
                    redisTemplate.delete("usersDeletedWithPause");
                    listOperations.leftPushAll("usersDeletedWithPause",usersJson);
                    redisTemplate.expire("usersDeletedWithPause",2, TimeUnit.HOURS);
                    return this.getUsers(true,true);
                }
                else
                {
                //    valueOperations.set("usersDeletedWithoutPause",usersJson);

                    redisTemplate.delete("usersDeletedWithoutPause");
                    listOperations.leftPushAll("usersDeletedWithoutPause",usersJson);
                    redisTemplate.expire("usersDeletedWithoutPause",2, TimeUnit.HOURS);
                    return this.getUsers(true,false);
                }
            }
            else {
                if (withPause == true) {
                    redisTemplate.delete("usersNotDeletedWithPause");
                    listOperations.leftPushAll("usersNotDeletedWithPause",usersJson);
                    redisTemplate.expire("usersNotDeletedWithPause",30, TimeUnit.DAYS);
                 //   valueOperations.set("usersNotDeletedWithPause",usersJson);
                    return this.getUsers(false,true);

                } else {
                    redisTemplate.delete("usersNotDeletedWithoutPause");
                    listOperations.leftPushAll("usersNotDeletedWithoutPause",usersJson);
                    redisTemplate.expire("usersNotDeletedWithoutPause",30, TimeUnit.DAYS);
                  //  valueOperations.set("usersNotDeletedWithoutPause",usersJson);
                    return this.getUsers(false,false);


                }
            }


        }
        else {return null;}


    }

    public List<UserDataDTO> getUsers(boolean deleted,boolean withPause)
    {
        if(deleted==true)
        {
            if (withPause==true)
            {
                List<String> jsonStringUsers = listOperations.range("usersDeletedWithPause",0,-1);
                //todo tu może być błąd?

                if(!jsonStringUsers.isEmpty())
                {
                    redisTemplate.expire("usersDeletedWithPause",30, TimeUnit.DAYS);
                    //   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);// gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());
                   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers,UserDataDTO.class);


                    System.out.println(listOperations.range("usersDeletedWithPause",0,-1));
                    return usersFromRedis;
                 //   return jsonStringUsers;
                }
                else return null;
            }
            else
            {
           //     List<String> jsonStringUsers = listOperations.range("usersDeletedWithoutPause",0,-1);
                List<String> jsonStringUsers = listOperations.range("usersDeletedWithoutPause",0,-1);

                if(!jsonStringUsers.isEmpty())
                {
                    redisTemplate.expire("usersDeletedWithoutPause",30, TimeUnit.DAYS);
                    //   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);// gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());
                    List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers,UserDataDTO.class);


                    return usersFromRedis;
                  //  return jsonStringUsers;
                }
                else return null;
            }
        }
        else {
            if (withPause == true) {


            //    var a =



             //   String jsonStringUsers = gsonService.objectToJson( valueOperations.get("usersNotDeletedWithPause"));
          //      List<String> jsonStringUsers =  listOperations.range("usersNotDeletedWithPause",0,-1);
                List<String> jsonStringUsers =  listOperations.range("usersNotDeletedWithPause",0,-1);

                if(!jsonStringUsers.isEmpty())
                {
                    redisTemplate.expire("usersNotDeletedWithPause",30, TimeUnit.DAYS);
                 //   List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);// gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());
                    List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers,UserDataDTO.class);


                    System.out.println(listOperations.range("usersNotDeletedWithPause",0,-1));

                    return usersFromRedis;
               //     return jsonStringUsers;
                }
                else return null;



            } else {
          //      List<String> jsonStringUsers =  listOperations.range("usersNotDeletedWithoutPause",0,-1);
                List<String> jsonStringUsers =  listOperations.range("usersNotDeletedWithoutPause",0,-1);
                  if(!jsonStringUsers.isEmpty()) {
                      redisTemplate.expire("usersNotDeletedWithoutPause",30, TimeUnit.DAYS);
                    List<UserDataDTO> usersFromRedis = gsonService.jsonToList(jsonStringUsers, UserDataDTO.class);//gson.fromJson(jsonStringUsers, new TypeToken<List<UserDataDTO>>(){}.getType());


                    System.out.println(listOperations.range("usersNotDeletedWithoutPause",0,-1));
                    return usersFromRedis;
               //       return jsonStringUsers;
                }
                else return null;

            }
        }
    }





//todo to chjyba usunąć, bo lepiej chyba pobrać z redis całą listę i wyszukać niż zapisywać dodatkowo w redis każdego usera oddzielnie
    public UserDataDTO addUserToRedis(Object userData )
    {
        String userJson;
        if (userData instanceof UserData) {

        userJson  = mapUserDataToJsonString(UserAdapter.convertUserDataToUserDataDTO((UserData) userData));
     valueOperations.set("user:" + ((UserData)userData).getUserId(), userJson);
            redisTemplate.expire("user:" + ((UserData)userData).getUserId(),1, TimeUnit.DAYS);

            System.out.println("addeduser to redis");
            this.addUserToUserLists((UserData) userData);


            return this.getUserById(((UserData) userData).getUserId());
        } else if (userData instanceof UserDataDTO) {
            userJson  = mapUserDataToJsonString( userData);
            valueOperations.set("user:" + ((UserDataDTO)userData).getUserId(), userJson);
            redisTemplate.expire("user:" + ((UserDataDTO)userData).getUserId(),1, TimeUnit.DAYS);
            System.out.println("addeduser to redis");
            this.addUserToUserLists(userData);


            return this.getUserById(((UserDataDTO) userData).getUserId());
        } else {
return null;
        }









    }

    private UserDataDTO getUserById(Integer userId) {
      String jsonUser=  (String) valueOperations.get("user:"+userId);
        redisTemplate.expire("user:"+userId,2, TimeUnit.DAYS);
        return gsonService.jsonToObject(jsonUser,UserDataDTO.class);
    }

    public UserData DeleteUserFromRedis()
    {
        return null;
    }


    private List<String> mapListUserDataToJsonString(List<UserData> users) {

        List<UserDataDTO> usersDTO = users.stream().map(UserAdapter::convertUserDataToUserDataDTO).collect(Collectors.toList());


        return gsonService.listToJson(usersDTO);
    }
    private String mapUserDataToJsonString(Object user) {
        if (user instanceof UserData) {


            return gsonService.objectToJson(UserAdapter.convertUserDataToUserDataDTO((UserData) user));
        } else if (user instanceof UserDataDTO) {
            return gsonService.objectToJson(user);
        } else {
            return null;
        }


    }

    public UserDataDTO findUserById(Integer id) {

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
if(userToAdd instanceof UserData)
{
    UserData userDataToadd=(UserData) userToAdd;
    String usersJson;

    if(userDataToadd.getDeleted()==true)
    {

        List<UserDataDTO> usersDeletedWithPause=this.getUsers(true,true);
        usersDeletedWithPause.add(UserAdapter.convertUserDataToUserDataDTO(userDataToadd));
        this.addAllUsers(usersDeletedWithPause,UserDataDTO.class,true,true);

    //    usersJson = gsonService.listToJson(usersDeletedWithPause);
  //      valueOperations.set("usersDeletedWithPause",usersJson);

        List<UserDataDTO>     usersDeletedWithoutPause   =    this.getUsers(true,false);
        usersDeletedWithoutPause.add(UserAdapter.convertUserDataToUserDataDTO(userDataToadd));
        this.addAllUsers(usersDeletedWithoutPause,UserDataDTO.class,true,false);

   //     usersJson = gsonService.listToJson(usersDeletedWithoutPause);
    //    valueOperations.set("usersDeletedWithoutPause",usersJson);
        return getUsers(true,true);
    }
    else {
        List<UserDataDTO>    usersNotDeletedWithPause=   this.getUsers(false,true);
        usersNotDeletedWithPause.add(UserAdapter.convertUserDataToUserDataDTO(userDataToadd));
   // this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,true);
   //     usersJson = gsonService.listToJson(usersNotDeletedWithPause);
//valueOperations.set("usersNotDeletedWithPause",usersJson);
        this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,true);

        List<UserDataDTO>    usersNotDeletedWithoutPause  =  this.getUsers(false,false);
        usersNotDeletedWithoutPause.add(UserAdapter.convertUserDataToUserDataDTO(userDataToadd));

      //  this.addAllUsers(usersNotDeletedWithoutPause,UserDataDTO.class,false,false);
   //     usersJson = gsonService.listToJson(usersNotDeletedWithoutPause);
    //    valueOperations.set("usersNotDeletedWithoutPause",usersJson);
        this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,false);
        return getUsers(false,true);
    }


} else if (userToAdd instanceof  UserDataDTO) {
    UserDataDTO userDataDTOtoAdd=(UserDataDTO) userToAdd;

    if(userDataDTOtoAdd.isDeleted()==true)
    {
        System.out.println("tu");
        List<UserDataDTO> usersDeletedWithPause=this.getUsers(true,true);
        usersDeletedWithPause.add(userDataDTOtoAdd);
        this.addAllUsers(usersDeletedWithPause,UserDataDTO.class,true,true);

        List<UserDataDTO>     usersDeletedWithoutPause   =    this.getUsers(true,false);
        usersDeletedWithoutPause.add(userDataDTOtoAdd);
        this.addAllUsers(usersDeletedWithoutPause,UserDataDTO.class,true,false);

        return getUsers(true,true);
    }
    else {
        List<UserDataDTO>    usersNotDeletedWithPause=   this.getUsers(false,true);
        usersNotDeletedWithPause.add(userDataDTOtoAdd);
        this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,true);


        List<UserDataDTO>    usersNotDeletedWithoutPause  =  this.getUsers(false,false);
        usersNotDeletedWithoutPause.add(userDataDTOtoAdd);
        System.out.println("tam");
        this.addAllUsers(usersNotDeletedWithoutPause,UserDataDTO.class,false,false);

        return getUsers(false,true);
    }


}
else return null;





    }

    public UserDataDTO updateUserInRedis(Object userToEdit) {
        if (userToEdit instanceof UserData) {


         //   UserDataDTO userInRedis=  this.findUserById(((UserData)userToEdit).getUserId());
            UserDataDTO   userInRedis=this.findUserById((((UserData) userToEdit).getUserId()));
            if(userInRedis==null)
            {
                userInRedis=this.addUserToRedis((UserData)userToEdit);
            }
               // UserDataDTO userToEditDTO=    UserAdapter.adapt((UserData) userToEdit);
                userInRedis.setDeleted(((UserData) userToEdit).getDeleted());
                userInRedis.setTeamName(((UserData) userToEdit).getTeamlist().get(0).getTeamName());

            this.editUserInUserLists(userToEdit);
          return   this.addUserToRedis(userInRedis);

        } else if (userToEdit instanceof UserDataDTO) {

            UserDataDTO   userInRedis=this.findUserById((((UserDataDTO) userToEdit).getUserId()));
            if(userInRedis==null)
            {
                userInRedis=this.addUserToRedis((UserData)userToEdit);
            }
            userInRedis.setDeleted(((UserDataDTO) userToEdit).isDeleted());
            userInRedis.setTeamName(((UserDataDTO) userToEdit).getTeamName());
            this.editUserInUserLists(userToEdit);
        return     this.addUserToRedis(userInRedis);

        } else {
            return null;
        }


    }

    public List<UserDataDTO> editUserInUserLists(Object editedUser) {
        if (editedUser instanceof UserData) {
            if(((UserData)editedUser).getDeleted()==true)
            {
                List<UserDataDTO> usersDeletedWithPause=this.getUsers(true,true);

                UserDataDTO  userInRedis= usersDeletedWithPause.get(((UserData) editedUser).getUserId());
                userInRedis.setDeleted(((UserData) editedUser).getDeleted());
                userInRedis.setTeamName(((UserData) editedUser).getTeamlist().get(0).getTeamName());
                this.addAllUsers(usersDeletedWithPause,UserDataDTO.class,true,true);

                List<UserDataDTO>     usersDeletedWithoutPause   =    this.getUsers(true,false);

                UserDataDTO  userInRedis1= usersDeletedWithoutPause.get(((UserData) editedUser).getUserId());
                userInRedis1.setDeleted(((UserData) editedUser).getDeleted());
                userInRedis1.setTeamName(((UserData) editedUser).getTeamlist().get(0).getTeamName());
                return        this.addAllUsers(usersDeletedWithoutPause,UserDataDTO.class,true,false);
            }
            else {
                List<UserDataDTO>    usersNotDeletedWithPause=   this.getUsers(false,true);

                UserDataDTO  userInRedis= usersNotDeletedWithPause.get(((UserData) editedUser).getUserId());
                userInRedis.setDeleted(((UserData) editedUser).getDeleted());
                userInRedis.setTeamName(((UserData) editedUser).getTeamlist().get(0).getTeamName());
                this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,true);

                List<UserDataDTO>    usersNotDeletedWithoutPause  =  this.getUsers(false,false);

                UserDataDTO  userInRedis1= usersNotDeletedWithoutPause.get(((UserData) editedUser).getUserId());
                userInRedis1.setDeleted(((UserData) editedUser).getDeleted());
                userInRedis1.setTeamName(((UserData) editedUser).getTeamlist().get(0).getTeamName());
                return     this.addAllUsers(usersNotDeletedWithoutPause,UserDataDTO.class,false,false);

            }


        } else if (editedUser instanceof UserDataDTO) {

            if(((UserDataDTO)editedUser).isDeleted()==true)
            {
                List<UserDataDTO> usersDeletedWithPause=this.getUsers(true,true);
                UserDataDTO  userInRedis= usersDeletedWithPause.get(((UserDataDTO) editedUser).getUserId());
                userInRedis.setDeleted(((UserDataDTO) editedUser).isDeleted());
                userInRedis.setTeamName(((UserDataDTO) editedUser).getTeamName());
                this.addAllUsers(usersDeletedWithPause,UserDataDTO.class,true,true);

                List<UserDataDTO>     usersDeletedWithoutPause   =    this.getUsers(true,false);
                UserDataDTO  userInRedis1= usersDeletedWithoutPause.get(((UserDataDTO) editedUser).getUserId());
                userInRedis1.setDeleted(((UserDataDTO) editedUser).isDeleted());
                userInRedis1.setTeamName(((UserDataDTO) editedUser).getTeamName());
                return    this.addAllUsers(usersDeletedWithoutPause,UserDataDTO.class,true,false);

            }
            else {
                List<UserDataDTO>    usersNotDeletedWithPause=   this.getUsers(false,true);
                UserDataDTO  userInRedis= usersNotDeletedWithPause.get(((UserDataDTO) editedUser).getUserId());
                userInRedis.setDeleted(((UserDataDTO) editedUser).isDeleted());
                userInRedis.setTeamName(((UserDataDTO) editedUser).getTeamName());
                this.addAllUsers(usersNotDeletedWithPause,UserDataDTO.class,false,true);

                List<UserDataDTO>    usersNotDeletedWithoutPause  =  this.getUsers(false,false);
                UserDataDTO  userInRedis1= usersNotDeletedWithoutPause.get(((UserDataDTO) editedUser).getUserId());
                userInRedis1.setDeleted(((UserDataDTO) editedUser).isDeleted());
                userInRedis1.setTeamName(((UserDataDTO) editedUser).getTeamName());
                return   this.addAllUsers(usersNotDeletedWithoutPause,UserDataDTO.class,false,false);

            }

        } else {
            return null;
        }




    }

    public UserDataDTO deleteUser(Integer userId) {


//todo dodać aktualizowanie/dodawanie do listy usuniętych jakby trzeba było dodawać. Dodać też




        UserDataDTO user = deletingUser(userId);
        if (user == null) return null;


        else
        {
            List<UserDataDTO> listOfNotDeletedWithPause=  this.getUsers(false,true).stream().filter(u->!u.getUserId().equals(userId)).collect(Collectors.toList());
            this.addAllUsers(listOfNotDeletedWithPause,UserDataDTO.class,false,true);

            List<UserDataDTO> listOfNotDeletedWithoutPause=  this.getUsers(false,false).stream().filter(u->!u.getUserId().equals(userId)).collect(Collectors.toList());
            this.addAllUsers(listOfNotDeletedWithoutPause,UserDataDTO.class,false,false);

        }



       return addUserToRedis(user);


    }



    private UserDataDTO deletingUser(Integer userId) {
        //dodając ten sam key updatuje obecny zapis
        UserDataDTO  user = getUserById(userId);

        if(user==null)//nie ma tego usera dodanego do graczy w redis
        {
                //todo nie wiem co :P
             Optional<UserData> userInDB=   userDAO.findById(userId);
             if(userInDB.isEmpty())//nie ma w bd
             {
                 return null;
             }
             else
             {

                addUserToRedis(user.getUserId());


             }
        }
        else
        {
            user.setDeleted(true);

        }



      List<UserDataDTO> listOfDeletedWithPause=  this.getUsers(true,true);//.stream().filter(user->!user.getUserId().equals(userId)).collect(Collectors.toList());
       listOfDeletedWithPause.add(user);
        this.addAllUsers(listOfDeletedWithPause,UserDataDTO.class,true,true);

  List<UserDataDTO> listOfDeletedWithoutPause=  this.getUsers(true,false);//.stream().filter(user->!user.getUserId().equals(userId)).collect(Collectors.toList());
        listOfDeletedWithPause.add(user);
        this.addAllUsers(listOfDeletedWithoutPause,UserDataDTO.class,true,false);




        return user;
    }
}