package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class NotMatchingEquipmentException extends GeneralException {
    public NotMatchingEquipmentException() { super(ErrorStatus.VISION_SCAN_NOT_MATCH); }
}