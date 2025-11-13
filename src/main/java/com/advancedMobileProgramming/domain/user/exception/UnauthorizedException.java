package com.advancedMobileProgramming.domain.user.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class UnauthorizedException extends GeneralException {
    public UnauthorizedException() {
        super(ErrorStatus.USER_UNAUTHORIZED);
    }
}
