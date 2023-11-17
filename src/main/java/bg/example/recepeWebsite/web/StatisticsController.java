package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.model.view.StatisticsViewDto;
import bg.example.recepeWebsite.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class StatisticsController {

        private final RecipeService recipeService;

    public StatisticsController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/statistics")
    public String register(Model model) {

        StatisticsViewDto statisticsViewDto = new StatisticsViewDto()
                .setAllRecipes(this.recipeService.findCountAll())
                .setMeatRecipes(this.recipeService.findCountByCategory(CategoryNameEnum.WITH_MEAT))
                .setMeatRecipes(this.recipeService.findCountByCategory(CategoryNameEnum.VEGETARIAN))
                .setMeatRecipes(this.recipeService.findCountByCategory(CategoryNameEnum.VEGAN))
                .setLocalDateTime(LocalDateTime.now());

        model.addAttribute("statistics", statisticsViewDto);
        return "statistics";
    }


}
