package LKManager.LK.Comparators;

import LKManager.LK.PlayerSummary;

import java.util.Comparator;

public class GraczPodsumowanieComparatorPoints implements Comparator<PlayerSummary> {
    @Override
    public int compare(PlayerSummary o1, PlayerSummary o2) {
   /*     if(o1.getSumaPunktow()=="")o1.setSumaPunktow("0");
        if(o2.getSumaPunktow()=="")o2.setSumaPunktow("0");*/
        return Integer.compare(o2.getSumaPunktow(),o1.getSumaPunktow());
    }
}
