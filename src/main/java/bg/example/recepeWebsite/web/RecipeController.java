package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.AddRecipeDto;
import bg.example.recepeWebsite.model.dto.EditRecipeDto;
import bg.example.recepeWebsite.model.dto.SearchRecipeDto;
import bg.example.recepeWebsite.model.dto.UploadPictureDto;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.service.CloudinaryService;
import bg.example.recepeWebsite.service.PictureService;
import bg.example.recepeWebsite.service.RecipeService;
import javax.validation.Valid;

import bg.example.recepeWebsite.service.TypeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final CloudinaryService cloudinaryService;
    private final PictureService pictureService;
    private final TypeService typeService;

    public RecipeController(RecipeService recipeService, CloudinaryService cloudinaryService, PictureService pictureService, TypeService typeService) {
        this.recipeService = recipeService;
        this.cloudinaryService = cloudinaryService;
        this.pictureService = pictureService;
        this.typeService = typeService;
    }

    @ModelAttribute
    AddRecipeDto addRecipeDto() {
        return new AddRecipeDto();
    }

    @ModelAttribute("uploadPictureDto")
    UploadPictureDto uploadPictureDto() {
        return new UploadPictureDto();
    }

    @ModelAttribute("allTypes")
    List<TypeNameEnum> allTypes() {
        return typeService.getAllTypes();
    }


    @GetMapping("/all")
    public String allRecipes(Model model,
                             @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 1) Pageable pageable) {
        model.addAttribute("recipes", recipeService.findAllRecipeViewModels(pageable));
        return "all-recipes";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String addRecipe(Model model) {
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

        pictureService.createAndSavePictureEntity(principal.getId(), uploadPictureDto.getPicture(), id);
        return "redirect:/recipes/details/" + id;
    }

    @PreAuthorize("isAuthenticated() && @pictureService.isOwner(#principal.name, #pictureId)")
    @DeleteMapping("/details/{recipeId}/picture/delete")
    public String deletePicture(@PathVariable("recipeId") Long recipeId,
                                @RequestParam("pictureId") Long pictureId,
                                Principal principal){
        pictureService.deletePicture(pictureId);
        return "redirect:/recipes/details/" + recipeId;

    }

    @PreAuthorize("@recipeService.isOwner(#principal.name, #recipeId)")
    @GetMapping("/edit/{id}")
    public String editRecipe(
            Principal principal,
            @PathVariable("id") Long recipeId,
            Model model){

        EditRecipeDto recipe = recipeService.getRecipeEditDetails(recipeId);

        model.addAttribute("recipe", recipe);
        return "recipe-update";
    }

    @PutMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid EditRecipeDto recipeModel,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("recipeModel", recipeModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.recipeModel", bindingResult);
            return "redirect:/recipes/edit/{id}";
        }

        recipeService.updateRecipeById(recipeModel, id, userDetails);

        return "redirect:/recipes/details/" + id;
    }

    @PreAuthorize("isAuthenticated() && @recipeService.isOwner(#principal.name, #recipeId)")
    @DeleteMapping("/delete/{id}")
    public String deleteRecipe(
            Principal principal,
            @PathVariable("id") Long recipeId){
        recipeService.deleteRecipeById(recipeId);

        return "redirect:/recipes/all";
    }

    @GetMapping("/search")
    public String searchQuery(@Valid SearchRecipeDto searchRecipeDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("searchRecipeDto", searchRecipeDto);
            model.addAttribute(
                    "org.springframework.validation.BindingResult.searchRecipeDto",
                    bindingResult);
            return "recipe-search";
        }

        if (!model.containsAttribute("searchRecipeDto")) {
            model.addAttribute("searchRecipeDto", searchRecipeDto);
            model.addAttribute("result", searchRecipeDto.toString());
        }

        if (!searchRecipeDto.isEmpty()) {
            model.addAttribute("recipes", recipeService.searchRecipe(searchRecipeDto));
            model.addAttribute("result", searchRecipeDto.toString());
        }

        return "recipe-search";
    }


}
