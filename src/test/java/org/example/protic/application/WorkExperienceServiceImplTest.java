package org.example.protic.application;

import org.example.protic.application.workexperience.CreateWorkExperienceCommand;
import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.application.workexperience.WorkExperienceService;
import org.example.protic.application.workexperience.WorkExperienceServiceImpl;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkExperienceServiceImplTest {

  @Test
  @DisplayName("Create work experience test.")
  void createWorkExperience() {
    WorkExperienceRepository repository = mock(WorkExperienceRepository.class);
    WorkExperienceService service = new WorkExperienceServiceImpl(repository);
    CreateWorkExperienceCommand command = createWorkExperienceCommand();
    when(repository.create(any(WorkExperience.class))).thenReturn(futureOf(null));

    UUID workExperienceId = service.createWorkExperience(command).join();

    ArgumentCaptor<WorkExperience> captor = ArgumentCaptor.forClass(WorkExperience.class);
    verify(repository).create(captor.capture());
    assertEquals(captor.getValue().getId(), workExperienceId);
  }

  private static CreateWorkExperienceCommand createWorkExperienceCommand() {
    CreateWorkExperienceCommand command = new CreateWorkExperienceCommand();
    command.userId = UserId.of("another_user_id");
    command.binding = true;
    command.jobTitle = RestrictedField.ofPrivate(JobTitle.of("job title"));
    command.company = RestrictedField.ofPrivate(Company.of("company"));
    command.technologies = RestrictedField.ofPrivate(Set.of(Technology.of("technology")));
    command.workPeriod =
        RestrictedField.ofPrivate(
            WorkPeriod.from(LocalDate.now().minus(1, ChronoUnit.DAYS)).toPresent());
    command.salary = RestrictedField.ofPrivate(Money.of(1000, "EUR"));
    return command;
  }

  private static <T> CompletableFuture<T> futureOf(T content) {
    return CompletableFuture.completedFuture(content);
  }
}
