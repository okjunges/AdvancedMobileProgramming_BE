package com.advancedMobileProgramming.domain.user.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class StudentAlreadyUseException extends GeneralException {
    public StudentAlreadyUseException() {
        super(ErrorStatus.STUDENT_NUMBER_ALREADY_USED);
    }
}
