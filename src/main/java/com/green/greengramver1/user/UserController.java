package com.green.greengramver1.user;

import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.user.model.UserInsReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "유저", description = "회원가입, 로그인, 마이페이지, 비밀번호 변경, 정보 수정 등 처리")
public class UserController {
    private final UserService service;

    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUp(@RequestBody UserInsReq p){
        int result = 0;
        return ResultResponse.<Integer>builder()
                .resultMessage("회원가입 완료")
                .resultData(result)
                .build();
    }
}
