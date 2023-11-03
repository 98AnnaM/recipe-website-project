package bg.example.recepeWebsite.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddCommentDto {


    @NotBlank
    @Size(min = 10)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
