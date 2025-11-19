package com.advancedMobileProgramming.global.common.code.status;

import com.advancedMobileProgramming.global.common.code.BaseCode;
import com.advancedMobileProgramming.global.common.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "요청 성공 및 리소스 생성됨"),

    // USER
    USER_REGISTER_LOCAL_SUCCESS(HttpStatus.OK, "USER_300", "로컬 회원가입 성공했습니다."),
    USER_PROFILE_SUCCESS(HttpStatus.OK, "USER_301", "로그인한 유저 조회 성공했습니다."),
    USER_LOGIN_LOCAL_SUCCESS(HttpStatus.OK, "USER_302", "로컬 로그인 성공했습니다."),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "USER_303", "로그아웃 성공했습니다."),
    USER_REFRESH_TOKEN(HttpStatus.OK, "USER_304", "토큰 재발급 성공했습니다."),

    // CATEGORY
    CATEGORY_ADD_SUCCESS(HttpStatus.OK, "CATEGORY_300", "카테고리 추가 성공했습니다."),
    CATEGORY_LIST_SUCCESS(HttpStatus.OK, "CATEGORY_301", "카테고리 조회 성공했습니다."),

    // EQUIPMENT
    EQUIPMENT_ADD_SUCCESS(HttpStatus.OK, "EQUIPMENT_202", "기자재 추가 성공했습니다."),
    EQUIPMENT_ADD_IMAGE_SUCCESS(HttpStatus.OK, "EQUIPMENT_203", "기자재 이미지 데이터 추가에 성공했습니다."),
    EQUIPMENT_MODIFY_SUCCESS(HttpStatus.OK, "EQUIPMENT_204", "기자재 수정 성공했습니다."),
    EQUIPMENT_DELETE_SUCCESS(HttpStatus.OK, "EQUIPMENT_205", "기자재 삭제 성공했습니다."),
    EQUIPMENT_SCAN_SUCCESS(HttpStatus.OK, "EQUIPMENT_206", "기자재 스캔 성공했습니다."),
    EQUIPMENT_POPULAR_SUCCESS(HttpStatus.OK, "EQUIPMENT_207", "인기 기자재 조회 성공입니다."),
    EQUIPMENT_LIST_SUCCESS(HttpStatus.OK, "EQUIPMENT_208", "기자재 전체 목록 조회 성공입니다."),
    EQUIPMENT_SEARCH_SUCCESS(HttpStatus.OK, "EQUIPMENT_209", "기자재 명으로 검색 성공입니다."),
    EQUIPMENT_CATEGORY_FILTER_SUCCESS(HttpStatus.OK, "EQUIPMENTF_310", "카테고리 필터링 성공입니다."),
    EQUIPMENT_RENTAL_STATUS_FILTER_SUCCESS(HttpStatus.OK, "EQUIPMENTF_311", "대여상태 필터링 성공입니다."),
    EQUIPMENT_FILTER_SUCCESS(HttpStatus.OK, "EQUIPMENTF_312", "카테고리 및 대여상태 필터링 성공입니다."),
    EQUIPMENT_DETAIL_SUCCESS(HttpStatus.OK, "EQUIPMENTF_313", "기자재 상세 정보 조회 성공입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}