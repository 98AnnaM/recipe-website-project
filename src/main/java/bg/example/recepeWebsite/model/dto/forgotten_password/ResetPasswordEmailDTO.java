package bg.example.recepeWebsite.model.dto.forgotten_password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ResetPasswordEmailDTO {
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    private String email;

    public ResetPasswordEmailDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
