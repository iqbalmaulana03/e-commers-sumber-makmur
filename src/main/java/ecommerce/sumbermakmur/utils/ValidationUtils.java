package ecommerce.sumbermakmur.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationUtils {

    private final Validator validator;

    public void validate(Object o){
        Set<ConstraintViolation<Object>> validate = validator.validate(o);

        if (!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
    }
}
