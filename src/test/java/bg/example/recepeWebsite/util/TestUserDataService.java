package bg.example.recepeWebsite.util;

import bg.example.recepeWebsite.model.user.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TestUserDataService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("admin")) {
            return new CustomUserDetails(1L,
                    "user",
                    "User Userov",
                    "12345",
                    List.of(new SimpleGrantedAuthority("ROLE_USER")),
                    true);
        }

        return new CustomUserDetails(1L,
                "admin",
                "Admin Adminov",
                "12345",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")),
                true);
    }
}
