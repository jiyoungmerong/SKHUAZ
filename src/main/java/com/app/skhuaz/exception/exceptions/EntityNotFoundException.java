package com.app.skhuaz.exception.exceptions;

import com.app.skhuaz.exception.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
