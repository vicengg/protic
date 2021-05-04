package org.example.protic.domain.negotiation;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.WorkExperience;

import java.util.List;
import java.util.Optional;

public interface Negotiation extends Identifiable, TimeTraceable {

  WorkExperience getOfferedWorkExperience();

  WorkExperience getDemandedWorkExperience();

  User getCreator();

  User getReceiver();

  Optional<User> getNextActor();

  List<Action> getActions();
}
