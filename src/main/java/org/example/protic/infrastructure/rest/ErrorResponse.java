package org.example.protic.infrastructure.rest;

public class ErrorResponse implements RestDto {

  public enum ErrorCode {
    GENERIC_ERROR
  }

  public ErrorCode errorCode;
  public String message;
}
