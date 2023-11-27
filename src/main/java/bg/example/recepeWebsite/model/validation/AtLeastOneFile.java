package bg.example.recepeWebsite.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AtLeastOneFileValidator.class)
public @interface AtLeastOneFile {

    String message() default "At least one file must be chosen!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
