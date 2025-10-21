package LKManager.DAO_SQL;

import LKManager.DAO_Redis.CustomUserDAO_Redis;
import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
@RedisHash
@Repository
public interface UserDAO extends JpaRepository<User, Long>, CustomUserDAO, CustomUserDAO_Redis  {

    @Query(value = "SELECT DB_NAME() AS CurrentDatabase, SCHEMA_NAME() AS CurrentSchema", nativeQuery = true)
    List<Object[]> checkConnection();
    @Query("select u from User u  where u.role!=0 and u.username!='pauza'")
    List<User> findUsers_ActiveWithoutPause();
    @Query("select u from User u  where u.role!=0 ")
   List<User> findUsers_ActiveWithPause();

    @Query("select u from MZUserData u where u.username=:userName")
    MZUserData findMZUserByMZname(@Param("userName") String userName);
    @Query("select u from User u where u.username=:userName")
    User findUserByName(@Param("userName") String userName);
    @Modifying
    @Query("update  User  set role = 0 where Id=:id")
    void deactivateUserById(@Param("id") Long id);
    @Query("select u from User u  where u.role=0 ")
    List<User> findUsers_DeactivatedWithPause();
    @Query("select u from User u  where u.role=0 and u.username!='pauza'")
    List<User> findUsers_DeactivatedWithoutPause();


    @Query("select u from MZUserData u where u.MZuser_id=:userId")
    Optional<MZUserData> findMzUserById(@Param("userId") Long userId);

    @Query("select u from User u where u.Id=:id")
    Optional<User> getUserById(@Param("id")Long id);

    //  UserData save(UserData user);

    //   UserData save(UserData user);

    Page<User> findByLeagueParticipation(LeagueParticipation leagueParticipation, Pageable pageable);
 @Query("select u.email from User u where u.Id=:id")
    String getUserEmail(Long id);

    @Query("select u.email from User u where u.username=:username")
    Optional<String> getUserEmail(String username);
}
