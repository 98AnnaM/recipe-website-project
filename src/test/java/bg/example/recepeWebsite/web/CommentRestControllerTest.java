package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.AddCommentDto;
import bg.example.recepeWebsite.model.entity.CommentEntity;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.RoleEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.RoleRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentRestControllerTest {

    private static final String COMMENT_1 = "This is comment one!";
    private static final String COMMENT_2 = "This is comment two!";
    private static final String COMMENT_3 = "This is comment three!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;
    private UserEntity admin;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        RoleEntity userRole = new RoleEntity();
        userRole.setRole(RoleNameEnum.USER);
        roleRepository.save(userRole);

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRole(RoleNameEnum.ADMIN);
        roleRepository.save(adminRole);

        testUser = new UserEntity();
        testUser.setPassword("12345");
        testUser.setUsername("anna");
        testUser.setFirstName("Anna");
        testUser.setLastName("Mileva");
        testUser.setEmail("anna@gmail.com");
        testUser.setRoles(List.of(userRole));

        testUser = userRepository.save(testUser);

        admin = new UserEntity();
        admin.setPassword("12345");
        admin.setUsername("admin");
        admin.setFirstName("Admin");
        admin.setLastName("Adminov");
        admin.setEmail("admin@gmail.com");
        admin.setRoles(List.of(adminRole));

        admin = userRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetComments() throws Exception {
        RecipeEntity testRecipe = initComments(initRecipe());

        mockMvc.perform(get("/api/" + testRecipe.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].message", is(COMMENT_1)))
                .andExpect(jsonPath("$.[0].user", is("Anna Mileva")))
                .andExpect(jsonPath("$.[1].message", is(COMMENT_2)))
                .andExpect(jsonPath("$.[1].user", is("Anna Mileva")))
                .andExpect(jsonPath("$.[2].message", is(COMMENT_3)))
                .andExpect(jsonPath("$.[2].user", is("Admin Adminov")));
    }

    private RecipeEntity initRecipe() {
        RecipeEntity testRecipe = new RecipeEntity();
        testRecipe.setName("Testing recipe")
                .setProducts("Testing products")
                .setDescription("Testing description")
                .setLevel(LevelEnum.EASY)
                .setAuthor(userRepository.findByUsername("anna").get())
                .setCategory(CategoryNameEnum.VEGAN)
                .setTimeNeeded(30)
                .setPortions(4);

        return testRecipe = recipeRepository.save(testRecipe);
    }

    private RecipeEntity initComments(RecipeEntity testRecipe) {
        CommentEntity comment1 = new CommentEntity();
        comment1.setAuthor(testUser);
        comment1.setCreated(LocalDateTime.now());
        comment1.setTextContent(COMMENT_1);
        comment1.setRecipe(testRecipe);

        CommentEntity comment2 = new CommentEntity();
        comment2.setAuthor(testUser);
        comment2.setCreated(LocalDateTime.now());
        comment2.setTextContent(COMMENT_2);
        comment2.setRecipe(testRecipe);

        CommentEntity comment3 = new CommentEntity();
        comment3.setAuthor(admin);
        comment3.setCreated(LocalDateTime.now());
        comment3.setTextContent(COMMENT_3);
        comment3.setRecipe(testRecipe);

        testRecipe.setComments(List.of(comment1, comment2, comment3));

        return recipeRepository.save(testRecipe);
    }

    @Test
    @WithMockUser("anna")
    void testCreateCommentsByLoggedUser() throws Exception {

        AddCommentDto testComment = new AddCommentDto();
        testComment.setMessage(COMMENT_1);

        RecipeEntity emptyRecipe = initRecipe();

        mockMvc.perform(post("/api/" + emptyRecipe.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testComment))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", MatchesPattern.matchesPattern("/api/" + emptyRecipe.getId() + "/comments/" + "\\d+")))
                .andExpect(jsonPath("$.message").value(is(COMMENT_1)))
                .andExpect(jsonPath("$.user", is("Anna Mileva")));
    }

    @Test
    void testCreateCommentsByAnonymousUser() throws Exception {

        AddCommentDto testComment = new AddCommentDto();
        testComment.setMessage(COMMENT_1);

        RecipeEntity emptyRecipe = initRecipe();

        mockMvc.perform(post("/api/" + emptyRecipe.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testComment))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void deleteCommentByAnonymousUser() throws Exception {

        RecipeEntity testRecipe = initComments(initRecipe());

        mockMvc.perform(MockMvcRequestBuilders.delete(
                        "/api/" + testRecipe.getId() + "/comments/" + testRecipe.getComments().get(0).getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("anna")
    void deleteCommentByAuthor() throws Exception {

        RecipeEntity testRecipe = initComments(initRecipe());

        mockMvc.perform(MockMvcRequestBuilders.delete(
                                "/api/" + testRecipe.getId() + "/comments/" + testRecipe.getComments().get(0).getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(COMMENT_1)))
                .andExpect(jsonPath("$.user", is("Anna Mileva")));
    }

    @Test
    @WithMockUser("anna")
    void deleteCommentByNotAuthor() throws Exception {

        RecipeEntity testRecipe = initComments(initRecipe());

        mockMvc.perform(MockMvcRequestBuilders.delete(
                        "/api/" + testRecipe.getId() + "/comments/" + testRecipe.getComments().get(0).getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("admin")
    void deleteCommentByAdmin() throws Exception {

        RecipeEntity testRecipe = initComments(initRecipe());

        mockMvc.perform(MockMvcRequestBuilders.delete(
                                "/api/" + testRecipe.getId() + "/comments/" + testRecipe.getComments().get(0).getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(COMMENT_1)))
                .andExpect(jsonPath("$.user", is("Anna Mileva")));
    }
}