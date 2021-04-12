package org.example.protic.infrastructure.database.workexperience;

import org.apache.commons.collections4.ListUtils;
import org.example.protic.commons.UuidAdapter;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.database.mybatis.mappers.*;
import org.example.protic.infrastructure.database.mybatis.records.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class WorkExperienceRepositoryAdapterSync {

  private final JobTitleRecordMapper jobTitleRecordMapper;
  private final CompanyRecordMapper companyRecordMapper;
  private final TechnologyRecordMapper technologyRecordMapper;
  private final WorkExperienceRecordMapper workExperienceRecordMapper;
  private final WorkExperienceTechnologyRecordMapper workExperienceTechnologyRecordMapper;

  public WorkExperienceRepositoryAdapterSync(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper,
      WorkExperienceRecordMapper workExperienceRecordMapper,
      WorkExperienceTechnologyRecordMapper workExperienceTechnologyRecordMapper) {
    this.jobTitleRecordMapper =
        Objects.requireNonNull(jobTitleRecordMapper, "Null job title record mapper.");
    this.companyRecordMapper =
        Objects.requireNonNull(companyRecordMapper, "Null company record mapper.");
    this.technologyRecordMapper =
        Objects.requireNonNull(technologyRecordMapper, "Null technology record mapper.");
    this.workExperienceRecordMapper =
        Objects.requireNonNull(workExperienceRecordMapper, "Null work experience record mapper.");
    this.workExperienceTechnologyRecordMapper =
        Objects.requireNonNull(
            workExperienceTechnologyRecordMapper, "Null work experience technology record mapper.");
  }

  @Transactional
  public void create(WorkExperience workExperience) {
    WorkExperienceRecord workExperienceRecord = new WorkExperienceRecord();
    workExperienceRecord.idWorkExperience = UuidAdapter.getBytesFromUUID(workExperience.getId());
    workExperienceRecord.createdAt = workExperience.getCreatedAt();
    workExperienceRecord.userId = workExperience.getUserId().getValue();
    workExperienceRecord.binding = workExperience.getBinding();
    workExperienceRecord.idJobTitle =
        createJobTitleIfNotExist(workExperience.getJobTitle().getValue());
    workExperienceRecord.visibilityJobTitle = workExperience.getJobTitle().isPublic();
    workExperienceRecord.idCompany =
        createCompanyIfNotExist(workExperience.getCompany().getValue());
    workExperienceRecord.visibilityCompany = workExperience.getCompany().isPublic();
    workExperienceRecord.visibilityTechnologies = workExperience.getTechnologies().isPublic();
    workExperienceRecord.startDate =
        Date.valueOf(workExperience.getWorkPeriod().getValue().getStartDate());
    workExperienceRecord.endDate =
        workExperience.getWorkPeriod().getValue().getEndDate().map(Date::valueOf).orElse(null);
    workExperienceRecord.visibilityWorkPeriod = workExperience.getWorkPeriod().isPublic();
    checkInsertion(workExperienceRecordMapper.insert(workExperienceRecord));
    insertTechnologies(workExperience.getId(), workExperience.getTechnologies().getValue());
  }

  public WorkExperience findById(UUID id) {
    WorkExperienceRecord workExperienceQuery = new WorkExperienceRecord();
    workExperienceQuery.idWorkExperience = UuidAdapter.getBytesFromUUID(id);
    WorkExperienceRecord workExperienceResult =
        expectOne(workExperienceRecordMapper.selectById(workExperienceQuery));
    return recoverWorkExperience(workExperienceResult);
  }

  public List<WorkExperience> getByUserId(UserId userId) {
    WorkExperienceRecord workExperienceQuery = new WorkExperienceRecord();
    workExperienceQuery.userId = userId.getValue();
    return ListUtils.emptyIfNull(workExperienceRecordMapper.selectByUserId(workExperienceQuery))
        .stream()
        .map(this::recoverWorkExperience)
        .collect(Collectors.toList());
  }

  private WorkExperienceAdapterImpl recoverWorkExperience(
      WorkExperienceRecord workExperienceRecord) {
    WorkExperienceAdapterImpl.Builder builder = WorkExperienceAdapterImpl.builder();
    JobTitle jobTitle = findJobTitleById(workExperienceRecord.idJobTitle);
    builder.withId(UuidAdapter.getUUIDFromBytes(workExperienceRecord.idWorkExperience));
    builder.withCreatedAt(workExperienceRecord.createdAt);
    builder.withUserId(UserId.of(workExperienceRecord.userId));
    builder.withBinding(workExperienceRecord.binding);
    builder.withJobTitle(
        workExperienceRecord.visibilityJobTitle
            ? WorkExperienceField.ofPublic(jobTitle)
            : WorkExperienceField.ofPrivate(jobTitle));
    Company company = findCompanyById(workExperienceRecord.idCompany);
    builder.withCompany(
        workExperienceRecord.visibilityCompany
            ? WorkExperienceField.ofPublic(company)
            : WorkExperienceField.ofPrivate(company));
    Set<Technology> technologies =
        findTechnologiesByWorkExperienceId(workExperienceRecord.idWorkExperience);
    builder.withTechnologies(
        workExperienceRecord.visibilityTechnologies
            ? WorkExperienceField.ofPublic(technologies)
            : WorkExperienceField.ofPrivate(technologies));
    WorkPeriod.Builder workPeriodStartDate =
        WorkPeriod.from(workExperienceRecord.startDate.toLocalDate());
    WorkPeriod workPeriod =
        Optional.ofNullable(workExperienceRecord.endDate)
            .map(date -> workPeriodStartDate.to(date.toLocalDate()))
            .orElse(workPeriodStartDate.toPresent());
    builder.withWorkPeriod(
        workExperienceRecord.visibilityWorkPeriod
            ? WorkExperienceField.ofPublic(workPeriod)
            : WorkExperienceField.ofPrivate(workPeriod));
    return builder.build();
  }

  private JobTitle findJobTitleById(long id) {
    JobTitleRecord jobTitleQuery = new JobTitleRecord();
    jobTitleQuery.idJobTitle = id;
    JobTitleRecord jobTitleResult = expectOne(jobTitleRecordMapper.selectById(jobTitleQuery));
    return JobTitle.of(jobTitleResult.nameValue);
  }

  private Company findCompanyById(long id) {
    CompanyRecord companyQuery = new CompanyRecord();
    companyQuery.idCompany = id;
    CompanyRecord companyResult = expectOne(companyRecordMapper.selectById(companyQuery));
    return Company.of(companyResult.nameValue);
  }

  private Technology findTechnologyById(long id) {
    TechnologyRecord technologyQuery = new TechnologyRecord();
    technologyQuery.idTechnology = id;
    TechnologyRecord technologyResult =
        expectOne(technologyRecordMapper.selectById(technologyQuery));
    return Technology.of(technologyResult.nameValue);
  }

  private Set<Technology> findTechnologiesByWorkExperienceId(byte[] id) {
    WorkExperienceTechnologyRecord workExperienceTechnologyQuery =
        new WorkExperienceTechnologyRecord();
    workExperienceTechnologyQuery.idWorkExperience = id;
    return workExperienceTechnologyRecordMapper
        .selectByWorkExperienceId(workExperienceTechnologyQuery)
        .stream()
        .map(record -> findTechnologyById(record.idTechnology))
        .collect(Collectors.toSet());
  }

  private void insertTechnologies(UUID idWorkExperience, Set<Technology> technologies) {
    technologies.stream()
        .map(this::createTechnologyIfNotExist)
        .map(
            idTechnology -> {
              WorkExperienceTechnologyRecord workExperienceTechnologyRecord =
                  new WorkExperienceTechnologyRecord();
              workExperienceTechnologyRecord.idWorkExperience =
                  UuidAdapter.getBytesFromUUID(idWorkExperience);
              workExperienceTechnologyRecord.idTechnology = idTechnology;
              return workExperienceTechnologyRecord;
            })
        .map(workExperienceTechnologyRecordMapper::insert)
        .forEach(WorkExperienceRepositoryAdapterSync::checkInsertion);
  }

  private long createJobTitleIfNotExist(JobTitle jobTitle) {
    JobTitleRecord jobTitleRecord = new JobTitleRecord();
    jobTitleRecord.nameValue = jobTitle.getName();
    JobTitleRecord jobTitleResult = jobTitleRecordMapper.selectByNameValue(jobTitleRecord);
    return Optional.ofNullable(jobTitleResult)
        .map(record -> record.idJobTitle)
        .orElseGet(
            () -> {
              checkInsertion(jobTitleRecordMapper.insert(jobTitleRecord));
              return jobTitleRecord.idJobTitle;
            });
  }

  private long createCompanyIfNotExist(Company company) {
    CompanyRecord companyRecord = new CompanyRecord();
    companyRecord.nameValue = company.getName();
    CompanyRecord companyResult = companyRecordMapper.selectByNameValue(companyRecord);
    return Optional.ofNullable(companyResult)
        .map(record -> record.idCompany)
        .orElseGet(
            () -> {
              checkInsertion(companyRecordMapper.insert(companyRecord));
              return companyRecord.idCompany;
            });
  }

  private long createTechnologyIfNotExist(Technology technology) {
    TechnologyRecord technologyRecord = new TechnologyRecord();
    technologyRecord.nameValue = technology.getName();
    TechnologyRecord technologyResult = technologyRecordMapper.selectByNameValue(technologyRecord);
    return Optional.ofNullable(technologyResult)
        .map(record -> record.idTechnology)
        .orElseGet(
            () -> {
              checkInsertion(technologyRecordMapper.insert(technologyRecord));
              return technologyRecord.idTechnology;
            });
  }

  private static void checkInsertion(long insertedRecords) {
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
