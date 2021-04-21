package org.example.protic.infrastructure.rest;

import java.util.Collection;

public class CollectionDto<R extends RestDto, C extends Collection<R>> implements RestDto {

  public C result;

  public static <R extends RestDto, C extends Collection<R>> CollectionDto<R, C> of(C collection) {
    CollectionDto<R, C> collectionDto = new CollectionDto<>();
    collectionDto.result = collection;
    return collectionDto;
  }
}
