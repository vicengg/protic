package org.example.protic.infrastructure.database.mybatis.records;

import java.sql.Timestamp;

public class NegotiationActionRecord {

  public long idNegotiationAction;
  public byte[] idNegotiation;
  public Timestamp actionDate;
  public String type;
  public String issuerId;
  public long idOfferedData;
  public long idDemandedData;
}
