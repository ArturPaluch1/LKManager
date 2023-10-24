package LKManager.LK.Comparators;

import LKManager.LK.GraczPodsumowanie;
import LKManager.LK.Terminarz;

import java.util.Comparator;

public class scheduleIdComparator implements Comparator<Terminarz> {

    @Override
    public int compare(Terminarz o1, Terminarz o2) {

        return Long.compare(o2.getId(),o1.getId());
    }
}
