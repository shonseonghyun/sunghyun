package com.sunghyun.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class TestAop {

    @Pointcut("execution(* com.sunghyun..presentation..*Controller.*(..))")
    private void controllerMethods() {}

    @Before("controllerMethods()")
    public void start() {
        log.info("시작: {}ms", System.currentTimeMillis());
    }

    @After("controllerMethods()")
    public void end() {
        log.info("종료: {}ms", System.currentTimeMillis());
    }

    @Around("controllerMethods()")
    public Object logParameter(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("{}.{}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName()
        );
        log.info("Args:{}", Arrays.toString(joinPoint.getArgs()));

        //실제 메소드 실행
        return joinPoint.proceed();
    }
}
