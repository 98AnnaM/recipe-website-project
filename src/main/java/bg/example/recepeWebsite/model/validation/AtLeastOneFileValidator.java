package bg.example.recepeWebsite.model.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class AtLeastOneFileValidator implements ConstraintValidator<AtLeastOneFile, List<MultipartFile>> {
    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext constraintValidatorContext) {
        return files != null && !files.isEmpty() && files.stream().anyMatch(file -> !file.isEmpty());
    }
}
