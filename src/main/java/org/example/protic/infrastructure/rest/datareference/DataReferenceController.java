package org.example.protic.infrastructure.rest.datareference;

import org.example.protic.infrastructure.database.mybatis.mappers.CompanyRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.JobTitleRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.TechnologyRecordMapper;
import org.example.protic.infrastructure.database.mybatis.records.TextRecord;
import org.example.protic.infrastructure.rest.ExceptionMapper;
import org.example.protic.infrastructure.rest.RestControllerUtils;
import org.example.protic.infrastructure.rest.RestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
public class DataReferenceController {

  private final JobTitleRecordMapper jobTitleRecordMapper;
  private final CompanyRecordMapper companyRecordMapper;
  private final TechnologyRecordMapper technologyRecordMapper;

  @Autowired
  public DataReferenceController(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper) {
    this.jobTitleRecordMapper = jobTitleRecordMapper;
    this.companyRecordMapper = companyRecordMapper;
    this.technologyRecordMapper = technologyRecordMapper;
  }

  @RequestMapping(
      value = "/job-titles",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> getJobTitles(
      @RequestParam(value = "name", defaultValue = "") String name) {
    TextRecord textRecord = new TextRecord();
    textRecord.text = name;
    return resolveDataRequest(
        () -> jobTitleRecordMapper.selectByNameValueContaining(textRecord),
        record -> record.nameValue);
  }

  @RequestMapping(
      value = "/companies",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> getCompanies(
      @RequestParam(value = "name", defaultValue = "") String name) {
    TextRecord textRecord = new TextRecord();
    textRecord.text = name;
    return resolveDataRequest(
        () -> companyRecordMapper.selectByNameValueContaining(textRecord),
        record -> record.nameValue);
  }

  @RequestMapping(
      value = "/technologies",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> getTechnologies(
      @RequestParam(value = "name", defaultValue = "") String name) {
    TextRecord textRecord = new TextRecord();
    textRecord.text = name;
    return resolveDataRequest(
        () -> technologyRecordMapper.selectByNameValueContaining(textRecord),
        record -> record.nameValue);
  }

  private static <T> CompletableFuture<ResponseEntity<RestDto>> resolveDataRequest(
      Supplier<List<T>> supplier, Function<T, String> mappingFunction) {
    return CompletableFuture.supplyAsync(supplier)
        .thenApply(Collection::stream)
        .thenApply(list -> list.map(mappingFunction))
        .thenApply(list -> list.collect(Collectors.toList()))
        .thenApply(DataResponseDto::of)
        .thenApply(RestDto.class::cast)
        .thenApply(RestControllerUtils::toOkResponse)
        .exceptionally(ExceptionMapper::map);
  }
}
