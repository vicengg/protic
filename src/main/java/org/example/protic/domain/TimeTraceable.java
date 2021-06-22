package org.example.protic.domain;

import java.sql.Timestamp;

public interface TimeTraceable {

  Timestamp getCreatedAt();
}
