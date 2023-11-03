package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.dto.AddRecipeDto;
import bg.example.recepeWebsite.model.entity.PictureEntity;
import bg.example.recepeWebsite.model.entity.RecipeEntity;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.model.view.RecipeDetailsViewModel;
import bg.example.recepeWebsite.model.view.RecipeViewModel;
import bg.example.recepeWebsite.repository.RecipeRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        for (MultipartFile file : addRecipeDto.getPictureFiles()) {
            PictureEntity picture = pictureService.createAndSavePictureEntity(userDetails.getId(), file);
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

                    recipeDetailsViewModel.setAuthor(recipe.getAuthor().getFirstName() + " " + recipe.getAuthor().getLastName());
                    recipeDetailsViewModel.getPictures().forEach(p -> p.setCanNotDelete(!p.getAuthor().getUsername().equals(principalName)));

                    return recipeDetailsViewModel;
                })
                .orElse(null);
    }




}
