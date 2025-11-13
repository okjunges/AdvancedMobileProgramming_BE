package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class NotExistedEquipmentException extends GeneralException {
    public NotExistedEquipmentException() {
        super(ErrorStatus.EQUIPMENT_NOT_EXISTED);
    }
}
