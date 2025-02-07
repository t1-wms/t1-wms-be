package com.example.wms.user.application.exception;

import com.example.wms.infrastructure.exception.NotFoundException;

public class UserIdNotFoundException extends NotFoundException {
  public UserIdNotFoundException(String message) {
    super(message);
  }
}
