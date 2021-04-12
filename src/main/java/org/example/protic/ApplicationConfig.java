package org.example.protic;

import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.application.workexperience.WorkExperienceService;
import org.example.protic.application.workexperience.WorkExperienceServiceImpl;
import org.example.protic.infrastructure.database.mybatis.mappers.*;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapter;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapterSync;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.example.protic.infrastructure.database.mybatis.mappers")
public class ApplicationConfig {

  @Bean
  public WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapterSync(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper,
      WorkExperienceRecordMapper workExperienceRecordMapper,
      WorkExperienceTechnologyRecordMapper workExperienceTechnologyRecordMapper) {

    return new WorkExperienceRepositoryAdapterSync(
        jobTitleRecordMapper,
        companyRecordMapper,
        technologyRecordMapper,
        workExperienceRecordMapper,
        workExperienceTechnologyRecordMapper);
  }

  @Bean
  public WorkExperienceRepository workExperienceServiceRepository(
      WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapterSync) {
    return new WorkExperienceRepositoryAdapter(workExperienceRepositoryAdapterSync);
  }

  @Bean
  public WorkExperienceService workExperienceService(
      WorkExperienceRepository workExperienceRepository) {
    return new WorkExperienceServiceImpl(workExperienceRepository);
  }
}