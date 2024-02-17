package LKManager.DAO;

import LKManager.LK.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundDAO extends JpaRepository<Round,Long> ,CustomRoundDAO{

}
