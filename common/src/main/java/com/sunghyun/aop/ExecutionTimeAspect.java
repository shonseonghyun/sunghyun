package com.sunghyun.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

//    @Pointcut("execution(* com.sunghyun..presentation..*Controller.*(..))")
//    private void controllerMethods() {}
//
//    @Before("controllerMethods()")
//    public void start() {
//        log.info("시작: {}ms", System.currentTimeMillis());
//    }
//
//    @After("controllerMethods()")
//    public void end() {
//        log.info("종료: {}ms", System.currentTimeMillis());
//    }
//
//    @Around("controllerMethods()")
//    public Object logParameter(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("{}.{}",
//                joinPoint.getTarget().getClass().getSimpleName(),
//                joinPoint.getSignature().getName()
//        );
//        log.info("Args:{}", Arrays.toString(joinPoint.getArgs()));
//
//        //실제 메소드 실행
//        return joinPoint.proceed();
//    }

    @Pointcut("execution(* com.sunghyun..presentation..*Controller.*(..))")
    private void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 실행 전 정보 로그 출력
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("▶ {}.{} | Args: {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        // 2. 시작 시간 기록 (밀리초 단위)
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            // 🔥 실제 컨트롤러 메서드 실행
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            // 예외가 발생하더라도 종료 시간과 소요 시간은 찍히도록 예외를 다시 던져줍니다.
            long endTime = System.currentTimeMillis();
            double duration = (endTime - startTime) / 1000.0;
            log.error("X [AOP 에러 발생] {}.{} | 소요시간: {}초", className, methodName, String.format("%.3f", duration));
            throw throwable;
        }

        // 3. 종료 시간 기록 및 소요 시간 계산
        long endTime = System.currentTimeMillis();

        // 밀리초 차이를 구한 뒤 1000.0으로 나누어 초(s) 단위 소수점으로 변환
        double duration = (endTime - startTime) / 1000.0;

        // 4. 소수점 셋째 자리까지 포맷팅하여 출력 (ex: 3.XXX초)
        log.info("■ {}.{} | 소요시간: {}초", className, methodName, String.format("%.3f", duration));

        return result;
    }

}
