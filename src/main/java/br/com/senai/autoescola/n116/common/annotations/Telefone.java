package br.com.senai.autoescola.n116.common.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(regexp = "\\d{11}", message = "must be exactly 11 digits")
@Digits(integer = 11, fraction = 0)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface Telefone {
    String message() default "must be exactly 11 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
