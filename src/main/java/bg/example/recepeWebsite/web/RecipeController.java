package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.AddRecipeDto;
import bg.example.recepeWebsite.model.dto.UploadPictureDto;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.service.CloudinaryService;
import bg.example.recepeWebsite.service.PictureService;
import bg.example.recepeWebsite.service.RecipeService;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final CloudinaryService cloudinaryService;
    private final PictureService pictureService;

    public RecipeController(RecipeService recipeService, CloudinaryService cloudinaryService, PictureService pictureService) {
        this.recipeService = recipeService;
        this.cloudinaryService = cloudinaryService;
        this.pictureService = pictureService;
    }

    @ModelAttribute
    AddRecipeDto addRecipeDto() {
        return new AddRecipeDto();
    }

    @ModelAttribute("uploadPictureDto")
    UploadPictureDto uploadPictureDto() {
        return new UploadPictureDto();
    }


    @GetMapping("/all")
    public String allRecipes(Model model) {
        model.addAttribute("recipes", recipeService.findAllRecipeViewModels());
        return "all-recipes";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String addRecipe() {
        return "add-recipe";
    }

    @PostMapping("/add")
    public String addRecipeConfirm(@Valid AddRecipeDto addRecipeDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("addRecipeDto", addRecipeDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addRecipeDto"
                            , bindingResult);

            return "redirect:add";
        }

        recipeService.addRecipe(addRecipeDto, userDetails);
        return "redirect:all";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model, Principal principal) {

        model.addAttribute("recipe", recipeService.findRecipeDetailsViewModelById(id, principal != null ? principal.getName() : ""));
        return "recipe-details";

    }

    @GetMapping("/vegetarian")
    public String vegetarianRecipes(Model model) {
        model.addAttribute("recipes", recipeService.findAllFilteredRecipesViewModels(CategoryNameEnum.VEGETARIAN));
        return "all-recipes";
    }

    @GetMapping("/mostCommented")
    public String getMostCommentedRecipe(Model model) {
        model.addAttribute("recipe", recipeService.findMostCommentedRecipeViewModel());
        return "recipe-details";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/details/{id}/picture/add")
    public String addPicture(UploadPictureDto uploadPictureDto,
                             @PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails principal) throws IOException {

        pictureService.uploadPicture(principal.getId(), id, uploadPictureDto.getPicture());

        return "redirect:/recipes/details/" + id;
    }

    @PreAuthorize("isAuthenticated() && @pictureService.isOwner(#principal.name, #pictureId)")
    @DeleteMapping("/details/{recipeId}")
    public String deletePicture(@PathVariable("recipeId") Long recipeId,
                                @RequestParam("pictureId") Long pictureId,
                                Principal principal){
        pictureService.deletePicture(pictureId);
        return "redirect:/recipes/details/" + recipeId;

    }


}
