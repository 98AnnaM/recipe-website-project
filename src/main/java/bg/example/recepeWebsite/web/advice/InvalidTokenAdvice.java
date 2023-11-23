package bg.example.recepeWebsite.web.advice;

import bg.example.recepeWebsite.web.exception.InvalidTokenException;
import bg.example.recepeWebsite.web.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidTokenAdvice {

    @ExceptionHandler(InvalidTokenException.class)
    public String handleInvalidTokenException(InvalidTokenException ex, Model model) {

        ex.printStackTrace();
        model.addAttribute("message", ex.getMessage());
        return "message";
    }
}
