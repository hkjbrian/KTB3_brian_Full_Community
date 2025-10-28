package com.community.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class ControllerExecutionTimeAspect {

    private static final String REQUEST_ID_KEY = "requestId";

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime("Controller", joinPoint);
    }

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime("Service", joinPoint);
    }

    private Object logExecutionTime(String layer, ProceedingJoinPoint joinPoint) throws Throwable {
        boolean requestIdCreated = ensureRequestId();
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String typeName = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            String requestId = MDC.get(REQUEST_ID_KEY);

            log.info("[{}][{}] {}.{} executed in {} ms", layer, requestId, typeName, methodName, duration);

            if (requestIdCreated) {
                MDC.remove(REQUEST_ID_KEY);
            }
        }
    }

    private boolean ensureRequestId() {
        if (MDC.get(REQUEST_ID_KEY) != null) {
            return false;
        }

        MDC.put(REQUEST_ID_KEY, UUID.randomUUID().toString());
        return true;
    }
}
