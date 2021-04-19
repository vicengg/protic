package org.example.protic.infrastructure.database.mybatis.records;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

public class WorkExperienceFilterRecord {

  public String userId;
  public String scope;
  public String jobTitle;
  public String company;
  public Set<String> technologies;
  public Date startDate;
  public Date endDate;
  public BigDecimal minSalary;
  public BigDecimal maxSalary;
}
