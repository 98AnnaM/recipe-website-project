package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.AddRecipeDto;
import bg.example.recepeWebsite.model.dto.EditRecipeDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

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
    public String editUserInformation( @PathVariable("id") Long id,
            Model model){

        if (!model.containsAttribute("userEditDto")){
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
                               @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 1) Pageable pageable) {

        Page<RecipeViewModel> recipes = recipeService.findAllByUserId(id, pageable);
        model.addAttribute("recipes", recipes);
        model.addAttribute("heading",
                String.format("Recipes added by %s (%s)", userService.findById(id).getUsername(), recipes.getTotalElements()));
        model.addAttribute("baseUrl", String.format("/users/profile/%s/addedRecipes", id));

        return "all-recipes";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/addedPictures")
    public String addedPictures(@PathVariable Long id,
                               Model model,
                               @PageableDefault(page = 0, size = 1) Pageable pageable) {

        String principalUserName = userService.findById(id).getUsername();
        Page<PictureViewModel> pictures =  pictureService.findAllPictureViewModelsByUsername(principalUserName ,pageable);
        model.addAttribute("pictures", pictures);
        model.addAttribute("heading", String.format("Photos added by %s (%s)", principalUserName, pictures.getTotalElements()));
        return "user-pictures";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}/deletePicture")
    public String deletePicture(@PathVariable("id") Long id, @RequestParam("pictureId") Long pictureId){
        pictureService.deletePicture(pictureId);
        return "redirect:/users/profile/" + id + "/addedPictures";
    }


}
