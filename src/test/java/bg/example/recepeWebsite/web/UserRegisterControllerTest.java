package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.repository.UserRepository;
import bg.example.recepeWebsite.service.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService mockEmailService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"));
    }

    private static final String TEST_USER_USERNAME = "anna";
    private static final String TEST_USER_FIRSTNAME = "Anna";
    private static final String TEST_USER_LASTNAME = "Mileva";
    private static final String TEST_USER_EMAIL = "anna@gmail.com";
    private static final String TEST_USER_PASSWORD = "12345";

//    @Test
//    void testRegistrationWithSuccess() throws Exception {
//        mockMvc.perform(post("/users/register").
//                        param("username", TEST_USER_USERNAME).
//                        param("firstName", TEST_USER_FIRSTNAME).
//                        param("lastName", TEST_USER_LASTNAME).
//                        param("email", TEST_USER_EMAIL).
//                        param("password", TEST_USER_PASSWORD).
//                        param("confirmPassword", TEST_USER_PASSWORD).
//                        cookie(new Cookie("lang", Locale.ENGLISH.getLanguage())).
//                        with(csrf()).
//                        contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                ).
//                andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));
//
//        verify(mockEmailService)
//                .sendRegistrationEmail(TEST_USER_EMAIL, TEST_USER_FIRSTNAME + " " + TEST_USER_LASTNAME, Locale.ENGLISH);
//
//        Assertions.assertEquals(1, userRepository.count());
//
//        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByUsername(TEST_USER_USERNAME);
//
//        Assertions.assertTrue(newlyCreatedUserOpt.isPresent());
//
//        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();
//
//        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
//        Assertions.assertEquals(TEST_USER_FIRSTNAME, newlyCreatedUser.getFirstName());
//        Assertions.assertEquals(TEST_USER_LASTNAME, newlyCreatedUser.getLastName());
//        Assertions.assertTrue(passwordEncoder.matches(TEST_USER_PASSWORD, newlyCreatedUser.getPassword()));
//    }
//
//    @Test
//    void testRegistrationFail() throws Exception {
//        mockMvc.perform(post("/users/register").
//                        param("username", TEST_USER_USERNAME).
//                        param("firstName", " ").
//                        param("lastName", TEST_USER_LASTNAME).
//                        param("email", TEST_USER_EMAIL).
//                        param("password", TEST_USER_PASSWORD).
//                        param("confirmPassword", TEST_USER_PASSWORD).
//                        cookie(new Cookie("lang", Locale.ENGLISH.getLanguage())).
//                        with(csrf())
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/users/register"));
//        verify(mockEmailService, never()).sendRegistrationEmail(TEST_USER_EMAIL, " " + TEST_USER_LASTNAME, Locale.GERMAN);
//    }
}