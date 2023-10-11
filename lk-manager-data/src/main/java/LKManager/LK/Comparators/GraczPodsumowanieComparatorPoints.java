package LKManager.LK.Comparators;

import LKManager.LK.GraczPodsumowanie;

import java.util.Comparator;

public class GraczPodsumowanieComparatorPoints implements Comparator<GraczPodsumowanie> {
    @Override
    public int compare(GraczPodsumowanie o1, GraczPodsumowanie o2) {
   /*     if(o1.getSumaPunktow()=="")o1.setSumaPunktow("0");
        if(o2.getSumaPunktow()=="")o2.setSumaPunktow("0");*/
        return Integer.compare(o2.getSumaPunktow(),o1.getSumaPunktow());
    }
}
