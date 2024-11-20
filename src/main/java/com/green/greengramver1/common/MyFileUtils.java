package com.green.greengramver1.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.FileSupportConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 빈등록 : 스프링컨테이너 한테 객체화 맡기는거
//         , 그 주솟값을 갖고 있고 필요할때마다 DI 요청하면 컨테이너가 복사해서 준다.
//@AllArgsConstructor 얘는 스프링한테 uploadPath에 뭐넣을지 더 말해줄수없어서 생성자를 직접 만든거
@Slf4j
@Component //빈등록 (@Mapper, @Service는 얘 상속받음)
public class MyFileUtils {
    private final String uploadPath;

    /*
        @Value("${file.directory}")는
        yaml 파일에 있는 file.directory 속성에 저장된 값을 생성자 호출할 때 uploadPath에 값(주소경로)을 넣어준다.
     */
    public MyFileUtils(@Value("${file.directory}") String uploadPath) {
        log.info("MyFileUtiles - 생성자: {}", uploadPath);
        this.uploadPath = uploadPath;
    }

    // path = "ddd/aaa"
    // D:/mydownload/greengram_ver1/ddd/aaa 이렇게 만든 파일경로객체를 만들어준다.
    // 디렉토리 생성
    public String makeFolders(String path){
        File file = new File(uploadPath, path);
        file.mkdirs(); //하나만들때도 되고 여러개도 되기에 dirs를 사용.
        return file.getAbsolutePath();
    }

    // 파일명에서 확장자 추출 (.jpg 같은거)
    public String getExt(String fileName){
        int lastIdx = fileName.lastIndexOf("."); //마지막에 있는 . 인덱스값
        return fileName.substring(lastIdx);
    }

    // 랜덤파일명 생성
    public String makeRandomFileName(){
        return UUID.randomUUID().toString();
    }

    // 랜덤파일명 + 확장자 생성
    // 오버로딩(같은 이름의 메소드 여러개 만드는 기법, 파라미터 다르면 구분됨)
    public String makeRandomFileName(String originalFileName) {
       return makeRandomFileName() + getExt(originalFileName);
    }

    public String makeRandomFileName(MultipartFile file) {
        return makeRandomFileName(file.getOriginalFilename());
    }

    //파일을 원하는 경로에 저장
    public void transferTo(MultipartFile multipartFile,  String path) throws IOException {
        File file = new File(uploadPath, path);
        multipartFile.transferTo(file);
    }
}


class Test {
    public static void main(String[] args){
        MyFileUtils myFileUtils = new MyFileUtils("D:/temp");
        String randomFileName = myFileUtils.makeRandomFileName("707211_1532672215.jpg");
        System.out.println(randomFileName);

    }
}
