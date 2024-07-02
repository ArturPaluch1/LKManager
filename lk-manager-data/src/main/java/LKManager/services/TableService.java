package LKManager.services;

import LKManager.model.RecordsAndDTO.ScheduleDTO;
import LKManager.model.Table;
import org.springframework.transaction.annotation.Transactional;

public interface TableService {
    //Table createTable(List<Match> matches);
      Table createTable(long chosenscheduleId);
    Table createTable(ScheduleDTO chosenschedule);

    @Transactional
    Table createSwissScheduleTable(ScheduleDTO chosenschedule);
}
