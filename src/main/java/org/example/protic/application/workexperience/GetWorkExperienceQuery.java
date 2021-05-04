package org.example.protic.application.workexperience;

import org.example.protic.application.IdentifiedRequest;
import org.example.protic.application.Query;
import org.example.protic.domain.workexperience.WorkExperienceProjection;

import java.util.UUID;

public class GetWorkExperienceQuery extends IdentifiedRequest<WorkExperienceProjection>
    implements Query<WorkExperienceProjection> {
  public UUID id;
}
