package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.UserEditDto;
import bg.example.recepeWebsite.model.view.PictureViewModel;
import bg.example.recepeWebsite.model.view.RecipeViewModel;
import bg.example.recepeWebsite.model.view.UserView;
import bg.example.recepeWebsite.service.PictureService;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users/profile")
public class UserController {

    private final UserService userService;
    private final RecipeService recipeService;
    private final PictureService pictureService;

    public UserController(UserService userService, RecipeService recipeService, PictureService pictureService) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.pictureService = pictureService;
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}")
    public String profile(@PathVariable Long id, Model model) {
        UserView user = this.userService.findById(id);
        model.addAttribute("user", this.userService.findById(id));
        return "profile";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/editProfile")
    public String editUserInformation(@PathVariable("id") Long id,
                                      Model model) {

        if (!model.containsAttribute("userEditDto")) {
            UserEditDto userEditDto = userService.getUserEditDetails(id);
            model.addAttribute("userEditDto", userEditDto);
        }
        return "profile-edit";
    }

    @PutMapping("/{id}/editProfile")
    public String update(@PathVariable("id") Long id,
                         @Valid UserEditDto userEditDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        final UserEditDto currentUser = userService.getUserEditDetails(id);
        if (!bindingResult.hasFieldErrors("username") &&
                !userEditDto.getUsername().equalsIgnoreCase(currentUser.getUsername()) &&
                userService.usernameExists(userEditDto.getUsername())) {
            bindingResult.addError(new FieldError("userEditDto", "username", userEditDto.getUsername(), false, null, null, "This username is occupied!"));
        }

        if (!bindingResult.hasFieldErrors("email") &&
                !userEditDto.getEmail().equals(currentUser.getEmail()) &&
                userService.emailExists(userEditDto.getEmail())) {
            bindingResult.addError(new FieldError("userEditDto", "email", userEditDto.getEmail(), false, null, null, "This email is occupied!"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userEditDto", userEditDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditDto", bindingResult);
            return "redirect:/users/profile/" + id + "/editProfile";
        }

        userService.updateUserProfile(userEditDto);
        return "redirect:/users/profile/" + id;
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/addedRecipes")
    public String addedRecipes(@PathVariable Long id,
                               Model model,
                               @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {

        Page<RecipeViewModel> recipes = recipeService.findAllRecipesUploadedByUserId(id, pageable);
        model.addAttribute("recipes", recipes);
        model.addAttribute("heading",
                String.format("Recipes added by %s (%s)", userService.findById(id).getUsername(), recipes.getTotalElements()));
        model.addAttribute("url", String.format("/users/profile/%s/addedRecipes", id));

        return "all-recipes";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/favoriteRecipes")
    public String favoriteRecipes(@PathVariable Long id,
                                  Model model,
                                  @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 12) Pageable pageable) {

        Page<RecipeViewModel> recipes = recipeService.findAllFavoriteRecipesForUserId(id, pageable);
        model.addAttribute("recipes", recipes);
        model.addAttribute("heading",
                String.format("Your favorite recipes (%s)", recipes.getTotalElements()));
        model.addAttribute("url", String.format("/users/profile/%s/favoriteRecipes", id));

        return "all-recipes";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/addedPictures")
    public String addedPictures(@PathVariable Long id,
                                Model model,
                                @PageableDefault(page = 0, size = 12) Pageable pageable) {

        String principalUserName = userService.findById(id).getUsername();
        Page<PictureViewModel> pictures = pictureService.findAllPictureViewModelsByUsername(principalUserName, pageable);
        model.addAttribute("pictures", pictures);
        model.addAttribute("heading", String.format("Photos added by %s (%s)", principalUserName, pictures.getTotalElements()));
        return "user-pictures";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}/deletePicture")
    public String deletePicture(@PathVariable("id") Long id, @RequestParam("pictureId") Long pictureId) {
        pictureService.deletePicture(pictureId);
        return "redirect:/users/profile/" + id + "/addedPictures";
    }
}
