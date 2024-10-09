package LKManager.model.account;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
@Data
public class RoleGrantedAuthority implements GrantedAuthority {
    private final Role role;




    @Override
    public String getAuthority() {
        return "ROLE_" + role.name();
    }
}
