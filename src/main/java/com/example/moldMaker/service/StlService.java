package com.example.moldMaker.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StlService {
    public Path processStlFile(MultipartFile uploadedFile) throws IOException {
        // 1) 파일 임시 저장
        Path tempFilePath = Files.createTempFile("temp-stl-", ".stl");
        Files.write(tempFilePath, uploadedFile.getBytes());

        // 2) STL 파일 변환
        //    예: 메시 검사, 변환, 메타데이터 추출 등


        // 3) 변환 결과 저장
        //    변환된 파일 또는 결과물을 저장 후 경로/식별자 반환

        return tempFilePath.toAbsolutePath();
    }
}
