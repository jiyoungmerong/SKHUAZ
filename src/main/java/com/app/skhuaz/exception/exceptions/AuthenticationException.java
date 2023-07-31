package com.app.skhuaz.exception.exceptions;

import com.app.skhuaz.exception.ErrorCode;

public class AuthenticationException extends AppServiceException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}

