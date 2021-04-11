package org.example.protic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProticApplicationIntegrationTest {

  @BeforeAll
  static void setUp() {
    ProticApplication.main(new String[] {});
  }

  @Test
  @DisplayName("Run application")
  void runApplication() {}
}
