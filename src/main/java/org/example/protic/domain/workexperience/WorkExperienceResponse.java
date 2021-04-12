package org.example.protic.domain.workexperience;

import org.example.protic.domain.UserId;

import java.util.Optional;
import java.util.Set;

public interface WorkExperienceResponse {

  Optional<UserId> getUserId();

  Optional<JobTitle> getJobTitle();

  Optional<Company> getCompany();

  Optional<Set<Technology>> getTechnologies();

  Optional<WorkPeriod> getWorkPeriod();
}
