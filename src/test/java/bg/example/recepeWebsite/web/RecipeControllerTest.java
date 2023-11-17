package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.service.CloudinaryImage;
import bg.example.recepeWebsite.service.CloudinaryService;
import bg.example.recepeWebsite.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private UserEntity testUser, testAdmin;

    private RecipeEntity testUserRecipe, testAdminRecipe;

    @MockBean
    private CloudinaryService mockCloudinaryService;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
        testAdmin = testDataUtils.createTestAdmin("admin");
        List<TypeEntity> testTypes = testDataUtils.createTestTypes();

        testUserRecipe = testDataUtils.createTestRecipe(testUser, testTypes);
        testAdminRecipe = testDataUtils.createTestRecipe(testAdmin, testTypes);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testDeleteByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/recipes/delete/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"})
    void testDeleteByAdmin() throws Exception {
        mockMvc.perform(delete("/recipes/delete/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/recipes/all"));
    }

    @WithMockUser(
            username = "user",
            roles = "USER")
    @Test
    void testDeleteByOwner() throws Exception {
        mockMvc.perform(delete("/recipes/delete/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/recipes/all"));
    }

    @WithMockUser(
            username = "user",
            roles = "USER")
    @Test
    public void testDeleteNotOwned_Forbidden() throws Exception {
        mockMvc.perform(delete("/recipes/delete/{id}", testAdminRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().isForbidden());
    }

    @Test
    void testAddRecipeByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(get("/recipes/add").
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DirtiesContext
    void testAddRecipeByLoggedUser_Success() throws Exception {

        when(mockCloudinaryService.uploadImage(any(MultipartFile.class)))
                .thenReturn(new CloudinaryImage().setUrl("mockUrl").setPublicId("mockPublicId"));

        long beforeCount = testDataUtils.getRecipeRepository().count();

        MockMultipartFile picture1 = new MockMultipartFile("pictureFiles", "file1.jpg", "image/jpeg", "fileContent1".getBytes());
        MockMultipartFile picture2 = new MockMultipartFile("pictureFiles", "file2.jpg", "image/jpeg", "fileContent2".getBytes());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/recipes/add")
                                .file(picture1)
                                .file(picture2)
                                // Add other parameters
                                .param("name", "Test recipe")
                                .param("products", "Test products")
                                .param("description", "Test description")
                                .param("level", "EASY")
                                .param("videoUrl", "video://test.mp4")
                                .param("category", "VEGAN")
                                .param("timeNeeded", "30")
                                .param("portions", "4")
                                .param("types", "BREAKFAST", "SALAD")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        long afterCount = testDataUtils.getRecipeRepository().count();
        Assertions.assertEquals(beforeCount + 1, afterCount);

        String redirectedUrl = result.getResponse().getRedirectedUrl();
        mockMvc.perform(get("http://localhost" + redirectedUrl))
                .andExpect(status().isOk());

        String recipeId = redirectedUrl.split("/")[redirectedUrl.split("/").length - 1];
        Assertions.assertTrue(testDataUtils.getRecipeRepository().existsById(Long.parseLong(recipeId)));

        mockMvc.perform(get("/recipes/details/{recipeId}", recipeId))
                .andExpect(status().isOk());

        Assertions.assertEquals(redirectedUrl, "/recipes/details/" + recipeId);
    }

    @Test
    void testAddRecipeByLoggedUser_Fail() throws Exception {

        when(mockCloudinaryService.uploadImage(any(MultipartFile.class)))
                .thenReturn(new CloudinaryImage().setUrl("mockUrl").setPublicId("mockPublicId"));

        long beforeCount = testDataUtils.getRecipeRepository().count();

        MockMultipartFile picture1 = new MockMultipartFile("pictureFiles", "file1.jpg", "image/jpeg", "fileContent1".getBytes());
        MockMultipartFile picture2 = new MockMultipartFile("pictureFiles", "file2.jpg", "image/jpeg", "fileContent2".getBytes());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/recipes/add")
                                .file(picture1)
                                .file(picture2)
                                // Add other parameters
                                .param("name", " ")
                                .param("products", "Test products")
                                .param("description", "Test description")
                                .param("level", "EASY")
                                .param("videoUrl", "video://test.mp4")
                                .param("category", "VEGAN")
                                .param("timeNeeded", "30")
                                .param("portions", "4")
                                .param("types", "BREAKFAST", "SALAD")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        long afterCount = testDataUtils.getRecipeRepository().count();
        Assertions.assertEquals(beforeCount, afterCount);

        String redirectedUrl = result.getResponse().getRedirectedUrl();
        Assertions.assertEquals(redirectedUrl, "/recipes/add");
    }

    @Test
    void testUpdateByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(get("/recipes/edit/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @WithUserDetails(value = "admin",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testUpdateByAdmin() throws Exception {
        mockMvc.perform(get("/recipes/edit/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("recipe-update"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testUpdateByOwner() throws Exception {
        mockMvc.perform(get("/recipes/edit/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("recipe-update"));
    }

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    @Transactional
    void testUpdateRecipeByOwner_Success() throws Exception {

        MvcResult result = mockMvc.perform(
                        put("/recipes/edit/{id}", testUserRecipe.getId())
                                .param("name", "New recipe name")
                                .param("products", "New products")
                                .param("description", "New description")
                                .param("level", "HARD")
                                .param("videoUrl", "video://newVideo.mp4")
                                .param("category", "WITH_MEAT")
                                .param("timeNeeded", "45")
                                .param("portions", "10")
                                .param("types", "BREAKFAST")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String redirectedUrl = result.getResponse().getRedirectedUrl();
        Assertions.assertEquals(redirectedUrl, "/recipes/details/" + testUserRecipe.getId());

        RecipeEntity updatedRecipe = testDataUtils.getRecipeRepository().findById(testUserRecipe.getId()).orElse(null);
        Assertions.assertNotNull(updatedRecipe);
        Assertions.assertEquals("New recipe name", updatedRecipe.getName());
        Assertions.assertEquals("New products", updatedRecipe.getProducts());
        Assertions.assertEquals("New description", updatedRecipe.getDescription());
        Assertions.assertEquals(LevelEnum.HARD, updatedRecipe.getLevel());
        Assertions.assertEquals("video://newVideo.mp4", updatedRecipe.getVideoUrl());
        Assertions.assertEquals(CategoryNameEnum.WITH_MEAT, updatedRecipe.getCategory());
        Assertions.assertEquals(45, updatedRecipe.getTimeNeeded());
        Assertions.assertEquals(10, updatedRecipe.getPortions());
        Assertions.assertEquals(1, updatedRecipe.getTypes().size());
        Assertions.assertEquals(TypeNameEnum.BREAKFAST, updatedRecipe.getTypes().get(0).getName());
    }

    @Test
    @Transactional
    void testUpdateRecipeByOwner_Fail() throws Exception {

        MvcResult result = mockMvc.perform(
                        put("/recipes/edit/{id}", testUserRecipe.getId())
                                .param("name", " ")
                                .param("products", "New products")
                                .param("description", "New description")
                                .param("level", "HARD")
                                .param("videoUrl", "video://newVideo.mp4")
                                .param("category", "WITH_MEAT")
                                .param("timeNeeded", "45")
                                .param("portions", "10")
                                .param("types", "BREAKFAST")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String redirectedUrl = result.getResponse().getRedirectedUrl();
        Assertions.assertEquals(redirectedUrl, "/recipes/edit/" + testUserRecipe.getId());

        RecipeEntity updatedRecipe = testDataUtils.getRecipeRepository().findById(testUserRecipe.getId()).orElse(null);
        Assertions.assertNotNull(updatedRecipe);
        Assertions.assertEquals("Testing recipe", updatedRecipe.getName());
        Assertions.assertEquals("Testing products", updatedRecipe.getProducts());
        Assertions.assertEquals("Testing description", updatedRecipe.getDescription());
        Assertions.assertEquals(LevelEnum.EASY, updatedRecipe.getLevel());
        Assertions.assertEquals("http//videoUrl", updatedRecipe.getVideoUrl());
        Assertions.assertEquals(CategoryNameEnum.VEGAN, updatedRecipe.getCategory());
        Assertions.assertEquals(30, updatedRecipe.getTimeNeeded());
        Assertions.assertEquals(4, updatedRecipe.getPortions());
        Assertions.assertEquals(2, updatedRecipe.getTypes().size());

    }

    @Test
    void testViewAllRecipesByAnonymousUser_Success() throws Exception {
        mockMvc.perform(get("/recipes/all").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("all-recipes"));
    }

    @Test
    void testViewVegetarianRecipesByAnonymousUser_Success() throws Exception {
        mockMvc.perform(get("/recipes/vegetarian").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("all-recipes"));
    }

    @Test
    void testViewVeganRecipesByAnonymousUser_Success() throws Exception {
        mockMvc.perform(get("/recipes/vegan").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("all-recipes"));
    }

    @Test
    void testViewWithMeatRecipesByAnonymousUser_Success() throws Exception {
        mockMvc.perform(get("/recipes/withMeat").
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("all-recipes"));
    }

    @Test
    void testViewRecipeDetails() throws Exception {
        mockMvc.perform(get("/recipes/details/{id}", testUserRecipe.getId()).
                        with(csrf())
                ).
                andExpect(status().isOk()).
                andExpect(view().name("recipe-details"));
    }







}