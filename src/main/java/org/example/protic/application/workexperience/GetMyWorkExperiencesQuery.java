package org.example.protic.application.workexperience;

import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.WorkExperienceResponse;

import java.util.List;

public class GetMyWorkExperiencesQuery implements Query<List<WorkExperienceResponse>> {

  public UserId userId;
}
