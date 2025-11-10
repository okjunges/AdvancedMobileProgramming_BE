package com.advancedMobileProgramming.domain.user.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class UserWrongPasswordException extends GeneralException {
    public UserWrongPasswordException() { super(ErrorStatus.USER_WRONG_PASSWORD); }
}
