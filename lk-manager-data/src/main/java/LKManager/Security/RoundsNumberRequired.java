package LKManager.Security;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RoundsNumberValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RoundsNumberRequired {
    String message() default "Nie podałeś liczby rund w systemie szwajcarskim.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
