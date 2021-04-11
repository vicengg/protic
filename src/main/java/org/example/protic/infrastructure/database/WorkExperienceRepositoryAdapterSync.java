package org.example.protic.infrastructure.database;

import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.database.mybatis.mappers.CompanyRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.JobTitleRecordMapper;
import org.example.protic.infrastructure.database.mybatis.mappers.TechnologyRecordMapper;
import org.example.protic.infrastructure.database.mybatis.records.CompanyRecord;
import org.example.protic.infrastructure.database.mybatis.records.JobTitleRecord;
import org.example.protic.infrastructure.database.mybatis.records.TechnologyRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class WorkExperienceRepositoryAdapterSync {

  private final JobTitleRecordMapper jobTitleRecordMapper;
  private final CompanyRecordMapper companyRecordMapper;
  private final TechnologyRecordMapper technologyRecordMapper;

  public WorkExperienceRepositoryAdapterSync(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper) {
    this.jobTitleRecordMapper =
        Objects.requireNonNull(jobTitleRecordMapper, "Null job title record mapper.");
    this.companyRecordMapper =
        Objects.requireNonNull(companyRecordMapper, "Null company record mapper.");
    this.technologyRecordMapper =
        Objects.requireNonNull(technologyRecordMapper, "Null technology record mapper.");
  }

  @Transactional
  public void create(WorkExperience workExperience) {
    long jobTitleId = createJobTitleIfNotExist(workExperience.getJobTitle().getValue());
    System.out.println("End.");
  }

  public FilteredWorkExperience findById(UUID id) {
    return null;
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
}
