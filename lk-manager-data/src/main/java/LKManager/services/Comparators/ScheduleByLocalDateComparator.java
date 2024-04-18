package LKManager.services.Comparators;

import LKManager.model.Schedule;

import java.util.Comparator;

public class ScheduleByLocalDateComparator implements Comparator<Schedule> {

    @Override
    public int compare(Schedule o1, Schedule o2) {

        return o2.getRounds().get(0).getDate().compareTo(
                o1.getRounds().get(0).getDate()
        );
    }
}
