package com.hogimn.myanimechart.core.common.apicalllog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {
    private final ApiCallLogService apiCallLogService;

    @Around("@within(com.hogimn.myanimechart.core.apicalllog.ApiLoggable) || " +
            "@annotation(com.hogimn.myanimechart.core.apicalllog.ApiLoggable)")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            apiCallLogService.recordApiCall(
                    request.getRequestURI(),
                    request.getMethod(),
                    getClientIpAddr(request)
            );
        }

        return joinPoint.proceed();
    }

    private String getClientIpAddr(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null
                && !xForwardedFor.isBlank()
                && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
