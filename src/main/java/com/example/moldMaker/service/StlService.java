package com.example.moldMaker.service;

import com.example.moldMaker.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StlService {
    /*
    public Path processStlFile(MultipartFile uploadedFile) throws IOException {
        // 1) 파일 임시 저장
        Path tempFilePath = Files.createTempFile("temp-stl-", ".stl");
        Files.write(tempFilePath, uploadedFile.getBytes());

        // 2) STL 파일 변환
        //    예: 메시 검사, 변환, 메타데이터 추출 등


        // 3) 변환 결과 저장
        //    변환된 파일 또는 결과물을 저장 후 경로/식별자 반환

        return tempFilePath.toAbsolutePath();
    }*/
    public byte[] processStlFile(byte[] stlFile) throws IOException, NotFoundException {
        if (stlFile == null || stlFile.length == 0) {
            throw new NotFoundException("Empty stl data");
        }
        // 1) 파일 임시 저장
        // 2) STL 파일 변환
        // 3) 변환 결과 저장

        byte[] convertedStlData = stlFile;

        return convertedStlData;
    }
}
