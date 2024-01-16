package LKManager.LK.Comparators;

import LKManager.LK.PlayerSummary;

import java.util.Comparator;

public class GraczPodsumowanieComparatorGoalLost implements Comparator<PlayerSummary> {
    @Override
    public int compare(PlayerSummary o1, PlayerSummary o2) {
     /*   if(o1.getGoleStracone()=="")o1.setGoleStracone("0");
        if(o2.getGoleStracone()=="")o2.setGoleStracone("0");*/
        return Integer.compare(o2.getGoleStracone(),o1.getGoleStracone());
    }
}
