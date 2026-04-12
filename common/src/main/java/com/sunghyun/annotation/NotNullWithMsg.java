package com.sunghyun.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@NotNull(message="{common.notnull}")
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNullWithMsg {
    String message() default "{common.notnull}"; //@NotBlankWithMsg
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
