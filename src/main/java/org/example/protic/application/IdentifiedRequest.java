package org.example.protic.application;

import org.example.protic.domain.user.User;

public abstract class IdentifiedRequest<T> implements Request<T> {

  public User user;
}
