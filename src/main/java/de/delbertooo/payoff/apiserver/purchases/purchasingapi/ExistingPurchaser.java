package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ExistingPurchaser.Validator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface ExistingPurchaser {
    String message() default "The name '${validatedValue}' is not a valid purchaser.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Component
    class Validator implements ConstraintValidator<ExistingPurchaser, String> {

        private UsersService usersService;

        @Autowired
        public Validator(UsersService usersService) {
            this.usersService = usersService;
        }

        @Override
        public boolean isValid(String name, ConstraintValidatorContext context) {
            if (name == null) {
                return true;
            }
            return usersService.findPurchaserNames().contains(name);
        }
    }

}