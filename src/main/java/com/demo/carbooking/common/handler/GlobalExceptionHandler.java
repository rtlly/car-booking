package com.demo.carbooking.common.handler;

import com.demo.carbooking.domain.exception.CarHasBeenBookedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.google.common.collect.ImmutableMap;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@ControllerAdvice
@Slf4j
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({CarHasBeenBookedException.class})
    public ResponseEntity<Object> handleEntityExistedException(CarHasBeenBookedException ex) {
        return handleException(ex, CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return handleException(ex, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        return handleException(ex, BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUntreatedException(Exception ex) {
        log.error("internal server error", ex);
        return new ResponseEntity<>(ImmutableMap.of("message", "服务器遇到错误，无法完成请求。"),
                new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleUntreatedThrowable(Throwable throwable) {
        log.error("Unknown server error", throwable);
        return new ResponseEntity<>(ImmutableMap.of("message", "服务器遇到错误，无法完成请求。"),
                new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Object> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return handleException(ex, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        String message;
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            message = fieldError.getField() + " can not be null";
        } else {
            message = "please check necessary query condition can not be not";
        }
        log.error("handle bind exception.", ex);
        return new ResponseEntity<>(ImmutableMap.of("message", message), new HttpHeaders(), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorInfo = ex.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    String errorMessage = objectError.getDefaultMessage();
                    if (objectError instanceof FieldError) {
                        errorMessage = ((FieldError) objectError).getField() + ":" + errorMessage;
                    }
                    return errorMessage;
                })
                .collect(Collectors.joining(";"));
        log.error("Handle method argument not valid!", ex);
        return new ResponseEntity<>(ImmutableMap.of("message", errorInfo), new HttpHeaders(), BAD_REQUEST);
    }

    //重写父类的handleExceptionInternal打出异常日志
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handle exception internal:", ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> handleException(Exception ex, HttpStatus httpStatus) {
        log.error("exception handler", ex);
        return new ResponseEntity<>(ImmutableMap.of("message", ex.getMessage()), new HttpHeaders(), httpStatus);
    }
}
