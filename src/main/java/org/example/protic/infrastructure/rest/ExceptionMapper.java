package org.example.protic.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionMapper {

  private ExceptionMapper() {}

  public static ResponseEntity<RestDto> map(Throwable throwable) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.errorCode = ErrorResponse.ErrorCode.GENERIC_ERROR;
    errorResponse.message = throwable.getMessage();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
