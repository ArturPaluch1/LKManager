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
public interface UserDAO extends JpaRepository<UserData, Long>, CustomUserDAO, CustomUserDAO_Redis {


    @Query("select u from UserData u  where u.role!=0 and u.username!='pauza'")
    List<UserData> findUsers_ActiveWithoutPause();
    @Query("select u from UserData u  where u.role!=0 ")
   List<UserData> findUsers_ActiveWithPause();

    @Query("select u from UserData u where u.username=:userName")
    UserData findByName(@Param("userName") String userName);

    @Modifying
    @Query("update  UserData  set role = 0 where userId=:id")
    void deactivateUserById(@Param("id") Long id);
    @Query("select u from UserData u  where u.role=0 ")
    List<UserData> findUsers_DeactivatedWithPause();
    @Query("select u from UserData u  where u.role=0 and u.username!='pauza'")
    List<UserData> findUsers_DeactivatedWithoutPause();



    //  UserData save(UserData user);

    //   UserData save(UserData user);


}
