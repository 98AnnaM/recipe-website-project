package bg.example.recepeWebsite.model.email;


import bg.example.recepeWebsite.model.entity.UserEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class ForgotPasswordEmailContext extends AbstractEmailContext {
    private static final String BEST_COOK_EMAIL = "no-reply@bestCook.com";
    private static final String RESET_SUBJECT = "reset_password_subject";
    private static final String TEMPLATE_LOCATION = "email/reset";

    private String baseUrl;

    private String token;

    public ForgotPasswordEmailContext setToken(String token) {
        this.token = token;
        return this;
    }

    public ForgotPasswordEmailContext setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public <T> void initContext(T context) {
        //we can do any common configuration setup here
        // like setting up some base URL and context

        UserEntity user = (UserEntity) context;

        setFrom(BEST_COOK_EMAIL);
        setTo(user.getEmail());
        put("username", user.getUsername());
        put("fullName", user.getFirstName() + " " + user.getLastName());
        put("verificationURL", buildVerificationUrl());
        setSubject(RESET_SUBJECT);
        setTemplateLocation(TEMPLATE_LOCATION);
    }

    private String buildVerificationUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/password/change").queryParam("token", token).toUriString();
    }
}
