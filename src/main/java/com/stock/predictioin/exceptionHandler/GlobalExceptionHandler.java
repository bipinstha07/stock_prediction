package com.stock.predictioin.exceptionHandler;


import com.stock.predictioin.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> errorResponse = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error->{
            String errorMessage = error.getDefaultMessage();
            String field = ((FieldError) error).getField();
            errorResponse.put(field,errorMessage);
        });
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception){
        ErrorResponseDto errorResponse = new ErrorResponseDto("Invalid Data entry or duplication",400,false);
        return  new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception){
        ErrorResponseDto errorResponse= new ErrorResponseDto(exception.getMessage(),400,false);
        return  new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto("Message Not Readable",400,false);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(),400,false);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler( NullPointerException.class)
    public ResponseEntity<ErrorResponseDto> handleNullPointerException( NullPointerException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(),400,false);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestPartException(MissingServletRequestPartException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(),400,false);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(),400,false);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(),400,false);
        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

}
