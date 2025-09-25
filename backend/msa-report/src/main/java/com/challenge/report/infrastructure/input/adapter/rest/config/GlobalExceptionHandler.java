package com.challenge.report.infrastructure.input.adapter.rest.config;

import com.challenge.report.infrastructure.exception.NotFoundException;
import com.challenge.services.server.models.ErrorDto;
import com.challenge.services.server.models.ErrorsDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<ErrorsDto> handleWebClientResponseException(WebClientResponseException ex) {
    ErrorsDto errorsDto = ex.getResponseBodyAs(ErrorsDto.class);
    return ResponseEntity.status(ex.getStatusCode()).body(errorsDto);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ErrorsDto> handleWebExchangeBindException(
      WebExchangeBindException exception) {
    List<ErrorDto> errorDtos = exception.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> {
          ErrorDto errorDto = new ErrorDto();
          errorDto.setMessage(fieldError.getField());
          errorDto.setBusinessMessage(fieldError.getDefaultMessage());
          return errorDto;
        }).toList();

    ErrorsDto errorsDto = new ErrorsDto();
    errorsDto.setTitle("Bad Request");
    errorsDto.setDetail("The input fields are not correct");
    errorsDto.setErrors(errorDtos);
    return new ResponseEntity<>(
        errorsDto
        , HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<ErrorsDto> handleNotFoundException(NotFoundException exception) {
    ErrorsDto errorsDto = new ErrorsDto();
    errorsDto.setTitle("Not Found");
    errorsDto.setDetail(exception.getMessage());
    return new ResponseEntity<>(errorsDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorsDto> handleGenericException(Exception exception) {
    ErrorsDto errorsDto = new ErrorsDto();
    errorsDto.setTitle("Internal Server Error: An unexpected error occurred");
    errorsDto.setDetail(exception.getMessage());
    return new ResponseEntity<>(
        errorsDto
        , HttpStatus.INTERNAL_SERVER_ERROR);
  }
}