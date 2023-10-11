package LKManager.DAO;

import LKManager.LK.Runda;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RundaDAO {

//    Runda save(Runda runda);

  void saveRound(Runda round);

  //  void saveRound(Runda runda, int runda);
   //  List<Terminarz> findAll();
  //  void saveResults(Integer runda, Terminarz terminarz1, MatchService matchService, MZUserService mzUserService) throws DatatypeConfigurationException, JAXBException, IOException, ParserConfigurationException, SAXException;
  Runda findByTerminarzIdAndRundaId(long terminarzId, int rundaId);

  List<Runda> findAllByTerminarzId(long id);

    List<Runda> findAllByTerminarzName(String wybranyTerminarz);

  Runda findRound(String wybranyTerminarz, Integer numerRundy);

  Runda findRoundWitchMatches(String wybranyTerminarz, Integer numerRundy);

  //  Terminarz findByTerminarzName(String name);

  //  Runda findLastById();
}
