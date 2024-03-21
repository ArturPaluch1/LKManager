package LKManager.HardCodedCache_unused;

import LKManager.HardCodedCache_unused.Cache.MZCache;
import LKManager.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


    @AllArgsConstructor
    @Getter
    @Setter
    public
    class UpdateUsersCache extends MZCacheAction {
        @Autowired
        private MZCache mzCache;
        //	private final UserDAO userDAO;
        private final UserService userService;
        @Override @Transactional
        public MZCache update() {
            //dodawanie users do cache
            userService.findUsers_NotDeletedWithPause();
            try {
                userService.findUsers_NotDeletedWithPause();
                //	mzCache.setUsers(userDAO.findNotDeletedUsers());
            }

            catch (Exception e)
            {
                //		failedDatabaseOperationRepository.addFailedOperation(new GetUsersFailedDatabaseOperation(SQLOperation.AddUserDatabaseAccessFailureException));

            }

            finally {
                return mzCache;
            }
        }
    }

