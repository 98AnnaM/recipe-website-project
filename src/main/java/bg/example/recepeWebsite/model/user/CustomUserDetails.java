package bg.example.recepeWebsite.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String fullName;
    private final String password;
    private final Collection<GrantedAuthority> authorities;
    private final boolean enabled;

    public CustomUserDetails(Long id, String username, String fullName, String password, Collection<GrantedAuthority> authorities, boolean enabled) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
