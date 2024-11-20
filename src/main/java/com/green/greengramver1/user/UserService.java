package com.green.greengramver1.user;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.user.model.UserInsReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;

    public int postSignUp(MultipartFile pic, UserInsReq p){
        //프로필 이미지 파일 처리
        //myFileUtils.makeFolders("ddd/aaa"); 폴더 잘 만들어지는거 확인했다.
        //String savedPicName = myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        String savedPicName = pic != null ? myFileUtils.makeRandomFileName(pic) : null; //위과 같은 결과가 나왔으면 좋겠다.(그래서 메소드만들었다)

        String hashedPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());
        log.info("hashedPassword: {}", hashedPassword);
        p.setUpw(hashedPassword);
        p.setPic(savedPicName);

        int result = mapper.insUser(p);

       /* String path = myFileUtils.makeFolders(String.format("user/%d", userId));
        log.info("path: {}", path);
        String filePath = String.format("%s/%s", path, savedPicName);*/

        if(pic == null) {
            return result;
        }

        // user/${userId}/${savedPicName}
        long userId = p.getUserId(); //user를 insert 후에 얻을 수 있다.

        String middlePath = String.format("user/%d", userId);
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath: {}", middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
