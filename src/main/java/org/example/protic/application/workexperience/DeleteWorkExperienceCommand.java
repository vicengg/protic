package org.example.protic.application.workexperience;

import org.example.protic.application.Command;
import org.example.protic.application.IdentifiedRequest;

import java.util.UUID;

public class DeleteWorkExperienceCommand extends IdentifiedRequest<Void> implements Command<Void> {

  public UUID id;
}
