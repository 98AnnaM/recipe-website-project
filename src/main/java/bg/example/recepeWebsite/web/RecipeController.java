package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.AddRecipeDto;
import bg.example.recepeWebsite.model.dto.EditRecipeDto;
import bg.example.recepeWebsite.model.dto.SearchRecipeDto;
import bg.example.recepeWebsite.model.dto.UploadPictureDto;
import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.model.view.RecipeViewModel;
import bg.example.recepeWebsite.service.PictureService;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.TypeService;
import org.springframework.data.domain.Page;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final PictureService pictureService;
    private final TypeService typeService;

    public RecipeController(RecipeService recipeService, PictureService pictureService, TypeService typeService) {
        this.recipeService = recipeService;
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
                             @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {
        Page<RecipeViewModel> pagesWithAllRecipes = recipeService.findAllRecipeViewModels(pageable);
        model.addAttribute("recipes", pagesWithAllRecipes);
        model.addAttribute("heading", String.format("All recipes (%s)", pagesWithAllRecipes.getTotalElements()));
        model.addAttribute("url", "/recipes/all");
        return "all-recipes";
    }

    @GetMapping("/vegetarian")
    public String vegetarianRecipes(Model model,
                                    @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {
        Page<RecipeViewModel> pagesWithVeganRecipes = recipeService.findAllFilteredRecipesViewModels(CategoryNameEnum.VEGETARIAN, pageable);
        model.addAttribute("recipes", pagesWithVeganRecipes);
        model.addAttribute("heading", String.format("Vegetarian recipes (%s)", pagesWithVeganRecipes.getTotalElements()));
        model.addAttribute("url", "/recipes/vegetarian");
        return "all-recipes";
    }

    @GetMapping("/withMeat")
    public String withMeatRecipes(Model model,
                                  @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {
        Page<RecipeViewModel> pagesWithMeatRecipes = recipeService.findAllFilteredRecipesViewModels(CategoryNameEnum.WITH_MEAT, pageable);
        model.addAttribute("recipes", pagesWithMeatRecipes);
        model.addAttribute("heading", String.format("Recipes with meat (%s)", pagesWithMeatRecipes.getTotalElements()));
        model.addAttribute("url", "/recipes/withMeat");
        return "all-recipes";
    }

    @GetMapping("/vegan")
    public String veganRecipes(Model model,
                               @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {
        Page<RecipeViewModel> pagesWithVeganRecipes = recipeService.findAllFilteredRecipesViewModels(CategoryNameEnum.VEGAN, pageable);
        model.addAttribute("recipes", pagesWithVeganRecipes);
        model.addAttribute("heading", String.format("Vegan recipes (%s)", pagesWithVeganRecipes.getTotalElements()));
        model.addAttribute("url", "/recipes/vegan");
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
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("addRecipeDto", addRecipeDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addRecipeDto"
                            , bindingResult);

            return "redirect:/recipes/add";
        }
        Long recipeId = recipeService.addRecipe(addRecipeDto, userDetails);
        return "redirect:/recipes/details/" + recipeId;
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model, Principal principal) {

        model.addAttribute("recipe", recipeService.findRecipeDetailsViewModelById(id, principal != null ? principal.getName() : ""));
        return "recipe-details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/details/{id}/picture/add")
    public String addPicture(UploadPictureDto uploadPictureDto,
                             @PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails principal) {

        pictureService.createAndSavePictureEntity(principal.getId(), uploadPictureDto.getPicture(), id);
        return "redirect:/recipes/details/" + id;
    }

    @PreAuthorize("isAuthenticated() && @pictureService.isOwner(#principal.name, #pictureId)")
    @DeleteMapping("/details/{recipeId}/picture/delete")
    public String deletePicture(@PathVariable("recipeId") Long recipeId,
                                @RequestParam("pictureId") Long pictureId,
                                Principal principal) {
        pictureService.deletePicture(pictureId);
        return "redirect:/recipes/details/" + recipeId;
    }

    @PreAuthorize("isAuthenticated() && @recipeService.isOwner(#principal.name, #recipeId)")
    @GetMapping("/edit/{id}")
    public String editRecipe(
            Principal principal,
            @PathVariable("id") Long recipeId,
            Model model) {

        if (!model.containsAttribute("editRecipeDto")) {
            EditRecipeDto editRecipeDto = recipeService.getRecipeEditDetails(recipeId);
            model.addAttribute("editRecipeDto", editRecipeDto);
        }
        return "recipe-update";
    }

    @PutMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid EditRecipeDto editRecipeDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editRecipeDto", editRecipeDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editRecipeDto", bindingResult);
            return "redirect:/recipes/edit/" + id;
        }

        recipeService.updateRecipeById(editRecipeDto, id, userDetails);
        return "redirect:/recipes/details/" + id;
    }

    @PreAuthorize("isAuthenticated() && @recipeService.isOwner(#principal.name, #recipeId)")
    @DeleteMapping("/delete/{id}")
    public String deleteRecipe(
            Principal principal,
            @PathVariable("id") Long recipeId) {
        recipeService.deleteRecipeById(recipeId);
        return "redirect:/recipes/all";
    }

    @GetMapping("/search")
    public String searchQuery(@RequestParam Map<String, String> queryParams,
                              HttpServletRequest request,
                              @Valid SearchRecipeDto searchRecipeDto,
                              BindingResult bindingResult,
                              Model model,
                              @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {

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
            model.addAttribute("recipes", recipeService.searchRecipe(searchRecipeDto, pageable));
            model.addAttribute("result", searchRecipeDto.toString());

            String queryString = queryParams.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals("page") && !entry.getKey().equals("size"))
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));

            String url = request.getRequestURL().toString() + (queryString.isEmpty() ? "" : "?" + queryString);
            model.addAttribute("url", url);
        }
        return "recipe-search";
    }
}
