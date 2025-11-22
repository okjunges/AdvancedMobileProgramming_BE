package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class InsufficientStockException extends GeneralException {

    public InsufficientStockException() {
        super(ErrorStatus.EQUIPMENT_OUT_OF_STOCK);
    }
}
