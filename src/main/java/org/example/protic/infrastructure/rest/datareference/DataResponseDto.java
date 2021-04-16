package org.example.protic.infrastructure.rest.datareference;

import java.util.List;

public class DataResponseDto {

  public List<String> data;

  public static DataResponseDto of(List<String> data) {
    DataResponseDto dataResponseDto = new DataResponseDto();
    dataResponseDto.data = data;
    return dataResponseDto;
  }
}
