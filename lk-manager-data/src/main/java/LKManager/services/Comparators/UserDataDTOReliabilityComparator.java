package LKManager.services.Comparators;

import LKManager.model.RecordsAndDTO.UserDataDTO;

import java.util.Comparator;

public class UserDataDTOReliabilityComparator implements Comparator<UserDataDTO> {
    @Override
    public int compare(UserDataDTO o1, UserDataDTO o2) {
   /*     if(o1.getSumaPunktow()=="")o1.setSumaPunktow("0");
        if(o2.getSumaPunktow()=="")o2.setSumaPunktow("0");*/
        return Long.compare(o2.getReliability(),o1.getReliability());
    }
}
