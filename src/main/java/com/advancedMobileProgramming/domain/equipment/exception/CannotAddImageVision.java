package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class CannotAddImageVision extends GeneralException {
    public CannotAddImageVision() {
        super(ErrorStatus.VISION_NOT_ACCESS_YOU);
    }
}
