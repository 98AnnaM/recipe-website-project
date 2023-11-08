package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.errors.ObjectNotFoundException;
import bg.example.recepeWebsite.model.dto.AddRecipeDto;
import bg.example.recepeWebsite.model.dto.EditRecipeDto;
import bg.example.recepeWebsite.model.dto.SearchRecipeDto;
import bg.example.recepeWebsite.model.entity.PictureEntity;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.model.view.RecipeDetailsViewModel;
import bg.example.recepeWebsite.model.view.RecipeViewModel;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.RecipeSpecification;
import bg.example.recepeWebsite.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TypeService typeService;
    private final PictureService pictureService;

    public RecipeService(RecipeRepository recipeRepository, ModelMapper modelMapper, UserRepository userRepository, TypeService categoryService, PictureService pictureService) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.typeService = categoryService;
        this.pictureService = pictureService;
    }

    @Transactional
    public List<RecipeViewModel> findAllRecipeViewModels() {
        return this.recipeRepository.
                findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RecipeViewModel> findAllFilteredRecipesViewModels(CategoryNameEnum category) {
        return this.recipeRepository.
                findAllByCategoryName(category)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());

    }

    private RecipeViewModel map(RecipeEntity recipe){
        RecipeViewModel recipeViewModel = modelMapper.map(recipe, RecipeViewModel.class);
        recipeViewModel.setAuthor(recipe.getAuthor().getFirstName() + " " + recipe.getAuthor().getLastName());
        recipeViewModel.setPictureUrl(
                recipe.getPictures()
                        .stream()
                        .sorted(Comparator.comparingLong(PictureEntity::getId))
                        .collect(Collectors.toList())
                        .get(0)
                        .getUrl());
        return recipeViewModel;

    }

    @Transactional
    public RecipeDetailsViewModel findMostCommentedRecipeViewModel() {
        if (recipeRepository.count() == 0){
            return null;
        }
        RecipeEntity mostCommented = this.recipeRepository.findRecipeWithMostComments().get(0);
        RecipeDetailsViewModel recipeDetailsViewModel = modelMapper
                .map(mostCommented, RecipeDetailsViewModel.class);

        recipeDetailsViewModel.setAuthor(mostCommented.getAuthor().getFirstName());
        return recipeDetailsViewModel;
    }


    public void addRecipe(AddRecipeDto addRecipeDto, CustomUserDetails userDetails) throws IOException {

        RecipeEntity newRecipe = modelMapper.map(addRecipeDto, RecipeEntity.class);

        newRecipe.setAuthor(userRepository.findById(userDetails.getId()).orElseThrow());
        newRecipe.setTypes(addRecipeDto.getTypes()
                .stream()
                .map(typeService::findByTypeName)
                .collect(Collectors.toList()));


        newRecipe.setPictures(new ArrayList<>());
        newRecipe = this.recipeRepository.save(newRecipe);
        for (MultipartFile file : addRecipeDto.getPictureFiles()) {
            PictureEntity picture = pictureService.createAndSavePictureEntity(userDetails.getId(), file, newRecipe.getId());
            newRecipe.getPictures().add(picture);
        }

        recipeRepository.save(newRecipe);
    }

    @Transactional
    public RecipeDetailsViewModel findRecipeDetailsViewModelById(Long id, String principalName) {
        return  this.recipeRepository
                .findById(id)
                .map(recipe -> {
                    RecipeDetailsViewModel recipeDetailsViewModel = modelMapper
                            .map(recipe, RecipeDetailsViewModel.class);

                    recipeDetailsViewModel.setProducts(Arrays.stream(recipe.getProducts().split("[\r\n]+")).collect(Collectors.toList()));
                    recipeDetailsViewModel.setAuthor(recipe.getAuthor().getFirstName() + " " + recipe.getAuthor().getLastName());
                    recipeDetailsViewModel.getPictures().forEach(p -> p.setCanNotDelete(!p.getAuthor().getUsername().equals(principalName)));
                    recipeDetailsViewModel.setCanDelete(isOwner(principalName, id));
                    recipeDetailsViewModel.setVideoId(extractVideoId(recipe.getVideoUrl()));

                    return recipeDetailsViewModel;
                })
                .orElse(null);
    }

    public boolean isOwner(String userName, Long recipeId) {
        boolean isOwner = recipeRepository.
                findById(recipeId).
                filter(o -> o.getAuthor().getUsername().equals(userName)).
                isPresent();

        if (isOwner){
            return true;
        }

        return userRepository
                .findByUsername(userName)
                .filter(this::isAdmin)
                .isPresent();
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRoles().
                stream().
                anyMatch(r -> r.getRole() == RoleNameEnum.ADMIN);
    }


    @Transactional
    public EditRecipeDto getRecipeEditDetails(Long recipeId){

        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with ID " + recipeId + "not found"));

        EditRecipeDto editRecipeDto = modelMapper.map(recipeEntity, EditRecipeDto.class);
        editRecipeDto.setTypes(recipeEntity.getTypes()
                .stream()
                .map(TypeEntity::getName)
                .collect(Collectors.toList()));

        return editRecipeDto;
    }


    public void updateRecipeById(EditRecipeDto editRecipeDto, Long id, UserDetails userDetails) {
        RecipeEntity updateRecipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with id: " + id + "not found!"));

        updateRecipe.setAuthor(userRepository.findByUsername(userDetails.getUsername()).orElseThrow());
        updateRecipe.setTypes(editRecipeDto.getTypes()
                .stream()
                .map(typeService::findByTypeName)
                .collect(Collectors.toList()));
        updateRecipe.setName(editRecipeDto.getName())
                    .setLevel(editRecipeDto.getLevel())
                    .setCategory(editRecipeDto.getCategory())
                    .setPortions(editRecipeDto.getPortions())
                            .setTimeNeeded(editRecipeDto.getTimeNeeded())
                            .setDescription(editRecipeDto.getDescription())
                            .setVideoUrl(editRecipeDto.getVideoUrl())
                            .setProducts(editRecipeDto.getProducts());

        recipeRepository.save(updateRecipe);
    }

    public static String extractVideoId(String videoUrl) {
        String pattern = "(?<=v=|\\/videos\\/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|%2Fvideos%2F|%2Fvi%2F|v=|%2Fv%2F)([a-zA-Z0-9_-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(videoUrl);

        if (matcher.find()) {
            return matcher.group();
        }

        return null; // Video ID not found
    }

    @Transactional
    public void deleteRecipeById(Long recipeId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElse(null);

        if (recipe != null) {
            recipe.getPictures().forEach(picture -> pictureService.deletePicture(picture.getId()));
            recipeRepository.deleteById(recipeId);
        }
    }
@Transactional
    public List<RecipeViewModel> searchRecipe(SearchRecipeDto searchRecipeDto) {
        return this.recipeRepository
                .findAll(new RecipeSpecification(searchRecipeDto))
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

}
