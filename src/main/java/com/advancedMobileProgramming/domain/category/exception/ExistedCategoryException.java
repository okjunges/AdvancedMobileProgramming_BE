package com.advancedMobileProgramming.domain.category.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class ExistedCategoryException extends GeneralException {
    public ExistedCategoryException() { super(ErrorStatus.CATEGORY_ALREADY_EXISTED); }
}
