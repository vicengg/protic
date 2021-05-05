package org.example.protic.domain.negotiation;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.WorkExperience;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NegotiationProjection extends Identifiable, TimeTraceable {

    UUID getOfferedWorkExperienceId();

    UUID getDemandedWorkExperienceId();

    User getCreator();

    User getReceiver();

    Optional<User> getNextActor();

    List<Action> getActions();

}
