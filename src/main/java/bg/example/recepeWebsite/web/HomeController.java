package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.HomePageDto;
import bg.example.recepeWebsite.service.HomePageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.scheduling.annotation.Scheduled;

@Controller
public class HomeController {

    private final HomePageService homePageService;
    private HomePageDto homePageDto;

    public HomeController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }


    @GetMapping("/")
    public String index(Model model) {

        if (homePageDto == null) {
            homePageDto = homePageService.initHomePageDto();
        }

        model.addAttribute("pictures", homePageDto.getPictures());
        model.addAttribute("message", homePageDto.getMessage());

        return "index";
    }

    @Scheduled(cron = "0 0 0,11,15,17 * * ?") // Execute at 00:00, 11:00, 15:00, and 17:00 every day
    public void scheduleInitHomePageDto() {
        homePageDto = homePageService.initHomePageDto();  // Update the instance
    }
}



