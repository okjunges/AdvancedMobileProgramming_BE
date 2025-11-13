package com.advancedMobileProgramming.domain.category.exception;

import com.advancedMobileProgramming.global.common.code.status.ErrorStatus;
import com.advancedMobileProgramming.global.exception.GeneralException;

public class CategoryNotExistException extends GeneralException {
    public CategoryNotExistException() { super(ErrorStatus.CATEGORY_NOT_EXISTED); }
}
