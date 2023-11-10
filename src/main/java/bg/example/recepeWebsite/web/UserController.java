package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.service.PictureService;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

        model.addAttribute("user", userService.findById(id));

        return "profile";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/addedRecipes")
    public String addedRecipes(@PathVariable Long id,
                               Model model,
                               @PageableDefault(sort = "name", direction = Sort.Direction.ASC, page = 0, size = 8) Pageable pageable) {

        model.addAttribute("recipes", recipeService.findAllByUserId(id, pageable));
        model.addAttribute("heading", String.format("Recipes added by %s", userService.findById(id).getUsername()));

        return "all-recipes";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/addedPictures")
    public String addedPictures(@PathVariable Long id,
                               Model model,
                               @PageableDefault(page = 0, size = 1) Pageable pageable) {

        model.addAttribute("pictureURLs", pictureService.findAllUrlsByUserId(id, pageable));
        model.addAttribute("heading", String.format("Photos added by %s", userService.findById(id).getUsername()));

        return "user-pictures";
    }

}
