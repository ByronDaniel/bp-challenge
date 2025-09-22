package com.challenge.movement.infrastructure.exception.utils;

import com.challenge.services.server.models.ErrorDto;
import com.challenge.services.server.models.ErrorsDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionUtils {

  public static ResponseEntity<ErrorsDto> buildResponseEntity(String title, String detail,
      HttpStatus status) {
    ErrorsDto errorsDto = buildErrorsDto(title, detail, null);
    return new ResponseEntity<>(errorsDto, status);
  }

  public static ErrorsDto buildErrorsDto(String title, String detail, List<ErrorDto> errors) {
    ErrorsDto dto = new ErrorsDto();
    dto.setTitle(title);
    dto.setDetail(detail);
    dto.setErrors(errors);
    return dto;
  }

  public static ErrorDto buildErrorDto(String message, String businessMessage) {
    ErrorDto errorDto = new ErrorDto();
    errorDto.setMessage(message);
    errorDto.setBusinessMessage(businessMessage);
    return errorDto;
  }
}
