package com.infy.infyinterns.utility;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.infy.infyinterns.exception.InfyInternException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Component
public class ExceptionControllerAdvice
{

    private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvice.class);

	@Autowired
    private Environment environment;
	@ExceptionHandler(InfyInternException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorInfo> infyInternException(InfyInternException exception) {
		LOGGER.error("infyInternException");
		LOGGER.error(exception.getMessage(), exception);
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		System.out.println("Exception.getMessage : "+exception.getMessage());
		System.out.println(" environment : "+environment.getProperty(exception.getMessage()));
		errorInfo.setErrorMessage(environment.getProperty(exception.getMessage()));
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception)
    {
		LOGGER.error("generalExceptionHandler");
	    LOGGER.error(exception.getMessage(), exception);
	    ErrorInfo errorInfo = new ErrorInfo();
	    errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    errorInfo.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE"));
		LOGGER.error(errorInfo);
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorInfo> validatorExceptionHandler(MethodArgumentNotValidException exception)
    {
		LOGGER.error("validatorExceptionHandler");
	LOGGER.error(exception.getMessage(), exception);
	String errorMsg = "Default";
		errorMsg = exception.getBindingResult()
				    .getAllErrors()
				    .stream()
				    .map(ObjectError::getDefaultMessage)
				    .collect(Collectors.joining(", "));
	ErrorInfo errorInfo = new ErrorInfo();
	errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
	errorInfo.setErrorMessage(errorMsg);
	return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
	@ExceptionHandler(ConstraintViolationException.class)
	public static ResponseEntity<ErrorInfo> pathExceptionHandler(ConstraintViolationException exception) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		String errorMsg = exception.getConstraintViolations().stream().map(x -> x.getMessage())
				.collect(Collectors.joining(", "));
		errorInfo.setErrorMessage(errorMsg);
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
}
