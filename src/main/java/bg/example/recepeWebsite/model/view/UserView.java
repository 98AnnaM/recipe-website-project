package bg.example.recepeWebsite.model.view;

import bg.example.recepeWebsite.model.entity.RoleEntity;

import java.util.Set;

public class UserView {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleEntity> roles;

    public String getUsername() {
        return username;
    }

    public UserView setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserView setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public UserView setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserView setId(Long id) {
        this.id = id;
        return this;
    }
}
