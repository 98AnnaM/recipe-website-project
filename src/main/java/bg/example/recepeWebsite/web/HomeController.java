package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.service.PictureService;
import bg.example.recepeWebsite.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PictureService pictureService;
    private final RecipeService recipeService;

    public HomeController(PictureService pictureService, RecipeService recipeService) {
        this.pictureService = pictureService;
        this.recipeService = recipeService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "index";
    }
}
