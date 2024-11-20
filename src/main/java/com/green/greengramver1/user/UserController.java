package com.green.greengramver1.user;

import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.user.model.UserInsReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Tag(name = "유저", description = "회원가입, 로그인, 마이페이지, 비밀번호 변경, 정보 수정 등 처리")
public class UserController {
    private final UserService service;

    /*
        파일(MultipartFile) + Data 둘다 올라오기 때문에
        파일 업로드 시에는 RequestBody를 사용할 수 없음.
        RequestPart 애노테이션 사용해야 함.

        required = false는 사진 안넣게도 하려고
     */
    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUp(@RequestPart UserInsReq p // 데이터 받기위해
                                        , @RequestPart(required = false) MultipartFile pic // 파일 받기위해
    ) {
        log.info("UserInsReq: {}, file: {}", p, pic != null ? pic.getOriginalFilename() : null);
        int result = service.postSignUp(pic, p);

        return ResultResponse.<Integer>builder()
                .resultMessage("회원가입 완료")
                .resultData(result)
                .build();
    }
}
