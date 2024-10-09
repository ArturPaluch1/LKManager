package LKManager.model.RecordsAndDTO;

import LKManager.model.Schedule;
import LKManager.model.UserMZ.MZUserData;

import java.util.List;

public record CreateScheduleResult(Schedule schedule, List<MZUserData> playersNotInMZ ){}