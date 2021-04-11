package org.example.protic.domain.workexperience;

import java.util.Optional;
import java.util.Set;

public interface FilteredWorkExperience {

  Optional<JobTitle> getJobTitle();

  Optional<Company> getCompany();

  Optional<Set<Technology>> getTechnologies();

  Optional<WorkPeriod> getWorkPeriod();
}
