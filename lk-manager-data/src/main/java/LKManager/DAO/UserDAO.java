package LKManager.DAO;

import LKManager.model.UserMZ.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@Repository

public interface UserDAO extends JpaRepository<UserData, Long>, CustomUserDAO  {


    @Query("select u from UserData u  where u.deleted='false' and u.username!='pauza'")
    List<UserData> findUsers_NotDeletedWithoutPause();
    @Query("select u from UserData u  where u.deleted='false' ")
   List<UserData> findUsers_NotDeletedWithPause();







 //   UserData save(UserData user);


}
