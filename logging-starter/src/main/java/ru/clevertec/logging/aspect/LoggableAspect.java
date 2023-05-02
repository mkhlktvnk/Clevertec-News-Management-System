package ru.clevertec.logging.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggableAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggableAspect.class);

    @Before("loggableMethods()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Method {} called with args {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(value = "loggableMethods()", returning = "retVal", argNames = "retVal,joinPoint")
    public void logAfterReturning(Object retVal, JoinPoint joinPoint) {
        LOGGER.info("Method {} returned {}", joinPoint.getSignature().getName(), retVal);
    }

    @AfterThrowing(value = "loggableMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        LOGGER.warn(
                "Method {} threw an exception {} with message {}",
                joinPoint.getSignature().getName(),
                exception.getClass().getName(),
                exception.getMessage()
        );
    }

    @Pointcut("@annotation(ru.clevertec.logging.annotation.Loggable)")
    private void loggableMethods() {
    }

}
