package com.SocialMedia.Social.Media.Platform.project.AOP;

import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Slf4j
@Component
public class LoggingAspects
{
    @Pointcut("execution(* com.SocialMedia.Social.Media.Platform.project.Service.*.*(..))")
    public  void ServiceMethod(){};

    @Around("ServiceMethod()")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();
        long executionTime = stopWatch.getTotalTimeMillis();
        //not hardcoded
        if (executionTime > 300) {
            log.warn("Performance : {}.{}() took {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    executionTime);
        }

        return result;
    }
    @After("ServiceMethod()")
    public void logMethodCompletion(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("Execution of {}.{} completed", className, methodName);
    }

    @Before("ServiceMethod()")
    public void logMethodStart(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("Execution of {}.{} started", className, methodName);
    }
}
