package LKManager.Security;

import LKManager.model.account.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserAuthenticationDetailsService {

 public   User getCustomUserDetails()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication.getPrincipal() instanceof String) && !authentication.getPrincipal().equals("anonymousUser"))

            return (User)authentication.getPrincipal();
      else return null;
    }
public void updateAuthentication(User changedUser)
{

    Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

    Authentication newAuth = new UsernamePasswordAuthenticationToken(changedUser, null,authorities );
    SecurityContextHolder.getContext().setAuthentication(newAuth);
}



}
