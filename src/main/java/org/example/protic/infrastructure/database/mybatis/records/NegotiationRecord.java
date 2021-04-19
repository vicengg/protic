package org.example.protic.infrastructure.database.mybatis.records;

import java.sql.Timestamp;

public class NegotiationRecord {

  public byte[] idNegotiation;
  public Timestamp createdAt;
  public long idOfferedData;
  public long idDemandedData;
  public String state;
}
