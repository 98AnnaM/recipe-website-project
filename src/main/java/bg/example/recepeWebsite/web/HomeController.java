package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.model.view.PictureHomePageViewModel;
import bg.example.recepeWebsite.service.PictureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalTime;
import java.util.List;

@Controller
public class HomeController {

    private final PictureService pictureService;

    public HomeController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<PictureHomePageViewModel> pictures;
        String message = "";

        LocalTime now = LocalTime.now();
        if (now.getHour() < 11) {
            pictures = this.pictureService
                    .getThreeRandomPicturesByRecipeType(TypeNameEnum.BREAKFAST);
            message = "Time to prepare your breakfast! This are our today's suggestions for you!";
        } else if (now.getHour() <= 14) {
            pictures = this.pictureService
                    .getThreeRandomPicturesByRecipeType(TypeNameEnum.MAIN_MEAL);
            message = "Time to prepare some lunch! This are our today's suggestions for you!";
        } else if (now.getHour() == 15 || now.getHour() == 16) {
            pictures = this.pictureService
                    .getThreeRandomPicturesByRecipeType(TypeNameEnum.DESERT);
            message = "Time for an afternoon coffee! Here are some deserts that can join your cup of coffee!";
        } else {
            pictures = this.pictureService
                    .getThreeRandomPicturesByRecipeType(TypeNameEnum.MAIN_MEAL);
            message = "Time to prepare some dinner! This are our today's suggestions for you!";
        }

        model.addAttribute("pictures", pictures);
        model.addAttribute("message", message);

        return "index";
    }
}
