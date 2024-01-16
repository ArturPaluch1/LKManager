package LKManager.LK.Comparators;

import LKManager.LK.PlayerSummary;

import java.util.Comparator;

public class GraczPodsumowanieComparatorGoalDifference implements Comparator<PlayerSummary> {
    @Override
    public int compare(PlayerSummary o1, PlayerSummary o2) {

        return Integer.compare(o2.getRoznica(),o1.getRoznica());
    }
}
