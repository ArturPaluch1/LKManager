package LKManager.model.RecordsAndDTO;

import LKManager.model.Schedule;
import LKManager.model.UserMZ.UserData;

import java.util.List;

public record CreateScheduleResult(Schedule schedule, List<UserData> playersNotInMZ ){}