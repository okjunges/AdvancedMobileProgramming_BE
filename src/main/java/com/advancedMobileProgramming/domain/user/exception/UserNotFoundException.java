package com.advancedMobileProgramming.domain.user.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super(ErrorStatus.USER_NOT_FOUND);
    }
}
