package com.hogimn.myanimechart.database.apicalllog.aop;

import com.hogimn.myanimechart.database.apicalllog.service.ApiCallLogService;
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
        String ip = getClientIpAddr(request);
        String country = request.getLocale().getCountry();

        apiCallLogService.saveLog(endpoint, method, ip, country);

        return joinPoint.proceed();
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }
}
