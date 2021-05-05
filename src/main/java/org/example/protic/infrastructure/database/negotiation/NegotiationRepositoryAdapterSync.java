package org.example.protic.infrastructure.database.negotiation;

import org.example.protic.application.negotiation.GetNegotiationsQuery;
import org.example.protic.commons.UuidAdapter;
import org.example.protic.domain.negotiation.Action;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.negotiation.Visibility;
import org.example.protic.domain.negotiation.VisibilityRequest;
import org.example.protic.domain.user.User;
import org.example.protic.infrastructure.connector.UserConnector;
import org.example.protic.infrastructure.database.mybatis.mappers.NegotiationActionRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.NegotiationRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.RequestedDataRecordMapper;
import org.example.protic.infrastructure.database.mybatis.records.NegotiationActionRecord;
import org.example.protic.infrastructure.database.mybatis.records.NegotiationRecord;
import org.example.protic.infrastructure.database.mybatis.records.RequestedDataRecord;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapterSync;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NegotiationRepositoryAdapterSync {

  private final RequestedDataRecordMapper requestedDataRecordMapper;
  private final NegotiationRecordMapper negotiationRecordMapper;
  private final NegotiationActionRecordMapper negotiationActionRecordMapper;
  private final WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapter;
  private final UserConnector userConnector;

  public NegotiationRepositoryAdapterSync(
      RequestedDataRecordMapper requestedDataRecordMapper,
      NegotiationRecordMapper negotiationRecordMapper,
      NegotiationActionRecordMapper negotiationActionRecordMapper,
      WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapter,
      UserConnector userConnector) {
    this.requestedDataRecordMapper =
        Objects.requireNonNull(requestedDataRecordMapper, "Null requested data record mapper.");
    this.negotiationRecordMapper =
        Objects.requireNonNull(negotiationRecordMapper, "Null negotiation record mapper.");
    this.negotiationActionRecordMapper =
        Objects.requireNonNull(
            negotiationActionRecordMapper, "Null negotiation action record mapper.");
    this.workExperienceRepositoryAdapter =
        Objects.requireNonNull(
            workExperienceRepositoryAdapter, "Null work experience repository adapter.");
    this.userConnector = Objects.requireNonNull(userConnector, "Null user connector.");
  }

  @Transactional
  public void create(Negotiation negotiation) {
    NegotiationRecord negotiationRecord = toNegotiationRecord(negotiation);
    checkOneModification(negotiationRecordMapper.insert(negotiationRecord));
  }

  @Transactional
  public void update(Negotiation negotiation) {
    NegotiationRecord negotiationRecord = toNegotiationRecord(negotiation);
    checkOneModification(negotiationRecordMapper.update(negotiationRecord));
    NegotiationActionRecord negotiationActionQuery = new NegotiationActionRecord();
    negotiationActionQuery.idNegotiation = negotiationRecord.idNegotiation;
    int previousActions =
        negotiationActionRecordMapper.selectByNegotiationId(negotiationActionQuery).size();
    List<Action> actions =
        negotiation.getActions().subList(previousActions, negotiation.getActions().size());
    actions.forEach(action -> persistAction(action, negotiation.getId()));
  }

  public Negotiation findById(UUID id) {
    NegotiationRecord negotiationQuery = new NegotiationRecord();
    negotiationQuery.idNegotiation = UuidAdapter.getBytesFromUUID(id);
    NegotiationRecord negotiationRecord =
        expectOne(negotiationRecordMapper.selectById(negotiationQuery));
    return completeNegotiationRecord(negotiationRecord);
  }

  public List<Negotiation> find(GetNegotiationsQuery query) {
    NegotiationRecord negotiationRecord = new NegotiationRecord();
    switch (query.scope) {
      case CREATOR:
        negotiationRecord.creatorId = query.user.getId();
        return negotiationRecordMapper.selectByCreator(negotiationRecord).stream()
            .map(this::completeNegotiationRecord)
            .collect(Collectors.toList());
      case RECEIVER:
        negotiationRecord.receiverId = query.user.getId();
        return negotiationRecordMapper.selectByReceiver(negotiationRecord).stream()
            .map(this::completeNegotiationRecord)
            .collect(Collectors.toList());
      default:
        throw new IllegalStateException("Unknown get negotiations query scope.");
    }
  }

  public List<Negotiation> findByWorkExperienceId(UUID workExperienceId) {
    NegotiationRecord negotiationRecord = new NegotiationRecord();
    negotiationRecord.idOfferedWorkExperience = UuidAdapter.getBytesFromUUID(workExperienceId);
    negotiationRecord.idDemandedWorkExperience = UuidAdapter.getBytesFromUUID(workExperienceId);
    return Stream.concat(
            negotiationRecordMapper.selectByOfferedWorkExperience(negotiationRecord).stream(),
            negotiationRecordMapper.selectByDemandedWorkExperience(negotiationRecord).stream())
        .map(this::completeNegotiationRecord)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteByWorkExperienceId(UUID workExperienceId) {
    NegotiationRecord negotiationQuery = new NegotiationRecord();
    negotiationQuery.idOfferedWorkExperience = UuidAdapter.getBytesFromUUID(workExperienceId);
    negotiationQuery.idDemandedWorkExperience = UuidAdapter.getBytesFromUUID(workExperienceId);
    Stream.concat(
            negotiationRecordMapper.selectByOfferedWorkExperience(negotiationQuery).stream(),
            negotiationRecordMapper.selectByDemandedWorkExperience(negotiationQuery).stream())
        .forEach(
            negotiationRecord -> {
              deleteNegotiationActions(negotiationRecord);
              negotiationRecordMapper.delete(negotiationRecord);
            });
  }

  private void deleteNegotiationActions(NegotiationRecord negotiationRecord) {
    NegotiationActionRecord negotiationActionRecord = new NegotiationActionRecord();
    negotiationActionRecord.idNegotiation = negotiationRecord.idNegotiation;
    negotiationActionRecordMapper.delete(negotiationActionRecord);
  }

  private Negotiation completeNegotiationRecord(NegotiationRecord negotiationRecord) {
    NegotiationAdapterDto negotiation = new NegotiationAdapterDto();
    negotiation.id = UuidAdapter.getUUIDFromBytes(negotiationRecord.idNegotiation);
    negotiation.createdAt = negotiationRecord.createdAt;
    negotiation.offeredWorkExperience =
        workExperienceRepositoryAdapter.findById(
            UuidAdapter.getUUIDFromBytes(negotiationRecord.idOfferedWorkExperience));
    negotiation.demandedWorkExperience =
        workExperienceRepositoryAdapter.findById(
            UuidAdapter.getUUIDFromBytes(negotiationRecord.idDemandedWorkExperience));
    negotiation.creator = userConnector.findUserById(negotiationRecord.creatorId);
    negotiation.receiver = userConnector.findUserById(negotiationRecord.receiverId);
    negotiation.nextActor =
        Optional.ofNullable(negotiationRecord.nextActor)
            .map(userConnector::findUserById)
            .orElse(null);
    NegotiationActionRecord negotiationActionQuery = new NegotiationActionRecord();
    negotiationActionQuery.idNegotiation = negotiationRecord.idNegotiation;
    negotiation.actions =
        negotiationActionRecordMapper.selectByNegotiationId(negotiationActionQuery).stream()
            .map(this::toAction)
            .collect(Collectors.toList());
    return negotiation;
  }

  private Action toAction(NegotiationActionRecord actionRecord) {
    RequestedDataRecord offeredDataRecord = new RequestedDataRecord();
    offeredDataRecord.idRequestedData = actionRecord.idOfferedData;
    VisibilityRequest offeredVisibility =
        toVisibilityRequest(expectOne(requestedDataRecordMapper.selectById(offeredDataRecord)));
    RequestedDataRecord demandedDataRecord = new RequestedDataRecord();
    demandedDataRecord.idRequestedData = actionRecord.idDemandedData;
    VisibilityRequest demandedVisibility =
        toVisibilityRequest(expectOne(requestedDataRecordMapper.selectById(demandedDataRecord)));
    return Action.of(
        Action.Type.of(actionRecord.type),
        userConnector.findUserById(actionRecord.issuerId),
        offeredVisibility,
        demandedVisibility);
  }

  private void persistAction(Action action, UUID negotiationId) {
    RequestedDataRecord offeredDataRecord = toRequestedDataRecord(action.getOfferedVisibility());
    RequestedDataRecord demandedDataRecord = toRequestedDataRecord(action.getDemandedVisibility());
    checkOneModification(requestedDataRecordMapper.insert(offeredDataRecord));
    checkOneModification(requestedDataRecordMapper.insert(demandedDataRecord));
    NegotiationActionRecord negotiationActionRecord = new NegotiationActionRecord();
    negotiationActionRecord.idNegotiation = UuidAdapter.getBytesFromUUID(negotiationId);
    negotiationActionRecord.actionDate = action.getDate();
    negotiationActionRecord.type = action.getType().name();
    negotiationActionRecord.issuerId = action.getIssuer().getId();
    negotiationActionRecord.idOfferedData = offeredDataRecord.idRequestedData;
    negotiationActionRecord.idDemandedData = demandedDataRecord.idRequestedData;
    checkOneModification(negotiationActionRecordMapper.insert(negotiationActionRecord));
  }

  private static NegotiationRecord toNegotiationRecord(Negotiation negotiation) {
    NegotiationRecord negotiationRecord = new NegotiationRecord();
    negotiationRecord.idNegotiation = UuidAdapter.getBytesFromUUID(negotiation.getId());
    negotiationRecord.createdAt = negotiation.getCreatedAt();
    negotiationRecord.idOfferedWorkExperience =
        UuidAdapter.getBytesFromUUID(negotiation.getOfferedWorkExperience().getId());
    negotiationRecord.idDemandedWorkExperience =
        UuidAdapter.getBytesFromUUID(negotiation.getDemandedWorkExperience().getId());
    negotiationRecord.creatorId = negotiation.getCreator().getId();
    negotiationRecord.receiverId = negotiation.getReceiver().getId();
    negotiationRecord.nextActor = negotiation.getNextActor().map(User::getId).orElse(null);
    return negotiationRecord;
  }

  private static RequestedDataRecord toRequestedDataRecord(VisibilityRequest visibilityRequest) {
    RequestedDataRecord record = new RequestedDataRecord();
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
