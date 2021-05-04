package org.example.protic.infrastructure.database.mybatis.records;

import java.sql.Timestamp;

public class NegotiationRecord {

  public byte[] idNegotiation;
  public Timestamp createdAt;
  public byte[] idOfferedWorkExperience;
  public byte[] idDemandedWorkExperience;
  public String creatorId;
  public String receiverId;
  public String nextActor;
}
