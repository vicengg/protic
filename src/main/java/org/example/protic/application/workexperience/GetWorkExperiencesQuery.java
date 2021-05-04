package org.example.protic.application.workexperience;

import org.example.protic.application.IdentifiedRequest;
import org.example.protic.application.Query;
import org.example.protic.domain.workexperience.Company;
import org.example.protic.domain.workexperience.JobTitle;
import org.example.protic.domain.workexperience.Technology;
import org.example.protic.domain.workexperience.WorkExperienceProjection;
import org.javamoney.moneta.Money;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GetWorkExperiencesQuery extends IdentifiedRequest<List<WorkExperienceProjection>>
    implements Query<List<WorkExperienceProjection>> {

  public enum Scope {
    OWN,
    FOREIGN,
    ALL;

    public static Scope of(String code) {
      return Arrays.stream(Scope.values())
          .filter(c -> c.name().equalsIgnoreCase(code))
          .findFirst()
          .orElse(ALL);
    }
  }

  public Scope scope;
  public JobTitle jobTitle;
  public Company company;
  public Set<Technology> technologies;
  public LocalDate startDate;
  public LocalDate endDate;
  public Money minSalary;
  public Money maxSalary;
}
