package bg.example.recepeWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecipeWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeWebsiteApplication.class, args);
    }

}
