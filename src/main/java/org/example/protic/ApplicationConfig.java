package org.example.protic;

import org.example.protic.application.negotiation.NegotiationRepository;
import org.example.protic.application.negotiation.NegotiationService;
import org.example.protic.application.negotiation.NegotiationServiceImpl;
import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.application.workexperience.WorkExperienceService;
import org.example.protic.application.workexperience.WorkExperienceServiceImpl;
import org.example.protic.infrastructure.connector.GithubUserConnector;
import org.example.protic.infrastructure.connector.UserConnector;
import org.example.protic.infrastructure.database.mybatis.mappers.*;
import org.example.protic.infrastructure.database.negotiation.NegotiationRepositoryAdapter;
import org.example.protic.infrastructure.database.negotiation.NegotiationRepositoryAdapterSync;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapter;
import org.example.protic.infrastructure.database.workexperience.WorkExperienceRepositoryAdapterSync;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.example.protic.infrastructure.database.mybatis.mappers")
public class ApplicationConfig {

  @Bean
  public UserConnector userConnector(UserCacheRecordMapper userCacheRecordMapper) {
    return new GithubUserConnector(userCacheRecordMapper);
  }

  @Bean
  public WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapterSync(
      JobTitleRecordMapper jobTitleRecordMapper,
      CompanyRecordMapper companyRecordMapper,
      TechnologyRecordMapper technologyRecordMapper,
      WorkExperienceRecordMapper workExperienceRecordMapper,
      WorkExperienceTechnologyRecordMapper workExperienceTechnologyRecordMapper,
      UserConnector userConnector) {

    return new WorkExperienceRepositoryAdapterSync(
        jobTitleRecordMapper,
        companyRecordMapper,
        technologyRecordMapper,
        workExperienceRecordMapper,
        workExperienceTechnologyRecordMapper,
        userConnector);
  }

  @Bean
  public WorkExperienceRepository workExperienceServiceRepository(
      WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapterSync) {
    return new WorkExperienceRepositoryAdapter(workExperienceRepositoryAdapterSync);
  }

  @Bean
  public WorkExperienceService workExperienceService(
      WorkExperienceRepository workExperienceRepository,
      NegotiationRepository negotiationRepository) {
    return new WorkExperienceServiceImpl(workExperienceRepository, negotiationRepository);
  }

  @Bean
  public NegotiationRepositoryAdapterSync negotiationRepositoryAdapterSync(
      RequestedDataRecordMapper requestedDataRecordMapper,
      NegotiationRecordMapper negotiationRecordMapper,
      NegotiationActionRecordMapper negotiationActionRecordMapper,
      WorkExperienceRepositoryAdapterSync workExperienceRepositoryAdapterSync,
      UserConnector userConnector) {

    return new NegotiationRepositoryAdapterSync(
        requestedDataRecordMapper,
        negotiationRecordMapper,
        negotiationActionRecordMapper,
        workExperienceRepositoryAdapterSync,
        userConnector);
  }

  @Bean
  public NegotiationRepository negotiationRepository(
      NegotiationRepositoryAdapterSync negotiationRepositoryAdapterSync) {
    return new NegotiationRepositoryAdapter(negotiationRepositoryAdapterSync);
  }

  @Bean
  public NegotiationService negotiationService(
      WorkExperienceRepository workExperienceRepository,
      NegotiationRepository negotiationRepository) {
    return new NegotiationServiceImpl(workExperienceRepository, negotiationRepository);
  }
}
