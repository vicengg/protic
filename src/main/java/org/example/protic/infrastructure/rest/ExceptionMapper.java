package org.example.protic.infrastructure.rest;

import javax.ws.rs.core.Response;

public class ExceptionMapper {

  public static Response map(Throwable throwable) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.errorCode = ErrorResponse.ErrorCode.GENERIC_ERROR;
    errorResponse.message = throwable.getMessage();
    return Response.serverError().entity(errorResponse).build();
  }
}
