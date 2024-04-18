package LKManager.services.Comparators;

import LKManager.LK.PlayerSummary;

import java.util.Comparator;

public class GraczPodsumowanieComparatorGoalDifference implements Comparator<PlayerSummary> {
    @Override
    public int compare(PlayerSummary o1, PlayerSummary o2) {

        return Integer.compare(o2.getDifference(),o1.getDifference());
    }
}
