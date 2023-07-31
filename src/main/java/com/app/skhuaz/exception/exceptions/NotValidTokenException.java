package com.app.skhuaz.exception.exceptions;

import com.app.skhuaz.exception.ErrorCode;

public class NotValidTokenException extends AppServiceException {
    public NotValidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}