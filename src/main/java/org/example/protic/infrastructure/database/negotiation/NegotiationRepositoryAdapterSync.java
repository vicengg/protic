package org.example.protic.infrastructure.database.negotiation;

import org.example.protic.commons.UuidAdapter;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.negotiation.NegotiationState;
import org.example.protic.domain.negotiation.Visibility;
import org.example.protic.domain.negotiation.VisibilityRequest;
import org.example.protic.infrastructure.database.mybatis.mappers.NegotiationRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.RequestedDataRecordMapper;
import org.example.protic.infrastructure.database.mybatis.records.NegotiationRecord;
import org.example.protic.infrastructure.database.mybatis.records.RequestedDataRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

public class NegotiationRepositoryAdapterSync {

  private final RequestedDataRecordMapper requestedDataRecordMapper;
  private final NegotiationRecordMapper negotiationRecordMapper;

  public NegotiationRepositoryAdapterSync(
      RequestedDataRecordMapper requestedDataRecordMapper,
      NegotiationRecordMapper negotiationRecordMapper) {
    this.requestedDataRecordMapper =
        Objects.requireNonNull(requestedDataRecordMapper, "Null requested data record mapper.");
    this.negotiationRecordMapper =
        Objects.requireNonNull(negotiationRecordMapper, "Null negotiation record mapper.");
  }

  @Transactional
  public void create(Negotiation negotiation) {
    NegotiationRecord negotiationRecord = new NegotiationRecord();
    negotiationRecord.idNegotiation = UuidAdapter.getBytesFromUUID(negotiation.getId());
    negotiationRecord.createdAt = negotiation.getCreatedAt();
    negotiationRecord.idOfferedData =
        requestedDataRecordMapper.insert(
            toRequestedDataRecord(
                negotiation.getOfferedWorkExperienceId(), negotiation.getOfferedData()));
    negotiationRecord.idDemandedData =
        requestedDataRecordMapper.insert(
            toRequestedDataRecord(
                negotiation.getDemandedWorkExperienceId(), negotiation.getDemandedData()));
    negotiationRecord.state = negotiation.getState().name();
    checkOneModification(negotiationRecordMapper.insert(negotiationRecord));
  }

  @Transactional
  public void update(Negotiation negotiation) {
    NegotiationRecord negotiationQuery = new NegotiationRecord();
    negotiationQuery.idNegotiation = UuidAdapter.getBytesFromUUID(negotiation.getId());
    NegotiationRecord negotiationResult =
        expectOne(negotiationRecordMapper.selectById(negotiationQuery));
    negotiationResult.state = negotiation.getState().name();
    expectOne(negotiationRecordMapper.update(negotiationResult));
    RequestedDataRecord offeredRequestedDataRecord =
        toRequestedDataRecord(
            negotiation.getOfferedWorkExperienceId(), negotiation.getOfferedData());
    offeredRequestedDataRecord.idRequestedData = negotiationResult.idOfferedData;
    expectOne(requestedDataRecordMapper.update(offeredRequestedDataRecord));
    RequestedDataRecord demandedRequestedDataRecord =
        toRequestedDataRecord(
            negotiation.getDemandedWorkExperienceId(), negotiation.getDemandedData());
    demandedRequestedDataRecord.idRequestedData = negotiationResult.idDemandedData;
    expectOne(requestedDataRecordMapper.update(demandedRequestedDataRecord));
  }

  public Negotiation find(UUID id) {
    NegotiationRecord negotiationQuery = new NegotiationRecord();
    negotiationQuery.idNegotiation = UuidAdapter.getBytesFromUUID(id);
    NegotiationRecord negotiationResult =
        expectOne(negotiationRecordMapper.selectById(negotiationQuery));
    NegotiationAdapterDto negotiation = new NegotiationAdapterDto();
    negotiation.id = UuidAdapter.getUUIDFromBytes(negotiationResult.idNegotiation);
    negotiation.createdAt = negotiationResult.createdAt;
    negotiation.state = NegotiationState.of(negotiationResult.state);

    RequestedDataRecord offeredDataQuery = new RequestedDataRecord();
    offeredDataQuery.idRequestedData = negotiationQuery.idOfferedData;
    RequestedDataRecord offeredDataResult =
        expectOne(requestedDataRecordMapper.selectById(offeredDataQuery));
    negotiation.offeredWorkExperienceId =
        UuidAdapter.getUUIDFromBytes(offeredDataResult.idWorkExperience);
    negotiation.offeredData = toVisibilityRequest(offeredDataResult);

    RequestedDataRecord demandedDataQuery = new RequestedDataRecord();
    offeredDataQuery.idRequestedData = negotiationQuery.idDemandedData;
    RequestedDataRecord demandedDataResult =
        expectOne(requestedDataRecordMapper.selectById(demandedDataQuery));
    negotiation.demandedWorkExperienceId =
        UuidAdapter.getUUIDFromBytes(demandedDataResult.idWorkExperience);
    negotiation.demandedData = toVisibilityRequest(demandedDataResult);

    return negotiation;
  }

  private static RequestedDataRecord toRequestedDataRecord(
      UUID workExperienceId, VisibilityRequest visibilityRequest) {
    RequestedDataRecord record = new RequestedDataRecord();
    record.idWorkExperience = UuidAdapter.getBytesFromUUID(workExperienceId);
    record.jobTitle = visibilityRequest.getJobTitle().name();
    record.company = visibilityRequest.getCompany().name();
    record.technologies = visibilityRequest.getTechnologies().name();
    record.workPeriod = visibilityRequest.getWorkPeriod().name();
    record.salary = visibilityRequest.getSalary().name();
    return record;
  }

  private static VisibilityRequest toVisibilityRequest(RequestedDataRecord record) {
    return VisibilityRequest.builder()
        .withJobTitle(Visibility.of(record.jobTitle))
        .withCompany(Visibility.of(record.company))
        .withTechnologies(Visibility.of(record.technologies))
        .withWorkPeriod(Visibility.of(record.workPeriod))
        .withSalary(Visibility.of(record.salary))
        .build();
  }

  private static void checkOneModification(long insertedRecords) {
    if (insertedRecords != 1) {
      // TODO: Change this.
      throw new RuntimeException("Insertion failed.");
    }
  }

  private static <T> T expectOne(T result) {
    if (Objects.isNull(result)) {
      // TODO: Change this.
      throw new RuntimeException("No result.");
    }
    return result;
  }
}
