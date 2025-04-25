package LKManager.model.account;

import LKManager.model.UserMZ.LeagueParticipation;
import LKManager.model.UserMZ.MZUserData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="users", schema="lkm_dev")
@Getter
@Setter
@NoArgsConstructor
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedUserFilter", condition = "deleted = :isDeleted")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE user_id=?")


public class User implements UserDetails {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(
            name = "userid_seq",
            sequenceName = "lkm_dev.userid_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userid_seq")

    @Column(name = "user_id")
            private Long Id;
@Column(name = "username",unique = true)
private String username;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role",columnDefinition = "TINYINT")
    private Role role;//= false;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "reliability")
    private long reliability;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "MZuser_id")
    private MZUserData mzUser;

    @Column(name="league_participation", columnDefinition = "TINYINT")
    @Enumerated(EnumType.ORDINAL)
    private LeagueParticipation leagueParticipation;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new RoleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return role.equals(Role.DEACTIVATED_USER)?false:true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return role.equals(Role.DEACTIVATED_USER)?false:true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return password==null?false:true;
    }

    @Override
    public boolean isEnabled() {
        return role.equals(Role.DEACTIVATED_USER)?false:true;
    }
}
