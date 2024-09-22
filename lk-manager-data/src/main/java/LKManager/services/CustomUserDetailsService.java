package LKManager.services;

import LKManager.DAO_SQL.UserDAO;
import LKManager.model.CustomUserDetails;
import LKManager.model.UserMZ.Role;
import LKManager.model.UserMZ.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);

        UserData user = userDAO.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        boolean accountNonBlocked=user.getRole()!= Role.DEACTIVATED_USER;
        // Convert Role to a GrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());


        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),

                user.getPassword(),
                Collections.singleton(authority),
                accountNonBlocked

        );
     /*   return new org.springframework.security.core.userdetails.User(
                user.getUsername(),

                user.getPassword(),
                true,
                true,
                true,
                accountNonBlocked,
                Collections.singleton(authority)

        );*/
    }
}
