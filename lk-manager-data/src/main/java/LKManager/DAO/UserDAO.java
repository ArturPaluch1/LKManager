package LKManager.DAO;

import LKManager.model.UserMZ.UserData;
import LKManager.services.CrudService;

public interface UserDAO extends CrudService<UserData, Long> {

    UserData save(UserData user);


}