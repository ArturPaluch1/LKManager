package LKManager.Security;


import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
    public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
//todo walidacja nie działa
     
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        System.out.println("Validating password: " + password);
        return password != null && password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-+=].*");
    }
}

