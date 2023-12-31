package bg.example.recepeWebsite.web.advice;

import bg.example.recepeWebsite.web.exception.InvalidTokenException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidTokenAdvice {

    @ExceptionHandler(InvalidTokenException.class)
    public String handleInvalidTokenException(InvalidTokenException ex, Model model) {

        ex.printStackTrace();
        model.addAttribute("message", ex.getMessage());
        return "message";
    }
}
