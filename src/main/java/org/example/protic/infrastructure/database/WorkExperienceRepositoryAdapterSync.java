package org.example.protic.infrastructure.database;

import org.example.protic.commons.UuidAdapter;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.database.mybatis.mappers.*;
import org.example.protic.infrastructure.database.mybatis.records.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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

  public FilteredWorkExperience findById(UUID id) {
    WorkExperienceRecord workExperienceQuery = new WorkExperienceRecord();
    workExperienceQuery.idWorkExperience = UuidAdapter.getBytesFromUUID(id);
    WorkExperienceRecord workExperienceResult =
        expectOne(workExperienceRecordMapper.selectById(workExperienceQuery));
    FilteredWorkExperienceImpl.Builder builder = FilteredWorkExperienceImpl.builder();
    if (workExperienceResult.visibilityJobTitle) {
      builder.withJobTitle(findJobTitleById(workExperienceResult.idJobTitle));
    }
    if (workExperienceResult.visibilityCompany) {
      builder.withCompany(findCompanyById(workExperienceResult.idCompany));
    }
    if (workExperienceResult.visibilityTechnologies) {
      builder.withTechnologies(
          findTechnologiesByWorkExperienceId(workExperienceResult.idWorkExperience));
    }
    if (workExperienceResult.visibilityWorkPeriod) {
      WorkPeriod.Builder from = WorkPeriod.from(workExperienceResult.startDate.toLocalDate());
      builder.withWorkPeriod(
          Optional.ofNullable(workExperienceResult.endDate)
              .map(date -> from.to(date.toLocalDate()))
              .orElse(from.toPresent()));
    }
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
