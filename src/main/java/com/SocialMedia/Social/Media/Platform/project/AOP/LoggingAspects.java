package com.SocialMedia.Social.Media.Platform.project.AOP;

import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

        if (executionTime > 300) {
            log.warn("PERFORMANCE WARNING: {}.{}() took {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    executionTime);
        }

        return result;
    }
}
