package com.green.greengramver1.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title =  "회원가입")
public class UserSignUpReq {
    @JsonIgnore //userId는 내부적으로만 다루면돼서
    private long userId;
    @Schema(description = "유저 아이디", example = "mic", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(description = "유저 비밀번호", example = "1212", requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;
    //@Schema(description = "유저 프로필 사진")
    @JsonIgnore // 파일명을 굳이 보여줄 필요없어서
    private String pic; //내부적으로 사용함, 파일명 저장용
    @Schema(description = "유저 닉네임", example = "홍길동")
    private String nickName;
}