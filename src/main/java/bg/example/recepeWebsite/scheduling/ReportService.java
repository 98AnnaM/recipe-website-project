package bg.example.recepeWebsite.scheduling;

import bg.example.recepeWebsite.model.entity.enums.CategoryNameEnum;
import bg.example.recepeWebsite.service.EmailService;
import bg.example.recepeWebsite.service.RecipeService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ReportService {

    private static final String REPORT_TITLE = "This is autogenerated message from your application.";
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReportService.class);
    private final RecipeService recipeService;
    private final EmailService emailService;
    private final UserService userService;

    public ReportService(RecipeService recipeService, EmailService emailService, UserService userService) {
        this.recipeService = recipeService;
        this.emailService = emailService;
        this.userService = userService;
    }

    public void generateDailyReport() {
        LocalDate today = LocalDate.now();
        Instant todayMidnight = today.atStartOfDay(ZoneOffset.UTC).toInstant();

        log.info("Start generating report");

        String result = String.format("""
                        Report on date: %s
                            Recipes vegan: %d
                            Recipes meat: %d
                            Recipes vegetarian: %d
                            Recipes all: %d
                        """, todayMidnight,
                recipeService.findCountByCategory(CategoryNameEnum.VEGAN),
                recipeService.findCountByCategory(CategoryNameEnum.WITH_MEAT),
                recipeService.findCountByCategory(CategoryNameEnum.VEGETARIAN),
                recipeService.findCountAll()
        );
        List<String> adminsEmails = this.userService.getAdminsEmails();
        for (String email : adminsEmails) {
            log.info(String.format("Report is sent to %s", email));
            emailService.sendSimpleMessage(email, REPORT_TITLE, result);
        }
    }
}
