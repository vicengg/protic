package org.example.protic.domain.workexperience;

import org.example.protic.domain.UserId;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WorkExperienceProjection {

  UUID getId();

  Optional<UserId> getUserId();

  Optional<JobTitle> getJobTitle();

  Optional<Company> getCompany();

  Optional<Set<Technology>> getTechnologies();

  Optional<WorkPeriod> getWorkPeriod();
}
