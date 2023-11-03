package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.UserRegisterDto;
import bg.example.recepeWebsite.service.UserService;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserRegisterController {

    private final UserService userService;


    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegisterDto")
    public UserRegisterDto initUserModel(Model model) {
        return new UserRegisterDto();
    }


    @GetMapping("/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid UserRegisterDto userRegisterDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("userRegisterDto", userRegisterDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDto"
                            , bindingResult);

            return "redirect:register";
        }

        userService.register(userRegisterDto);
        return "redirect:/";
    }
}
