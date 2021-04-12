package org.example.protic.domain.workexperience;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.UserId;

import java.util.Set;

public interface WorkExperience extends Identifiable, TimeTraceable {

  UserId getUserId();

  boolean getBinding();

  WorkExperienceField<JobTitle> getJobTitle();

  WorkExperienceField<Company> getCompany();

  WorkExperienceField<Set<Technology>> getTechnologies();

  WorkExperienceField<WorkPeriod> getWorkPeriod();
}
