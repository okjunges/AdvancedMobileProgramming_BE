package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class ExistedEquipmentException extends GeneralException {
    public ExistedEquipmentException() { super(ErrorStatus.EQUIPMENT_ALREADY_EXISTED); }
}
