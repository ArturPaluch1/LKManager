package LKManager.LK.Comparators;

import LKManager.LK.GraczPodsumowanie;

import java.util.Comparator;

public class GraczPodsumowanieComparatorGoalScored implements Comparator<GraczPodsumowanie> {
    @Override
    public int compare(GraczPodsumowanie o1, GraczPodsumowanie o2) {
   /*     if(o1.getGoleStrzelone()=="")o1.setGoleStrzelone("0");
        if(o2.getGoleStrzelone()=="")o2.setGoleStrzelone("0");*/
        return Integer.compare(o2.getGoleStrzelone(),o1.getGoleStrzelone());
    }
}
