package com.advancedMobileProgramming.domain.user.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class UserNotAdmin extends GeneralException {
    public UserNotAdmin() { super(ErrorStatus.USER_NOT_ADMIN); }
}
