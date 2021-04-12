package org.example.protic.infrastructure.rest.workexperience;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.example.protic.domain.workexperience.WorkPeriod;

import java.time.LocalDate;

public class WorkPeriodDto {

  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonFormat(pattern = "dd/MM/yyyy")
  public LocalDate startDate;

  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonFormat(pattern = "dd/MM/yyyy")
  public LocalDate endDate;

  public static WorkPeriodDto of(WorkPeriod workPeriod) {
    WorkPeriodDto workPeriodDto = new WorkPeriodDto();
    workPeriodDto.startDate = workPeriod.getStartDate();
    workPeriodDto.endDate = workPeriod.getEndDate().orElse(null);
    return workPeriodDto;
  }
}
