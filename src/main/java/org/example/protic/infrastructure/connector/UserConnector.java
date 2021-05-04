package org.example.protic.infrastructure.connector;

import org.example.protic.domain.user.User;

public interface UserConnector {

  User findUserById(String id);
}
