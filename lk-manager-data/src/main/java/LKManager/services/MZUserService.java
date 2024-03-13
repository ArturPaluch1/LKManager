package LKManager.services;

import LKManager.model.UserMZ.UserData;

public interface MZUserService extends CrudService<UserData,Long> {
     UserData findByUsername(String username) ;
}
