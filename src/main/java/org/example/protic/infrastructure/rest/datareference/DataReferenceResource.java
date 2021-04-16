package org.example.protic.infrastructure.rest.datareference;

import org.example.protic.infrastructure.database.mybatis.mappers.CompanyRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.JobTitleRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.TechnologyRecordMapper;
import org.example.protic.infrastructure.database.mybatis.records.TextRecord;
import org.example.protic.infrastructure.rest.ExceptionMapper;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Path("/data")
public class DataReferenceResource {

  private final JobTitleRecordMapper jobTitleRecordMapper;
  private final CompanyRecordMapper companyRecordMapper;
  private final TechnologyRecordMapper technologyRecordMapper;

  @Inject
  public DataReferenceResource(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper) {
    this.jobTitleRecordMapper = jobTitleRecordMapper;
    this.companyRecordMapper = companyRecordMapper;
    this.technologyRecordMapper = technologyRecordMapper;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/job-titles")
  public void getJobTitles(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      @DefaultValue("") @QueryParam("name") String name) {
    TextRecord textRecord = new TextRecord();
    textRecord.text = name;
    resolveDataRequest(
        () -> jobTitleRecordMapper.selectByNameValueContaining(textRecord),
        record -> record.nameValue,
        asyncResponse);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/companies")
  public void getCompanies(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      @DefaultValue("") @QueryParam("name") String name) {
    TextRecord textRecord = new TextRecord();
    textRecord.text = name;
    resolveDataRequest(
        () -> companyRecordMapper.selectByNameValueContaining(textRecord),
        record -> record.nameValue,
        asyncResponse);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/technologies")
  public void getTechnologies(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      @DefaultValue("") @QueryParam("name") String name) {
    TextRecord textRecord = new TextRecord();
    textRecord.text = name;
    resolveDataRequest(
        () -> technologyRecordMapper.selectByNameValueContaining(textRecord),
        record -> record.nameValue,
        asyncResponse);
  }

  private static <T> void resolveDataRequest(
      Supplier<List<T>> supplier,
      Function<T, String> mappingFunction,
      AsyncResponse asyncResponse) {
    CompletableFuture.supplyAsync(supplier)
        .thenApply(Collection::stream)
        .thenApply(list -> list.map(mappingFunction))
        .thenApply(list -> list.collect(Collectors.toList()))
        .thenApply(DataResponseDto::of)
        .thenApply(Response::ok)
        .thenApply(Response.ResponseBuilder::build)
        .exceptionally(ExceptionMapper::map)
        .thenAccept(asyncResponse::resume);
  }
}
