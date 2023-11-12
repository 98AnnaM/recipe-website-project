package bg.example.recepeWebsite.web.advice;

import bg.example.recepeWebsite.web.exception.InvalidFileException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidFileUploadAdvice {

    @ExceptionHandler(InvalidFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidFileException(InvalidFileException ex, Model model) {
        // Log the exception (you can use a logging framework like SLF4J)
        ex.printStackTrace();

        // Add attributes to the model if needed
        model.addAttribute("errorMessage", ex.getMessage());

        // Return the name of the error page
        return "error/invalidPictureUploadError";
    }
}
