package bg.example.recepeWebsite.web;

import bg.example.recepeWebsite.model.dto.forgotten_password.ResetPasswordEmailDTO;
import bg.example.recepeWebsite.service.EmailService;
import bg.example.recepeWebsite.service.SecureTokenService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
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
        model.addAttribute("success", "Email with reset password link was sent to " + emailDTO.getEmail());

        return "reset-password-success";
    }




}
