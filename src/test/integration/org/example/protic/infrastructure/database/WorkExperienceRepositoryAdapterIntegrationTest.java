package org.example.protic.infrastructure.database;

import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.database.mybatis.mappers.*;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapter;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapterSync;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.when;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MapperScan("org.example.protic.infrastructure.database.mybatis.mappers")
public class WorkExperienceRepositoryAdapterIntegrationTest {

  @Inject private JobTitleRecordMapper jobTitleRecordMapper;
  @Inject private CompanyRecordMapper companyRecordMapper;
  @Inject private TechnologyRecordMapper technologyRecordMapper;
  @Inject private WorkExperienceRecordMapper workExperienceRecordMapper;
  @Inject private WorkExperienceTechnologyRecordMapper workExperienceTechnologyRecordMapper;

  @Test
  @DisplayName("It tests work experience creation on database.")
  void persistWorkExperience() {
    WorkExperienceRepository adapter = createAdapter();
    WorkExperience workExperience = createWorkExperience();
    adapter.create(workExperience).join();
    WorkExperience recoveredWorkExperience = adapter.findById(workExperience.getId()).join();
    assertNotNull(recoveredWorkExperience);
  }

  private WorkExperienceRepository createAdapter() {
    return new WorkExperienceRepositoryAdapter(createAdapterSync());
  }

  private WorkExperienceRepositoryAdapterSync createAdapterSync() {
    return new WorkExperienceRepositoryAdapterSync(
        jobTitleRecordMapper,
        companyRecordMapper,
        technologyRecordMapper,
        workExperienceRecordMapper,
        workExperienceTechnologyRecordMapper);
  }

  private static WorkExperience createWorkExperience() {
    WorkExperience workExperience = Mockito.mock(WorkExperience.class);
    when(workExperience.getId()).thenReturn(UUID.randomUUID());
    when(workExperience.getCreatedAt()).thenReturn(Timestamp.from(Instant.now()));
    when(workExperience.getUserId()).thenReturn(UserId.of("USER_ID"));
    when(workExperience.getBinding()).thenReturn(true);
    when(workExperience.getJobTitle())
        .thenReturn(WorkExperienceField.ofPublic(JobTitle.of("JOB_TITLE")));
    when(workExperience.getCompany())
        .thenReturn(WorkExperienceField.ofPublic(Company.of("COMPANY")));
    when(workExperience.getTechnologies())
        .thenReturn(
            WorkExperienceField.ofPublic(
                Set.of(Technology.of("TECHNOLOGY1"), Technology.of("TECHNOLOGY2"))));
    when(workExperience.getWorkPeriod())
        .thenReturn(
            WorkExperienceField.ofPublic(
                WorkPeriod.from(LocalDate.now().minus(2, ChronoUnit.DAYS))
                    .to(LocalDate.now().minus(1, ChronoUnit.DAYS))));
    return workExperience;
  }
}
