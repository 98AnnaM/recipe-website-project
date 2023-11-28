package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.dto.HomePageDto;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.model.view.PictureHomePageViewModel;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class HomePageService {

    private final PictureService pictureService;

    public HomePageService(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public HomePageDto initHomePageDto(){
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
        } else if (now.getHour() <= 16) {
            pictures = this.pictureService
                    .getThreeRandomPicturesByRecipeType(TypeNameEnum.DESERT);
            message = "Time for an afternoon coffee! Here are some deserts that can join your cup of coffee!";
        } else {
            pictures = this.pictureService
                    .getThreeRandomPicturesByRecipeType(TypeNameEnum.MAIN_MEAL);
            message = "Time to prepare some dinner! This are our today's suggestions for you!";
        }

        return new HomePageDto().setMessage(message).setPictures(pictures);
    }
}

