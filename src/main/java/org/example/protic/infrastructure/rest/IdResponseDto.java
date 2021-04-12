package org.example.protic.infrastructure.rest;

import java.util.UUID;

public class IdResponseDto {

  public String id;

  public static IdResponseDto of(UUID uuid) {
    IdResponseDto idResponseDto = new IdResponseDto();
    idResponseDto.id = uuid.toString();
    return idResponseDto;
  }
}
