package org.example.protic.infrastructure.database.mybatis.records;

import java.sql.Date;
import java.sql.Timestamp;

public class WorkExperienceRecord {

  public byte[] idWorkExperience;
  public Timestamp createdAt;
  public String userId;
  public boolean binding;
  public long idJobTitle;
  public boolean visibilityJobTitle;
  public long idCompany;
  public boolean visibilityCompany;
  public boolean visibilityTechnologies;
  public Date startDate;
  public Date endDate;
  public boolean visibilityWorkPeriod;
}
