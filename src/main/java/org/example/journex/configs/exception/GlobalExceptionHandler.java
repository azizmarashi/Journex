package org.example.journex.configs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = {JournexException.class})
    public ResponseEntity<Object> handelLogicException(JournexException e) {

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        String message = messageSource.getMessage(
                e.getMessage(),
                null,
                LocaleContextHolder.getLocale()
        );

        ExceptionModel exceptionModel =
                new ExceptionModel(
                        message,
                        badRequest,
                        LocalDateTime.now()
                );

        log.error(message);

        return new ResponseEntity<>(exceptionModel, badRequest);
    }
}
