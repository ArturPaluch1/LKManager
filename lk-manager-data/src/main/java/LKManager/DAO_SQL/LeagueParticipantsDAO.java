package LKManager.DAO_SQL;

import LKManager.model.LeagueParticipants;
import LKManager.model.UserMZ.LeagueParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface LeagueParticipantsDAO extends JpaRepository<LeagueParticipants,Long> {


    @Query("delete from LeagueParticipants where participantID=:participantID")
    void deleteById(@Param("participantID") Long participantID);

    @Transactional
    @Modifying
    @Query("delete from LeagueParticipants lp where lp.user.id in " +
            "(select u.id from User u where u.leagueParticipation =:leagueParticipation)")
    void deleteByLeagueParticipation(@Param("leagueParticipation") LeagueParticipation leagueParticipation);


/*   @Modifying
    @Query("INSERT INTO LeagueParticipants (participantID, user_id) VALUES (:participantID, :userId)" )
    void addUser(@Param("participantID") Long participantID, @Param("userId") Long userId);*/


    /*  int addLeagueParticipant(@Param("userId") Long userId);*/
}
