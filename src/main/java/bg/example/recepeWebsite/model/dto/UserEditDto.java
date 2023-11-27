package bg.example.recepeWebsite.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserEditDto {

    private Long id;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Must be valid email address.")
    private String email;

    @NotEmpty(message = "Username cannot be empty.")
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    private String username;

    @NotEmpty(message = "First name cannot be empty.")
    @Size(min = 2, max = 20, message = "First name length must be between 2 and 20 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(min = 2, max = 20, message = "Last name length must be between 2 and 20 characters!")
    private String lastName;

    public String getEmail() {
        return email;
    }

    public UserEditDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserEditDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEditDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEditDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserEditDto setId(Long id) {
        this.id = id;
        return this;
    }
}
