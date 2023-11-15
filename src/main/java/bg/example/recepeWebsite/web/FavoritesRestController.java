package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.user.CustomUserDetails;
import bg.example.recepeWebsite.model.view.CommentViewModel;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FavoritesRestController {

    private final UserService userService;
    private final RecipeService recipeService;

    public FavoritesRestController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/recipes/{id}/addOrRemoveFromFavorites")
    public ResponseEntity<Boolean> getStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(this.recipeService.isRecipeFavorite(principal.getUsername(), id));

    }


    @PostMapping("/api/recipes/{id}/addOrRemoveFromFavorites")
    public ResponseEntity<Boolean> addOrRemoveFromFavorites( @PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails principal){

        if (principal != null) {
            boolean isFavorite = userService.addOrRemoveRecipeFromFavorites(principal.getUsername(), id);
            return ResponseEntity.ok(isFavorite);
        }

        return ResponseEntity.status(403).build();
    }
}
