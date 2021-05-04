package org.example.protic.infrastructure.database.workexperience;

import org.apache.commons.collections4.ListUtils;
import org.example.protic.application.workexperience.GetWorkExperiencesQuery;
import org.example.protic.commons.UuidAdapter;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.connector.UserConnector;
import org.example.protic.infrastructure.database.mybatis.mappers.*;
import org.example.protic.infrastructure.database.mybatis.records.*;
import org.javamoney.moneta.Money;
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
  private final UserConnector userConnector;

  public WorkExperienceRepositoryAdapterSync(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper,
      WorkExperienceRecordMapper workExperienceRecordMapper,
      WorkExperienceTechnologyRecordMapper workExperienceTechnologyRecordMapper,
      UserConnector userConnector) {
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
    this.userConnector = Objects.requireNonNull(userConnector, "Null user connector.");
  }

  @Transactional
  public void create(WorkExperience workExperience) {
    WorkExperienceRecord workExperienceRecord = toWorkExperienceRecord(workExperience);
    checkOneModification(workExperienceRecordMapper.insert(workExperienceRecord));
    insertTechnologies(workExperience.getId(), workExperience.getTechnologies().getValue());
  }

  public WorkExperience findById(UUID id) {
    WorkExperienceRecord workExperienceQuery = new WorkExperienceRecord();
    workExperienceQuery.idWorkExperience = UuidAdapter.getBytesFromUUID(id);
    WorkExperienceRecord workExperienceResult =
        expectOne(workExperienceRecordMapper.selectById(workExperienceQuery));
    return recoverWorkExperience(workExperienceResult);
  }

  public List<WorkExperience> find(GetWorkExperiencesQuery query) {
    WorkExperienceFilterRecord workExperienceFilters = mapToWorkExperienceFilters(query);
    return ListUtils.emptyIfNull(workExperienceRecordMapper.select(workExperienceFilters)).stream()
        .map(this::recoverWorkExperience)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateWorkExperience(WorkExperience workExperience) {
    WorkExperienceRecord workExperienceRecord = toWorkExperienceRecord(workExperience);
    checkOneModification(workExperienceRecordMapper.update(workExperienceRecord));
    deleteTechnologies(workExperience.getId());
    insertTechnologies(workExperience.getId(), workExperience.getTechnologies().getValue());
  }

  @Transactional
  public void deleteWorkExperience(UUID id) {
    WorkExperienceRecord workExperienceQuery = new WorkExperienceRecord();
    workExperienceQuery.idWorkExperience = UuidAdapter.getBytesFromUUID(id);
    deleteTechnologies(id);
    checkOneModification(workExperienceRecordMapper.delete(workExperienceQuery));
  }

  private WorkExperienceRecord toWorkExperienceRecord(WorkExperience workExperience) {
    WorkExperienceRecord workExperienceRecord = new WorkExperienceRecord();
    workExperienceRecord.idWorkExperience = UuidAdapter.getBytesFromUUID(workExperience.getId());
    workExperienceRecord.createdAt = workExperience.getCreatedAt();
    workExperienceRecord.userId = workExperience.getUser().getId();
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
    workExperienceRecord.visibilitySalary = workExperience.getSalary().isPublic();
    workExperienceRecord.salary = workExperience.getSalary().getValue().getNumberStripped();
    workExperienceRecord.currency =
        workExperience.getSalary().getValue().getCurrency().getCurrencyCode();
    return workExperienceRecord;
  }

  private static WorkExperienceFilterRecord mapToWorkExperienceFilters(
      GetWorkExperiencesQuery query) {
    WorkExperienceFilterRecord filters = new WorkExperienceFilterRecord();
    filters.userId = Optional.ofNullable(query.user.getId()).orElse(null);
    filters.scope =
        Optional.ofNullable(query.scope)
            .map(Enum::name)
            .orElse(GetWorkExperiencesQuery.Scope.ALL.name());
    filters.jobTitle = Optional.ofNullable(query.jobTitle).map(JobTitle::getName).orElse(null);
    filters.company = Optional.ofNullable(query.company).map(Company::getName).orElse(null);
    filters.technologies =
        Optional.ofNullable(query.technologies)
            .map(Collection::stream)
            .map(s -> s.map(Technology::getName))
            .map(s -> s.collect(Collectors.toSet()))
            .orElse(null);
    filters.startDate = Optional.ofNullable(query.startDate).map(Date::valueOf).orElse(null);
    filters.endDate = Optional.ofNullable(query.endDate).map(Date::valueOf).orElse(null);
    filters.minSalary =
        Optional.ofNullable(query.minSalary).map(Money::getNumberStripped).orElse(null);
    filters.maxSalary =
        Optional.ofNullable(query.maxSalary).map(Money::getNumberStripped).orElse(null);
    return filters;
  }

  private WorkExperienceAdapterDto recoverWorkExperience(
      WorkExperienceRecord workExperienceRecord) {
    WorkExperienceAdapterDto workExperience = new WorkExperienceAdapterDto();
    JobTitle jobTitle = findJobTitleById(workExperienceRecord.idJobTitle);
    workExperience.id = UuidAdapter.getUUIDFromBytes(workExperienceRecord.idWorkExperience);
    workExperience.createdAt = workExperienceRecord.createdAt;
    workExperience.user = userConnector.findUserById(workExperienceRecord.userId);
    workExperience.binding = workExperienceRecord.binding;
    workExperience.jobTitle =
        workExperienceRecord.visibilityJobTitle
            ? RestrictedField.ofPublic(jobTitle)
            : RestrictedField.ofPrivate(jobTitle);
    Company company = findCompanyById(workExperienceRecord.idCompany);
    workExperience.company =
        workExperienceRecord.visibilityCompany
            ? RestrictedField.ofPublic(company)
            : RestrictedField.ofPrivate(company);
    Set<Technology> technologies =
        findTechnologiesByWorkExperienceId(workExperienceRecord.idWorkExperience);
    workExperience.technologies =
        workExperienceRecord.visibilityTechnologies
            ? RestrictedField.ofPublic(technologies)
            : RestrictedField.ofPrivate(technologies);
    WorkPeriod.Builder workPeriodStartDate =
        WorkPeriod.from(workExperienceRecord.startDate.toLocalDate());
    WorkPeriod workPeriod =
        Optional.ofNullable(workExperienceRecord.endDate)
            .map(date -> workPeriodStartDate.to(date.toLocalDate()))
            .orElse(workPeriodStartDate.toPresent());
    workExperience.workPeriod =
        workExperienceRecord.visibilityWorkPeriod
            ? RestrictedField.ofPublic(workPeriod)
            : RestrictedField.ofPrivate(workPeriod);
    Money salary = Money.of(workExperienceRecord.salary, workExperienceRecord.currency);
    workExperience.salary =
        workExperienceRecord.visibilitySalary
            ? RestrictedField.ofPublic(salary)
            : RestrictedField.ofPrivate(salary);
    return workExperience;
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

  private void deleteTechnologies(UUID idWorkExperience) {
    WorkExperienceTechnologyRecord record = new WorkExperienceTechnologyRecord();
    record.idWorkExperience = UuidAdapter.getBytesFromUUID(idWorkExperience);
    workExperienceTechnologyRecordMapper.deleteByWorkExperienceId(record);
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
        .forEach(WorkExperienceRepositoryAdapterSync::checkOneModification);
  }

  private long createJobTitleIfNotExist(JobTitle jobTitle) {
    JobTitleRecord jobTitleRecord = new JobTitleRecord();
    jobTitleRecord.nameValue = jobTitle.getName();
    JobTitleRecord jobTitleResult = jobTitleRecordMapper.selectByNameValue(jobTitleRecord);
    return Optional.ofNullable(jobTitleResult)
        .map(record -> record.idJobTitle)
        .orElseGet(
            () -> {
              checkOneModification(jobTitleRecordMapper.insert(jobTitleRecord));
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
              checkOneModification(companyRecordMapper.insert(companyRecord));
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
              checkOneModification(technologyRecordMapper.insert(technologyRecord));
              return technologyRecord.idTechnology;
            });
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
