package com.example.moldMaker.controller;

import com.example.moldMaker.service.StlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
* Http 형식: multipart/form-data
*
* */
@RequiredArgsConstructor
@RestController
public class StlController {
    private final StlService stlService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadStlFile(@RequestParam("uploadedFile") MultipartFile uploadedFile) {
        // 1) 파일 유효성 검사
        String originalFilename = uploadedFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".stl")) {
            return ResponseEntity.badRequest().body("Invalid file type. Only .stl is allowed");
        }
        try {
            // 2) 저장/파싱 로직 호출
            String resultPath = stlService.processStlFile(uploadedFile);
            // 필요 시, 결과물 정보(파일 경로, 변환 상태 등)를 JSON 형태로 응답
            return ResponseEntity.ok("STL file processed. Saved at: " + resultPath);
        } catch (IOException e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing STL file");
        }
    }


}
