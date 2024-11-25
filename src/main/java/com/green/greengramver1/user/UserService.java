package com.green.greengramver1.user;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.user.model.UserSignInReq;
import com.green.greengramver1.user.model.UserSignInRes;
import com.green.greengramver1.user.model.UserSignUpReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.color.ICC_ColorSpace;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;

    public int postSignUp(MultipartFile pic, UserSignUpReq p){
        //프로필 이미지 파일 처리
        //myFileUtils.makeFolders("ddd/aaa"); 폴더 잘 만들어지는거 확인했다.
        //String savedPicName = myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null); //위과 같은 결과가 나왔으면 좋겠다.(그래서 메소드만들었다)
        // 확장자를 살리기 위해서 위처럼 작성했다.
        //밑에 줄들을 위해 만든 명: savedPicName

        String hashedPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt()); //비번 암호화
        log.info("hashedPassword: {}", hashedPassword);
        p.setUpw(hashedPassword); //비번설정
        p.setPic(savedPicName); //파일이름설정

        int result = mapper.insUser(p); //mapper.insUser(p)얘가 리턴하는값은 영향받은행 정수값
        //호출이후에 p를 살펴보면 pk값이 들어가있을거다

       /* String path = myFileUtils.makeFolders(String.format("user/%d", userId));
        log.info("path: {}", path);
        String filePath = String.format("%s/%s", path, savedPicName);*/

        if(pic == null) { //pic이 null이면 오리지널네임이 없어서 밑에 과정없이 바로 result 리턴함.
            return result;
        }

        // 저장 위치 만든다.
        // middlePath = user/${userId}
        // filePath = user/${userId}/${savedPicName}
        long userId = p.getUserId(); //user를 insert 후에 얻을 수 있다. //Req에 있던 userId를 외부로 빼냄,뽑아냄(다른 userId로)
        String middlePath = String.format("user/%d", userId); //userId는 pk값
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath: {}", middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);
        //여러가지 언어(일본어, 아랍어 등)의 파일명보다는 문제없는 숫자와 영어로 된 파일이름을 만들려고 파일이름을 바꿨다.

        try {
            myFileUtils.transferTo(pic, filePath); //filePath위치로 파일만들어줌
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result; //영향받은 튜플 1이 리턴될것이다.
    }

    public UserSignInRes postSignIn(UserSignInReq p){
        UserSignInRes res = mapper.selUserForSignIn(p);
        if(res == null){ //아이디 없음
            res = new UserSignInRes();
            res.setMessage("아이디를 확인해 주세요.");
            return res;
        }

        //비밀번호과 다를시
        if( !BCrypt.checkpw(p.getUpw(), res.getUpw()) ){ //순수한비번, 암호화된 비번
            res = new UserSignInRes();
            res.setMessage("비밀번호를 확인해 주세요.");
            return res;
        }
        res.setMessage("로그인 성공");
        return res;
    }
}
