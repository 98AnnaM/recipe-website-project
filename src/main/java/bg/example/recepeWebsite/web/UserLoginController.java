package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {

        model.addAttribute("user", userService.findById(id));

        return "profile";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute("username") String username,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("username", username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }


}

