package bg.example.recepeWebsite.web.advice;

import bg.example.recepeWebsite.web.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ObjectNotFoundAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleObjectNotFoundException(ObjectNotFoundException ex, Model model) {

        ex.printStackTrace();
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/objectNotFoundError";
    }
}



