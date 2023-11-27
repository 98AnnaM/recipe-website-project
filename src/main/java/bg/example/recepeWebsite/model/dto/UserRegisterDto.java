package bg.example.recepeWebsite.model.dto;

import bg.example.recepeWebsite.model.validation.FieldMatch;
import bg.example.recepeWebsite.model.validation.UniqueEmail;
import bg.example.recepeWebsite.model.validation.UniqueUsername;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords do not match.")
public class UserRegisterDto {

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Must be valid email address.")
    @UniqueEmail(message = "This email is already in use.")
    private String email;

    @NotEmpty(message = "Username cannot be empty.")
    @UniqueUsername(message = "This username is already in use.")
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "First name cannot be empty.")
    @Size(min = 3, max = 20, message = "First name length must be between 3 and 20 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(min = 3, max = 20, message = "Last name length must be between 3 and 20 characters!")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRegisterDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterDto setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
