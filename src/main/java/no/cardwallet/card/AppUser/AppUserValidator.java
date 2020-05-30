package no.cardwallet.card.AppUser;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AppUserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AppUser.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        AppUser appUser = (AppUser) object;
        ValidationUtils.rejectIfEmpty(errors, "email", "email.empty", "Please enter a valid email address.");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Please enter a valid password.");
        validatePassword(appUser.getPassword(), appUser.getRepeatPassword(), errors);
    }

    public void validatePassword(String password, String repeatPassword, Errors errors) {
        if (!password.equals(repeatPassword)) {
            errors.rejectValue("repeatPassword", "password.notEqual", "Passwords not equal.");
        }
    }
}
