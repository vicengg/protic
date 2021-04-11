package org.example.protic.domain.workexperience;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;

import java.util.Set;

public interface WorkExperience extends Identifiable, TimeTraceable {
  WorkExperienceField<JobTitle> getJobTitle();

  WorkExperienceField<Company> getCompany();

  WorkExperienceField<Set<Technology>> getTechnologies();

  WorkExperienceField<WorkPeriod> getWorkPeriod();
}
