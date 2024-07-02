package LKManager.DAO_SQL;

import LKManager.DAO_Redis.CustomUserDAO_Redis;
import LKManager.model.UserMZ.UserData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

//@Repository
@RedisHash
public interface UserDAO extends JpaRepository<UserData, Integer>, CustomUserDAO, CustomUserDAO_Redis {


    @Query("select u from UserData u  where u.deleted='false' and u.username!='pauza'")
    List<UserData> findUsers_NotDeletedWithoutPause();
    @Query("select u from UserData u  where u.deleted='false' ")
   List<UserData> findUsers_NotDeletedWithPause();

    @Query("select u from UserData u where u.username=:userName")
    UserData findByName(@Param("userName") String userName);

    @Modifying
    @Query("update  UserData  set deleted = true where userId=:id")
    void deleteUserById(@Param("id") Integer id);
    @Query("select u from UserData u  where u.deleted='true' ")
    List<UserData> findUsers_DeletedWithPause();
    @Query("select u from UserData u  where u.deleted='true' and u.username!='pauza'")
    List<UserData> findUsers_DeletedWithoutPause();



    //  UserData save(UserData user);

    //   UserData save(UserData user);


}
