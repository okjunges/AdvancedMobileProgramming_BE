package com.advancedMobileProgramming.global.common.code.status;

import com.advancedMobileProgramming.global.common.code.BaseErrorCode;
import com.advancedMobileProgramming.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // --- Common ---
    _INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON400", "입력값이 올바르지 않습니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "권한이 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // --- USER ---
    STUDENT_NUMBER_ALREADY_USED(HttpStatus.BAD_REQUEST, "USER_400", "이미 가입된 학번입니다."),
    USER_WRONG_PASSWORD(HttpStatus.NOT_FOUND, "USER_403", "비밀번호를 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "사용자를 찾을 수 없습니다."),
    USER_UNAUTHORIZED(HttpStatus.NOT_FOUND, "USER_501", "사용자 인증 토근이 없습니다."),
    USER_NOT_ADMIN(HttpStatus.BAD_REQUEST, "USER_405", "관리자 권한이 없습니다."),

    // --- CATEGORY ---
    CATEGORY_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "CATEGORY_400", "이미 존재하는 카테고리입니다."),
    CATEGORY_NOT_EXISTED(HttpStatus.NOT_FOUND, "CATEGORY_404", "존재하지 않는 카테고리입니다."),

    // --- EQUIPMENT ---
    EQUIPMENT_ALREADY_EXISTED(HttpStatus.BAD_REQUEST, "EQUIPMENT_400", "이미 등록된 기자재입니다."),
    EQUIPMENT_UNKNOWN_IMAGE(HttpStatus.BAD_REQUEST, "EQUIPMENT_401", "기자재의 이미지가 필수입니다."),
    EQUIPMENT_NOT_EXISTED(HttpStatus.NOT_FOUND, "EQUIPMENT_404", "기자재가 존재하지 않습니다."),
    EQUIPMENT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "EQUIPMENT_405", "대여 가능한 재고가 없습니다."),

    // --- VISION ---
    VISION_NOT_ACCESS_YOU(HttpStatus.BAD_REQUEST, "EQUIPMENT_400", "이미지 데이터를 추가할 권한이 없습니다."),
    VISION_SCAN_NOT_MATCH(HttpStatus.NOT_FOUND, "EQUIPMENT_404", "이미지 데이터에 맞는 기자재가 없습니다."),
    UNSUPPORTED_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "EQUIPMENT_401", "해당 이미지 파일은 지원하지 않는 확장자입니다. JPG/PNG로 업로드해 주세요."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}