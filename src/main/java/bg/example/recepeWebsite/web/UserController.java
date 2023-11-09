package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.view.RecipeViewModel;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.data.domain.Page;
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

    public UserController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
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

        Page<RecipeViewModel> pagesWithRecipes =  recipeService.findAllByUserId(id, pageable);
        model.addAttribute("recipes", pagesWithRecipes);
        model.addAttribute("heading", String.format("Recipes added by %s", userService.findById(id).getUsername()));

        return "all-recipes";
    }

}
