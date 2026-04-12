package com.sunghyun.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;

@NotBlank(message="{common.notblank}")
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = {}) // "나 검증용이야!"
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotBlankWithMsg {
    String message() default "{common.notblank}"; //@NotBlankWithMsg
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
