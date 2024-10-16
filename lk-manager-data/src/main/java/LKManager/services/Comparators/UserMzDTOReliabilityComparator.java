package LKManager.services.Comparators;

import LKManager.model.RecordsAndDTO.UserMzDTO;
import LKManager.model.UserMZ.MZUserData;
import LKManager.model.account.User;
import LKManager.services.UserService;

import java.util.Comparator;
import java.util.List;

public class UserMzDTOReliabilityComparator implements Comparator<MZUserData> {

    private final UserService userService;

    public UserMzDTOReliabilityComparator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int compare(MZUserData o1, MZUserData o2) {
        //todo do zrobienia
   /*     if(o1.getSumaPunktow()=="")o1.setSumaPunktow("0");
        if(o2.getSumaPunktow()=="")o2.setSumaPunktow("0");*/


     //   findPlayersWithMZuser()


     //   return Long.compare(o2.getReliability(),o1.getReliability());
        return 0;
    }

    List<User> findPlayersWithMZuser(UserMzDTO user)
    {
     //   userService.fin
        return null;
    }
}
