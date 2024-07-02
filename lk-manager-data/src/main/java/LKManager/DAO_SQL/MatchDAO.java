package LKManager.DAO_SQL;

import LKManager.model.MatchesMz.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

@Repository
@RedisHash
public interface MatchDAO extends JpaRepository<Match,Long> , CustomMatchDAO {




}
