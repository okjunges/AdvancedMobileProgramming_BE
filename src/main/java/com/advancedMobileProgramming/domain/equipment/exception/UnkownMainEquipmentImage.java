package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class UnkownMainEquipmentImage extends GeneralException {
    public UnkownMainEquipmentImage() { super(ErrorStatus.EQUIPMENT_UNKNOWN_IMAGE); }
}