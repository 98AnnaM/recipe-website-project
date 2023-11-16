package bg.example.recepeWebsite.util;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.RoleEntity;
import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.RoleRepository;
import bg.example.recepeWebsite.repository.TypeRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDataUtils {

  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private RecipeRepository recipeRepository;
  private TypeRepository typeRepository;

  public TestDataUtils(UserRepository userRepository,
                       RoleRepository roleRepository,
                       RecipeRepository recipeRepository,
                       TypeRepository typeRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.recipeRepository = recipeRepository;
    this.typeRepository = typeRepository;
  }

  private void initRoles() {
    if (roleRepository.count() == 0) {
      RoleEntity adminRole = new RoleEntity().setRole(RoleNameEnum.ADMIN);
      RoleEntity userRole = new RoleEntity().setRole(RoleNameEnum.USER);

      roleRepository.save(adminRole);
      roleRepository.save(userRole);
    }
  }

    public UserEntity createTestAdmin(String username) {

    initRoles();

    UserEntity admin = new UserEntity().
            setUsername(username).
            setEmail("admin@example.com").
            setFirstName("Admin").
            setLastName("Adminov").
            setRoles(roleRepository.findAll())
            .setPassword("12345");

    return userRepository.save(admin);
  }

  public UserEntity createTestUser(String username) {

    initRoles();

    UserEntity user = new UserEntity().
            setUsername(username).
            setEmail("user@example.com").
            setFirstName("User").
            setLastName("Userov").
            setRoles(roleRepository.
            findAll().stream().
            filter(r -> r.getRole() != RoleNameEnum.ADMIN).
            toList())
            .setPassword("12345");

    return userRepository.save(user);
  }

  public RecipeEntity createTestRecipe(UserEntity author,
                                       List<TypeEntity> types) {
    var recipeEntity = new RecipeEntity().
            setName("Testing recipe")
            .setProducts("Testing products")
            .setDescription("Testing description")
            .setLevel(LevelEnum.EASY)
            .setAuthor(author)
            .setCategory(CategoryNameEnum.VEGAN)
            .setTypes(types)
            .setTimeNeeded(30)
            .setPortions(4);

    return recipeRepository.save(recipeEntity);
  }

  public TypeEntity createTestType() {
    var typeEntity = new TypeEntity().
        setName(TypeNameEnum.BREAKFAST);

    return typeRepository.save(typeEntity);
  }

  public void cleanUpDatabase() {
    recipeRepository.deleteAll();
    userRepository.deleteAll();
    roleRepository.deleteAll();
    typeRepository.deleteAll();
  }

  public RecipeRepository getRecipeRepository() {
    return recipeRepository;
  }
}
