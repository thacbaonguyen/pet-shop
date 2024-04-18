package com.petbackend.thbao.exceptions;

import com.petbackend.thbao.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<?>handlingAccessDeniedException(AccessDeniedException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2000);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.FORBIDDEN);
        exceptionResponse.setError("Access denied");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }
    @ExceptionHandler(InvalidAccountException.class)
    ResponseEntity<?>handlingInvalidAccountException(InvalidAccountException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2001);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.CONFLICT);
        exceptionResponse.setError("Invalid account");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }
    @ExceptionHandler(AlreadyExistingException.class)
    ResponseEntity<?>handlingAlreadyExistingException(AlreadyExistingException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2002);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.CONFLICT);
        exceptionResponse.setError("Already existing");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }
    @ExceptionHandler(DataNotFoundException.class)
    ResponseEntity<?>handlingDataNotFoundException(DataNotFoundException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2003);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND);
        exceptionResponse.setError("Not found data");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<?>handlingRuntimeException(RuntimeException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2004);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setError("Runtime");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(InvalidDateException.class)
    ResponseEntity<?>handlingInvalidDateException(InvalidDateException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2005);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setError("Invalid dd/mm/yy");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(InvalidPasswordException.class)
    ResponseEntity<?>handlingInvalidPasswordException(InvalidPasswordException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2006);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setError("Invalid password");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(PermissionDenyException.class)
    ResponseEntity<?>handlingPermissionDenyException(PermissionDenyException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(2007);
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setStatus(HttpStatus.FORBIDDEN);
        exceptionResponse.setError("Permission");
        exceptionResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }
}
