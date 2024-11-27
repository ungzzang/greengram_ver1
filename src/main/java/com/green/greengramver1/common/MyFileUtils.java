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
public class MyFileUtils { //스프링이 이 클래스를 객체화하고 객체주소값을 관리함
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
        // File file = new File(uploadPath + "/" + path)도 됨. 이렇게하면 인자 하나만 보내는거, 인자하나받는 생성자도 있어서 생성자 총 2개가됨.

        // static 아님 >> 객체화하고 주소값(file.)으로 호출했기 때문에
        // 리턴타입은 boolean >> if()안에서 호출했기 때문에
        // 파라미터는 없음 >> 호출 때 인자를 보내지 않았기 때문에
        // 메소드명은 >> exists였다.
        if(!file.exists()){ //ddd나 aaa 폴더 하나이상 존재하면 true, 없으면 false, 존재하지 않는다면 폴더만들겠다(!붙어서)
            file.mkdirs();
        }
        //file.mkdirs(); //하나만들때도 되고 여러개도 되기에 dirs를 사용. //얘가 폴더만들어줌(폴더가 있으면 그냥 지나감)
        return file.getAbsolutePath();
    }

    // 파일명에서 확장자 추출 (.jpg 같은거)
    public String getExt(String fileName){ // ex) fileName = abc.jpg
        int lastIdx = fileName.lastIndexOf("."); //마지막에 있는 . 위치 인덱스값(정수값) ex) 3
        return fileName.substring(lastIdx);
    }

    // 랜덤파일명 생성
    public String makeRandomFileName(){
        return UUID.randomUUID().toString(); //UUID가 랜덤형식(유니버셜 유니크 아이디)
    }

    // 랜덤파일명 + 확장자 생성
    // 오버로딩(같은 이름의 메소드 여러개 만드는 기법, 파라미터 다르면 구분됨)
    public String makeRandomFileName(String originalFileName) {
       return makeRandomFileName() + getExt(originalFileName); // 스트링끼리 +한거
        //makeRandomFileName()얘는 리턴메소드다. 왜냐면 리턴해야되는데 쟤(makeRandomFileName())가 리턴이 아니면 그 값을 받아 리턴못하니까
    }

    public String makeRandomFileName(MultipartFile file) {
        return makeRandomFileName(file.getOriginalFilename());
        // file.getOriginalFilename()얘는 리턴메소드이다.(괄호에 둘러쌓였음), 리턴해야 값을 받고 makeRandomFileName(file.getOriginalFilename());이게 되니까
        //getOriginalFilename()는 스트링이고 makeRandomFileName(String originalFileName) 얘가 스트링을 파라미터로 받음
        //getOriginalFilename()는 내가 올린 진짜 파일이름
    }

    //파일을 원하는 경로에 저장
    //multipartFile이 사진정보, path가 filePath(user/userId/savedFileName)
    public void transferTo(MultipartFile multipartFile,  String path) throws IOException {
        // file에 uploadPath와 path가 합쳐진(부모/자식)
        // ex)D:/mydownload/test1_greengram/user/1/b76892b9-b2d4-4921-a896-fec7c9896867.jpg 가 담긴다.
        File file = new File(uploadPath, path);
        multipartFile.transferTo(file);// file을 옮긴다.(저장), 사진을 다른이름으로 저장한다고 생각(file이 가지고 있는 주소로)
        // MultipartFile은 인터페이스고 이걸 상속받은 객체주소값이 multipartFile에 담긴다.
    }
}


class Test {
    public static void main(String[] args){
        MyFileUtils myFileUtils = new MyFileUtils("D:/temp");
        String randomFileName = myFileUtils.makeRandomFileName("707211_1532672215.jpg");
        System.out.println(randomFileName);

    }
}
