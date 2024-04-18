package LKManager.services.Comparators;

import LKManager.model.Schedule;

import java.util.Comparator;

public class scheduleIdComparator implements Comparator<Schedule> {

    @Override
    public int compare(Schedule o1, Schedule o2) {

        return Long.compare(o2.getId(),o1.getId());
    }
}
