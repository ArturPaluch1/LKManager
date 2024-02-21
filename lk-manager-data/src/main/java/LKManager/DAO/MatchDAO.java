package LKManager.DAO;

import LKManager.model.MatchesMz.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchDAO extends JpaRepository<Match,Long> , CustomMatchDAO {




}
