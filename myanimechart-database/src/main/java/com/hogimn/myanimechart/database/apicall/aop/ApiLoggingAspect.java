package com.hogimn.myanimechart.database.apicall.aop;

import com.hogimn.myanimechart.database.apicall.service.ApiCallLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ApiLoggingAspect {
    private final ApiCallLogService apiCallLogService;

    public ApiLoggingAspect(ApiCallLogService apiCallLogService) {
        this.apiCallLogService = apiCallLogService;
    }

    @Around("@annotation(apiLoggable)")
    public Object logApiCall(ProceedingJoinPoint joinPoint, ApiLoggable apiLoggable) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String endpoint = request.getRequestURI();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        String country = request.getLocale().getCountry();

        apiCallLogService.saveLog(endpoint, method, ip, country);

        return joinPoint.proceed();
    }
}
