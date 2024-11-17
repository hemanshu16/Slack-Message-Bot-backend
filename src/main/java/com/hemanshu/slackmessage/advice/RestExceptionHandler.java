package com.hemanshu.slackmessage.advice;

import com.hemanshu.slackmessage.dto.ErrorMessageDTO;
import com.hemanshu.slackmessage.exception.BadRequestException;
import com.hemanshu.slackmessage.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value =  {BadRequestException.class,NotFoundException.class})
    protected ResponseEntity<ErrorMessageDTO> handleException(Exception ex, HttpServletRequest request) {

        ErrorMessageDTO errorResponseDTO = ErrorMessageDTO.builder()
                .path(request.getRequestURI())
                .errorMessage(ex.getMessage())
                .build();

        return new ResponseEntity<>(
                errorResponseDTO,
                (ex instanceof BadRequestException) ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND
        );
    }
}
