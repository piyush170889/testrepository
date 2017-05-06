package com.repleteinc.motherspromise.exception;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.ResponseMessage;

@ControllerAdvice
public class BaseExceptionHandler {
	
  @Autowired
  private Properties responseMessageProperties;

  @Autowired
  private Properties configProperties;
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Object processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    FieldError error = result.getFieldError();

    return processFieldError(error);
  }

  private Object processFieldError(FieldError error) {
    ResponseMessage responseMessage = null;
    BaseWrapper response = null;
    if (error != null) {
      String msg = responseMessageProperties.getProperty(error.getDefaultMessage());
      responseMessage = new ResponseMessage(HttpStatus.PAYMENT_REQUIRED.toString(), msg,
    		  configProperties.getProperty("api.version"));
      response = new BaseWrapper(responseMessage);
    }
    return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
  }
  
}
