package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.view.StatisticsViewDto;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class StatisticsController {

    private final RecipeService recipeService;
    private final UserService userService;

    public StatisticsController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/statistics")
    public String register(Model model) {

        StatisticsViewDto statisticsViewDto = new StatisticsViewDto()
                .setAllRecipes(this.recipeService.findCountAll())
                .setMeatRecipes(this.recipeService.findCountByCategory(CategoryNameEnum.WITH_MEAT))
                .setVegetarianRecipes(this.recipeService.findCountByCategory(CategoryNameEnum.VEGETARIAN))
                .setVeganRecipes(this.recipeService.findCountByCategory(CategoryNameEnum.VEGAN))
                .setUsersCount(this.userService.getCountRegisteredUsers())
                .setLocalDateTime(LocalDateTime.now());

        model.addAttribute("statistics", statisticsViewDto);
        return "statistics";
    }
}
