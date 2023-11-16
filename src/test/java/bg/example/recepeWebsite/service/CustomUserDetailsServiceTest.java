package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.RoleEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    private UserEntity testUser;
    private RoleEntity adminRole, userRole;

    private CustomUserDetailsService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init() {

        //ARRANGE
        serviceToTest = new CustomUserDetailsService(mockUserRepository);

        adminRole = new RoleEntity();
        adminRole.setRole(RoleNameEnum.ADMIN);

        userRole = new RoleEntity();
        userRole.setRole(RoleNameEnum.USER);

        testUser = new UserEntity();
        testUser.setUsername("anna");
        testUser.setFirstName("Anna");
        testUser.setLastName("Mileva");
        testUser.setEmail("anna@gmail.com");
        testUser.setPassword("12345");
        testUser.setRoles(List.of(adminRole, userRole));
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("invalid_username")
        );
    }

    @Test
    void testUserFound() {

        // Arrange
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));

        // Act
        CustomUserDetails userDetails =(CustomUserDetails) serviceToTest.loadUserByUsername(testUser.getUsername());

        // Assert

        Assertions.assertEquals(userDetails.getUsername(), testUser.getUsername());
        Assertions.assertEquals(userDetails.getFullName(),testUser.getFirstName() + " " + testUser.getLastName());
        Assertions.assertEquals(userDetails.getId(), testUser.getId());
        Assertions.assertEquals(userDetails.getPassword(), testUser.getPassword());

        Assertions.assertEquals(2, userDetails.getAuthorities().size());

        String expectedRoles = "ROLE_ADMIN, ROLE_USER";
        String actualRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(", "));

        Assertions.assertEquals(expectedRoles, actualRoles);
    }

}