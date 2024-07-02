package LKManager.services;

import LKManager.DAO_SQL.RoundDAO;
import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.model.Round;
import LKManager.services.Adapters.RoundAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundServiceImpl implements RoundService{

    private final RoundDAO roundDAO;

    public RoundServiceImpl(RoundDAO roundDAO) {
        this.roundDAO = roundDAO;
    }

    @Override
    @Transactional
    public List<RoundDTO> getRoundsByDate(LocalDate roundDate) {


            List<Round> roundsInDb=   roundDAO.findRoundsByDate(roundDate);
            if(roundsInDb.size()!=0)
            {
                List<RoundDTO> rounds=   roundsInDb.stream().map(r-> RoundAdapter.adapt(r)).collect(Collectors.toList());
                return rounds;
            }

        return null;
    }


}
