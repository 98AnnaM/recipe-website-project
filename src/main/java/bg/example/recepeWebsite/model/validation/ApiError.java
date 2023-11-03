package bg.example.recepeWebsite.model.validation;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ApiError {

    private final HttpStatus status;
    private List<String> fieldWithErrors = new ArrayList<>();

    public ApiError(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getFieldWithErrors() {
        return fieldWithErrors;
    }

    public void setFieldWithErrors(List<String> fieldWithErrors) {
        this.fieldWithErrors = fieldWithErrors;
    }

    public void addFieldWithError(String error){
        fieldWithErrors.add(error);
    }
}
