package org.example.protic.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public abstract class Entity implements Identifiable, TimeTraceable {

  private final UUID id;
  private final Timestamp createdAt;

  protected Entity() {
    this.id = UUID.randomUUID();
    this.createdAt = Timestamp.from(Instant.now());
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Timestamp getCreatedAt() {
    return createdAt;
  }
}
