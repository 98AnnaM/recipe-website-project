package bg.example.recepeWebsite.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleEntity> roles;
    @OneToMany(mappedBy = "author")
    private List<RecipeEntity> addedRecipes;
    @Column
    private boolean accountVerified;

    @ManyToMany()
    @JoinTable(
            name = "user_favorite_recipes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<RecipeEntity> favorites;

    @OneToMany(mappedBy = "author")
    private List<PictureEntity> addedPictures;

    public List<RecipeEntity> getAddedRecipes() {
        return addedRecipes;
    }

    public UserEntity setAddedRecipes(List<RecipeEntity> addedRecipes) {
        this.addedRecipes = addedRecipes;
        return this;
    }

    public List<RecipeEntity> getFavorites() {
        return favorites;
    }

    public UserEntity setFavorites(List<RecipeEntity> favorites) {
        this.favorites = favorites;
        return this;
    }

    public List<PictureEntity> getAddedPictures() {
        return addedPictures;
    }

    public UserEntity setAddedPictures(List<PictureEntity> addedPictures) {
        this.addedPictures = addedPictures;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public boolean isAccountVerified() {
        return accountVerified;
    }

    public UserEntity setAccountVerified(boolean accountVerified) {
        this.accountVerified = accountVerified;
        return this;
    }
}
