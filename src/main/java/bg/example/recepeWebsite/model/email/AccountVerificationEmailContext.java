package bg.example.recepeWebsite.model.email;

import bg.example.recepeWebsite.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;

public class AccountVerificationEmailContext extends AbstractEmailContext {
    private static final String BEST_COOK_EMAIL = "no-reply@bestCook.com";
    private static final String VERIFICATION_SUBJECT = "registration_subject";
    private static final String TEMPLATE_LOCATION = "email/validation";
    private static final String BASE_URL = "http://localhost:8080";

    private String token;

    public AccountVerificationEmailContext setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public <T> void initContext(T context) {
        //we can do any common configuration setup here
        // like setting up some base URL and context

        UserEntity user = (UserEntity) context;

        setFrom(BEST_COOK_EMAIL);
        setTo(user.getEmail());
        put("fullName", user.getFirstName() + " " + user.getLastName());
        put("verificationURL", buildVerificationUrl());
        setSubject(VERIFICATION_SUBJECT);
        setTemplateLocation(TEMPLATE_LOCATION);
    }

    public String buildVerificationUrl() {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/users/register/verify").queryParam("token", token).toUriString();
    }
}
