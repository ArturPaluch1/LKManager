package LKManager.services;

import LKManager.DAO_SQL.UserDAO;
import LKManager.model.CustomUserDetails;
import LKManager.model.account.User;
import LKManager.model.account.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


//todo to nie potrzebne
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO UserDAO;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);

        User user = UserDAO.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        boolean accountNonBlocked=user.getRole()!= Role.DEACTIVATED_USER;
        // Convert Role to a GrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());


        return new CustomUserDetails(
                user.getId(),
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
