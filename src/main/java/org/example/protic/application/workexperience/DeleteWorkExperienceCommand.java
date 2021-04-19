package org.example.protic.application.workexperience;

import org.example.protic.application.Command;
import org.example.protic.domain.UserId;

import java.util.UUID;

public class DeleteWorkExperienceCommand implements Command<Void> {

  public UUID id;
  public UserId userId;
}
