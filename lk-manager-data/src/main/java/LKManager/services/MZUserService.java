package LKManager.services;

import LKManager.model.UserMZ.MZUserData;

import java.net.MalformedURLException;

public interface MZUserService extends CrudService<MZUserData,Long> {
     MZUserData findByUsernameInManagerzone(String username) ;

     MZUserData getMZUserDataById(Long player) throws MalformedURLException;
}
