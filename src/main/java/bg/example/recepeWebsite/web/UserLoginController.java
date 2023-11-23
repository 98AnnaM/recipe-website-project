package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserLoginController {

    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth-login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute("username") String username,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("username", username);
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");

        return "redirect:/users/login";
    }
}

