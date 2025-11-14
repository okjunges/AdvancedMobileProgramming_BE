package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class NoMatchingEquipmentException extends GeneralException {
    public NoMatchingEquipmentException() { super(ErrorStatus.VISION_SCAN_NOT_MATCH); }
}