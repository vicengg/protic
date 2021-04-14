package org.example.protic.commons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UuidAdapterTest {

  @Test
  @DisplayName("Converts UUID to byte array and then deconverts it.")
  void convertAndDeconvertUUID() {
    UUID uuid = UUID.randomUUID();
    byte[] bytesFromUUID = UuidAdapter.getBytesFromUUID(uuid);
    assertNotNull(bytesFromUUID);
    UUID uuidFromBytes = UuidAdapter.getUUIDFromBytes(bytesFromUUID);
    assertEquals(uuid, uuidFromBytes);
  }
}
