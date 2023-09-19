package com.app.skhuaz.util;

import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityUtil {
    public static <T> T mustNotNull(T entity, ErrorCode errorCode) {
        if (entity == null) throw new BusinessException(errorCode);
        return entity;
    }

    public static <T> T mustNotNull(Optional<T> optEntity, ErrorCode errorCode) {
        return optEntity.orElseThrow(() -> new BusinessException(errorCode));
    }

    public static void mustNull(Object entity, ErrorCode errorCode) {
        if (entity != null) throw new BusinessException(errorCode);
    }
}