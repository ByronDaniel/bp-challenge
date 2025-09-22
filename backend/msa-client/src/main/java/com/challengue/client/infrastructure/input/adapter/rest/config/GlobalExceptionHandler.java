package com.challengue.client.infrastructure.input.adapter.rest.config;

import static com.challengue.client.infrastructure.exception.utils.Constants.BAD_REQUEST;
import static com.challengue.client.infrastructure.exception.utils.Constants.CONFLICT;
import static com.challengue.client.infrastructure.exception.utils.Constants.FIELDS_ARE_NOT_CORRECT;
import static com.challengue.client.infrastructure.exception.utils.Constants.INTERNAL_SERVER_ERROR_AN_UNEXPECTED_ERROR_OCCURRED;
import static com.challengue.client.infrastructure.exception.utils.Constants.NOT_FOUND;
import static com.challengue.client.infrastructure.exception.utils.Constants.UNEXPECTED_ERROR;

import com.challengue.client.infrastructure.exception.ConflictException;
import com.challengue.client.infrastructure.exception.NotFoundException;
import com.challengue.services.server.models.ErrorDto;
import com.challengue.services.server.models.ErrorsDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ErrorsDto> handleWebExchangeBindException(WebExchangeBindException ex) {
    List<ErrorDto> errorDtos = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> buildErrorDto(fieldError.getField(), fieldError.getDefaultMessage()))
        .toList();

    ErrorsDto errorsDto = buildErrorsDto(BAD_REQUEST, FIELDS_ARE_NOT_CORRECT, errorDtos);

    return ResponseEntity.badRequest().body(errorsDto);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorsDto> handleNotFoundException(NotFoundException ex) {
    return buildResponseEntity(NOT_FOUND, ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorsDto> handleConflictException(ConflictException ex) {
    return buildResponseEntity(CONFLICT, ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorsDto> handleIllegalArgumentException(IllegalArgumentException ex) {
    List<ErrorDto> errorDtos = List.of(
        buildErrorDto(FIELDS_ARE_NOT_CORRECT, ex.getMessage())
    );
    ErrorsDto errorsDto = buildErrorsDto(BAD_REQUEST, FIELDS_ARE_NOT_CORRECT, errorDtos);
    return ResponseEntity.badRequest().body(errorsDto);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorsDto> handleGenericException(Exception ex) {
    log.error(UNEXPECTED_ERROR, ex.getMessage(), ex);
    return buildResponseEntity(INTERNAL_SERVER_ERROR_AN_UNEXPECTED_ERROR_OCCURRED,
        ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ErrorsDto> buildResponseEntity(String title, String detail,
      HttpStatus status) {
    ErrorsDto errorsDto = buildErrorsDto(title, detail, null);
    return new ResponseEntity<>(errorsDto, status);
  }

  private ErrorsDto buildErrorsDto(String title, String detail, List<ErrorDto> errors) {
    ErrorsDto dto = new ErrorsDto();
    dto.setTitle(title);
    dto.setDetail(detail);
    dto.setErrors(errors);
    return dto;
  }

  private ErrorDto buildErrorDto(String message, String businessMessage) {
    ErrorDto errorDto = new ErrorDto();
    errorDto.setMessage(message);
    errorDto.setBusinessMessage(businessMessage);
    return errorDto;
  }
}