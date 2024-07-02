package LKManager.services;

import LKManager.model.RecordsAndDTO.RoundDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoundService {


  List<RoundDTO> getRoundsByDate(LocalDate roundDate);
}
