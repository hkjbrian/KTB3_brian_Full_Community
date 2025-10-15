package com.community.global.validation;

public class MessageConstants {

    private MessageConstants() {}

    // email
    public static final String EMAIL_REQUIRED = "이메일 값은 필수입니다.";
    public static final String EMAIL_FORMAT_INVALID = "유효한 이메일 형식이 아닙니다.";

    // password
    public static final String PASSWORD_REQUIRED = "비밀번호 값은 필수입니다.";
    public static final String PASSWORD_SIZE_INVALID = "비밀번호는 8자 이상, 20자 이하입니다.";

    // nickname
    public static final String NICKNAME_REQUIRED = "닉네임 값은 필수입니다.";
    public static final String NICKNAME_SIZE_INVALID = "닉네임은 최대 10자까지 작성 가능합니다.";


}
