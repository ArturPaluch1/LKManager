package LKManager.services;

import LKManager.LK.Table;
import LKManager.model.MatchesMz.Match;

import java.util.List;

public interface TableService {
     Table createTable(List<Match>matches);
}
