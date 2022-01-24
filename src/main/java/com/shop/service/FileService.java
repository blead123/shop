package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    //업로드 메소드
    public String uploadFile(String uploadPath, String originalFileName , byte[] fileData ) throws Exception{
        UUID uuid = UUID.randomUUID();//서로다른 개체를 구분하기위한 파일명 부여

        String extension= originalFileName.substring(originalFileName.lastIndexOf("."));

        String savedFileName = uuid.toString()+extension;//uuid 값과 원래 파일명 확장자를 조합해서 저장될 파일이름 만들기

        String fileUploadFullUrl = uploadPath+"/"+savedFileName;
        //바이트 단위의 출력을 내보내는 클래스
        //생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 쓸 파일 출력 스트림 생성
        FileOutputStream fos= new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData);//파일명 출력 스트림에 입력

        fos.close();

        return savedFileName;
    }

    //삭제 메소드
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일삭제에 성공했습니다");
        }else{
            log.info("파일이 존재하지 않습니다");
        }

    }
}
