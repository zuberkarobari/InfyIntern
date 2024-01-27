package com.infy.infyinterns.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.infy.infyinterns.exception.InfyInternException;

@Aspect
@Component
public class LoggingAspect {

    private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);
    static {
        LOGGER.info("Logging Aspect Point Cut");
    }
    @AfterThrowing(pointcut = "execution(* com.infy.infyinterns.service.ProjectAllocationServiceImpl.*(..))", throwing = "exception")
    public void logServiceException(InfyInternException exception) {
        LOGGER.error("Exception caught in ProjectAllocationServiceImpl: " + exception.getMessage(), exception);
    }
    @AfterThrowing(pointcut = "execution(* com.infy.infyinterns.api.ProjectAllocationAPI.*(..))", throwing = "exception")
    public void logapiException(InfyInternException exception) {
        LOGGER.error("Logging Aspect :{} ",exception);
        LOGGER.error("Exception caught in ProjectAllocationServiceImpl: " + exception.getMessage(), exception);
    }
}
