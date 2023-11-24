package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.UserRegisterDto;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserRegisterController {

    private final UserService userService;
    private final LocaleResolver localeResolver;

    public UserRegisterController(UserService userService, LocaleResolver localeResolver) {
        this.userService = userService;
        this.localeResolver = localeResolver;
    }

    @ModelAttribute("userRegisterDto")
    public UserRegisterDto initUserModel() {
        return new UserRegisterDto();
    }

    @GetMapping("/register")
    public String register() {
        return "auth-register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid UserRegisterDto userRegisterDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("userRegisterDto", userRegisterDto)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDto"
                            , bindingResult);

            return "redirect:/users/register";
        }

        userService.register(userRegisterDto, localeResolver.resolveLocale(request));
        return "need-for-verification";
    }

    @GetMapping("/register/verify")
    public String verifyAccount(@RequestParam(required = false) String token, Model model,
                                RedirectAttributes redirectAttributes) {

        if (token == null) {
            model.addAttribute("message", "Token is empty. Please make sure to copy the entire URL");
            return "message";
        }

         userService.verifyAccount(token);
        redirectAttributes.addFlashAttribute("successMessage",
                "Your account was successfully verified. You can now login.");
        return "redirect:/users/login";
    }

    @GetMapping ("/register/sendNewVerificationMail")
    public String sendNewVerificationEmail(@RequestParam("username") String username,
                                           HttpServletRequest request){

        userService.sendVerificationMail(userService.findByUsername(username), localeResolver.resolveLocale(request));
        return "need-for-verification";
    }


}
