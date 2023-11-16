package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.repository.UserRepository;
import bg.example.recepeWebsite.web.exception.ObjectNotFoundException;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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

    public RecipeService(RecipeRepository recipeRepository, ModelMapper modelMapper, UserRepository userRepository, TypeService typeService, PictureService pictureService) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.typeService = typeService;
        this.pictureService = pictureService;
    }

    @Transactional
    public Page<RecipeViewModel> findAllRecipeViewModels(Pageable pageable) {
        return this.recipeRepository.
                findAll(pageable)
                .map(this::mapToRecipeViewModel);
    }

    @Transactional
    public Page<RecipeViewModel> findAllFilteredRecipesViewModels(CategoryNameEnum category, Pageable pageable) {
        return this.recipeRepository.
                findAllByCategory(category, pageable)
                .map(this::mapToRecipeViewModel);

    }

    public RecipeViewModel mapToRecipeViewModel(RecipeEntity recipe){
        RecipeViewModel recipeViewModel = modelMapper.map(recipe, RecipeViewModel.class);
        recipeViewModel.setAuthor(recipe.getAuthor().getFirstName() + " " + recipe.getAuthor().getLastName());
        recipeViewModel.setPictureUrl( !recipe.getPictures().isEmpty() ?
                recipe.getPictures()
                        .stream()
                        .sorted(Comparator.comparingLong(PictureEntity::getId))
                        .collect(Collectors.toList())
                        .get(0)
                        .getUrl() : "/static/images/register.jpg");
        return recipeViewModel;

    }


    @Transactional
    public void addRecipe(AddRecipeDto addRecipeDto, CustomUserDetails userDetails) {

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

        RecipeEntity recipeEntity = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with ID " + id + " not found!"));

        RecipeDetailsViewModel recipeDetailsViewModel = modelMapper
                .map(recipeEntity, RecipeDetailsViewModel.class);

        recipeDetailsViewModel.setPictures(recipeEntity.getPictures()
                .stream()
                .map(p -> pictureService.mapToPictureViewModel(p, principalName))
                .collect(Collectors.toList()));

        recipeDetailsViewModel.setProducts(Arrays.stream(recipeEntity.getProducts().split("[\r\n]+")).collect(Collectors.toList()));
        recipeDetailsViewModel.setAuthor(recipeEntity.getAuthor().getFirstName() + " " + recipeEntity.getAuthor().getLastName());
        recipeDetailsViewModel.setCanDelete(isOwner(principalName, id));
        recipeDetailsViewModel.setIsFavorite(isRecipeFavorite(principalName, recipeEntity.getId()));
        recipeDetailsViewModel.setVideoId(extractVideoId(recipeEntity.getVideoUrl()));

        return  recipeDetailsViewModel;
    }

    public boolean isOwner(String userName, Long recipeId) {
        boolean isOwner = recipeRepository.
                findById(recipeId).
                filter(r -> r.getAuthor().getUsername().equals(userName)).
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

    public boolean isRecipeFavorite(String username, Long recipeId){
        UserEntity user = this.userRepository.findByUsername(username).orElse(null);

        if (user == null){
         return false;
        }

        return user.getFavorites().stream().map(RecipeEntity::getId).anyMatch(id -> id.equals(recipeId));
    }

    @Transactional
    public EditRecipeDto getRecipeEditDetails(Long recipeId){

        RecipeEntity recipeEntity = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with ID " + recipeId + " not found"));

        EditRecipeDto editRecipeDto = modelMapper.map(recipeEntity, EditRecipeDto.class);
        editRecipeDto.setTypes(recipeEntity.getTypes()
                .stream()
                .map(TypeEntity::getName)
                .collect(Collectors.toList()));

        return editRecipeDto;
    }


    public void updateRecipeById(EditRecipeDto editRecipeDto, Long id, UserDetails userDetails) {
        RecipeEntity updateRecipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with id: " + id + " not found!"));

        updateRecipe.setAuthor(userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("User with username " + userDetails.getUsername() + " not found!")));

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
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe with ID " + recipeId + " not found!"));

        recipe.getPictures().forEach(picture -> pictureService.deletePicture(picture.getId()));
        recipeRepository.deleteById(recipeId);

    }
@Transactional
    public Page<RecipeViewModel> searchRecipe(SearchRecipeDto searchRecipeDto, Pageable pageable) {
        return this.recipeRepository
                .findAll(new RecipeSpecification(searchRecipeDto), pageable)
                .map(this::mapToRecipeViewModel);
    }

    @Transactional
    public Page<RecipeViewModel> findAllRecipesUploadedByUserId(Long id, Pageable pageable) {
        return this.recipeRepository.
                findAllByAuthor_Id(id, pageable)
                .map(this::mapToRecipeViewModel);
    }

    @Transactional
    public Page<RecipeViewModel> findAllFavoriteRecipesForUserId(Long userId, Pageable pageable) {
        return this.recipeRepository.
                findAllFavoriteRecipes(userId, pageable)
                .map(this::mapToRecipeViewModel);
    }

    public int findAllByCategory(CategoryNameEnum categoryNameEnum) {
        return this.recipeRepository.countAllByCategory(categoryNameEnum);
    }

    public long findAll() {
        return this.recipeRepository.count();
    }


}
