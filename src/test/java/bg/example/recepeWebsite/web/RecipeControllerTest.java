package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.service.CloudinaryImage;
import bg.example.recepeWebsite.service.CloudinaryService;
import bg.example.recepeWebsite.service.EmailService;
import bg.example.recepeWebsite.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

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

    private RecipeEntity testRecipe, testAdminRecipe;

    @MockBean
    private CloudinaryService mockCloudinaryService;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("user");
        testAdmin = testDataUtils.createTestAdmin("admin");
        List<TypeEntity> testTypes = testDataUtils.createTestTypes();

        testRecipe = testDataUtils.createTestRecipe(testUser, testTypes);
        testAdminRecipe = testDataUtils.createTestRecipe(testAdmin, testTypes);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testDeleteByAnonymousUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/recipes/delete/{id}", testRecipe.getId()).
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
        mockMvc.perform(delete("/recipes/delete/{id}", testRecipe.getId()).
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
        mockMvc.perform(delete("/recipes/delete/{id}", testRecipe.getId()).
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

    @WithUserDetails(value = "user",
            userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testAddOffer() throws Exception {

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



}