package LKManager.DAO;

import LKManager.LK.Terminarz;

public interface TerminarzDAO {

    void save(Terminarz terminarz);

    void saveRound(Terminarz terminarz, int runda);
}
