package com.sunghyun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateAble {
    //null 무시한다 == 수정하지 않겠다
    //null 무시하지 않는다 == null인 경우에도 null로 수정한다
    boolean ignoreNull() default true;
}
