package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.forgotten_password.ResetPasswordData;
import bg.example.recepeWebsite.model.dto.forgotten_password.ResetPasswordEmailDTO;
import bg.example.recepeWebsite.service.EmailService;
import bg.example.recepeWebsite.service.SecureTokenService;
import bg.example.recepeWebsite.service.UserService;
import bg.example.recepeWebsite.web.exception.InvalidTokenException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/password")
public class PasswordResetController {

    private final UserService userService;
    private final SecureTokenService secureTokenService;
    private final EmailService emailService;

    public PasswordResetController(UserService userService, SecureTokenService secureTokenService, EmailService emailService) {
        this.userService = userService;
        this.secureTokenService = secureTokenService;
        this.emailService = emailService;
    }

    @ModelAttribute("emailDTO")
    public ResetPasswordEmailDTO resetPasswordEmailDTO() {
        return new ResetPasswordEmailDTO();
    }

    @GetMapping("/reset")
    public String resetPassword() {
        return "reset-password-request";
    }

    @PostMapping("/reset")
    public String sendResetPasswordEmail(@Valid ResetPasswordEmailDTO emailDTO,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {

        if (!userService.emailExists(emailDTO.getEmail())) {
            bindingResult.addError(new FieldError("emailDTO", "email", emailDTO.getEmail(), false, null, null,
                    "User with email " + emailDTO.getEmail() + " does not  exist!"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailDTO", emailDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO",
                    bindingResult);
            return "redirect:/password/reset";
        }

        String resetLink = this.secureTokenService.createSecureTokenResetLink(emailDTO.getEmail());
        this.emailService.sendResetPasswordEmail(emailDTO.getEmail(), resetLink);
        model.addAttribute("message", "Email with reset password link was sent to " + emailDTO.getEmail());

        return "reset-password-message";
    }

    @GetMapping("/change")
    public String changePassword(@RequestParam(required = false) String token,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (token == null) {
            model.addAttribute("message", "Token is empty. Please make sure to copy the entire URL");
            return "reset-password-message";
        }

        if (!model.containsAttribute("data")) {
            ResetPasswordData passwordData = new ResetPasswordData();
            passwordData.setToken(token);
            model.addAttribute("data", passwordData);
        }

        return "change-password";
    }

    @PostMapping("/change")
    public String changePassword(@Valid ResetPasswordData data,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("data", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.data",
                    bindingResult);
            return String.format("redirect:/password/change?token=%s", data.getToken());
        }

        userService.updatePassword(data.getPassword(), data.getToken());

        redirectAttributes.addFlashAttribute("successMessage",
                "Your password was successfully changed. You can now login into your account.");
        return "redirect:/users/login";
    }





}
