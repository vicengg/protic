package org.example.protic.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class Entity implements Identifiable, TimeTraceable {

  private final UUID id;
  private final Timestamp createdAt;

  protected Entity(UUID id, Timestamp createdAt) {
    this.id = Objects.requireNonNull(id);
    this.createdAt = Objects.requireNonNull(createdAt);
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
