package LKManager.LK.Comparators;

import LKManager.LK.GraczPodsumowanie;

import java.util.Comparator;

public class GraczPodsumowanieComparatorGoalDifference implements Comparator<GraczPodsumowanie> {
    @Override
    public int compare(GraczPodsumowanie o1, GraczPodsumowanie o2) {

        return Integer.compare(o2.getRoznica(),o1.getRoznica());
    }
}
