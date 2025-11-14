package com.advancedMobileProgramming.domain.equipment.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class UnsupportedImageFormatException extends GeneralException {
    public UnsupportedImageFormatException() { super(ErrorStatus.UNSUPPORTED_IMAGE_FORMAT); }
}
