package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.RoleEntity;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//NOTE: This is not annotated as @Service, because we will return it as a @Bean.
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Trying to load user by username: {}", username); // Add this line for logging

        return this.userRepository
                .findByUsername(username)
                .map(this::map)
                .orElseThrow(() -> {
                    logger.error("User with email {} not found.", username); // Add this line for logging
                    return new UsernameNotFoundException("User with username " + username + " not found.");
                });
    }

    private UserDetails map(UserEntity userEntity){
        logger.info("Mapping user details for user with email: {}", userEntity.getEmail()); // Add this line for logging

        return new CustomUserDetails(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstName() + " " + userEntity.getLastName(),
                userEntity.getPassword(),
                userEntity
                        .getRoles()
                        .stream()
                        .map(this::map)
                        .toList());
    }

    private GrantedAuthority map(RoleEntity userRole){
        logger.info("Mapping user role: {}", userRole.getRole()); // Add this line for logging

        return new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name());
    }
}
