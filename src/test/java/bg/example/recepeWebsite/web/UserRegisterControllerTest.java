package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.repository.SecureTokenRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import bg.example.recepeWebsite.service.UserService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.port}")
    private Integer mailPort;
    @Value("${mail.username}")
    private String mailUsername;
    @Value("${mail.password}")
    private String mailPassword;
    @Autowired
    private MockMvc mockMvc;
    private GreenMail greenMail;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecureTokenRepository secureTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        greenMail = new GreenMail(new ServerSetup(mailPort, mailHost, "smtp"));
        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        secureTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-register"));
    }

    private static final String TEST_USER_USERNAME = "user";
    private static final String TEST_USER_FIRSTNAME = "User";
    private static final String TEST_USER_LASTNAME = "Userov";
    private static final String TEST_USER_EMAIL = "user@example.com";
    private static final String TEST_USER_PASSWORD = "12345";
    private static final String BEST_COOK_EMAIL = "no-reply@bestCook.com";
    private static final String BASE_URL = "http://localhost:8080";

    @Test
    void testRegistrationWithSuccess() throws Exception {
        mockMvc.perform(post("/users/register").
                        param("username", TEST_USER_USERNAME).
                        param("firstName", TEST_USER_FIRSTNAME).
                        param("lastName", TEST_USER_LASTNAME).
                        param("email", TEST_USER_EMAIL).
                        param("password", TEST_USER_PASSWORD).
                        param("confirmPassword", TEST_USER_PASSWORD).
                        cookie(new Cookie("lang", Locale.ENGLISH.getLanguage())).
                        with(csrf()).
                        contentType(MediaType.APPLICATION_FORM_URLENCODED)
                ).
                andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register/sendNewVerificationMail?username=user"));

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assertions.assertEquals(1, receivedMessages.length);

        MimeMessage verificationEmail = receivedMessages[0];

        Assertions.assertTrue(GreenMailUtil
                .getBody(verificationEmail)
                .contains(TEST_USER_FIRSTNAME + " " + TEST_USER_LASTNAME));

        String verificationUrl = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/users/register/verify")
                .queryParam("token", secureTokenRepository.findById(1L).get().getToken()).toUriString();

        Assertions.assertTrue(GreenMailUtil
                .getBody(verificationEmail)
                .contains(verificationUrl));

        Assertions.assertEquals(1, verificationEmail.getAllRecipients().length);
        Assertions.assertEquals(TEST_USER_EMAIL, verificationEmail.getAllRecipients()[0].toString());
        Assertions.assertEquals(BEST_COOK_EMAIL, verificationEmail.getFrom()[0].toString());

        Assertions.assertEquals(1, userRepository.count());

        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByUsername(TEST_USER_USERNAME);

        Assertions.assertTrue(newlyCreatedUserOpt.isPresent());

        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();

        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
        Assertions.assertEquals(TEST_USER_FIRSTNAME, newlyCreatedUser.getFirstName());
        Assertions.assertEquals(TEST_USER_LASTNAME, newlyCreatedUser.getLastName());
        Assertions.assertTrue(passwordEncoder.matches(TEST_USER_PASSWORD, newlyCreatedUser.getPassword()));
        Assertions.assertFalse(newlyCreatedUser.isAccountVerified());
    }

    @Test
    void testRegistrationFail() throws Exception {
        mockMvc.perform(post("/users/register").
                        param("username", TEST_USER_USERNAME).
                        param("firstName", " ").
                        param("lastName", TEST_USER_LASTNAME).
                        param("email", TEST_USER_EMAIL).
                        param("password", TEST_USER_PASSWORD).
                        param("confirmPassword", TEST_USER_PASSWORD).
                        cookie(new Cookie("lang", Locale.ENGLISH.getLanguage())).
                        with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
    }
}