package LKManager.services.Comparators;

import LKManager.model.RecordsAndDTO.RoundDTO;

import java.util.Comparator;

public class RoundDTOByDateComparator implements Comparator<RoundDTO> {

    @Override
    public int compare(RoundDTO o1, RoundDTO o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
