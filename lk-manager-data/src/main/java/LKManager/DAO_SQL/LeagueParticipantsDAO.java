package LKManager.DAO_SQL;

import LKManager.model.LeagueParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LeagueParticipantsDAO extends JpaRepository<LeagueParticipants,Long> {

  /*  int addLeagueParticipant(@Param("userId") Long userId);*/
}
