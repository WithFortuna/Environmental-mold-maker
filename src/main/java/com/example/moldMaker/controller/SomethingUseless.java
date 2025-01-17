/*
package com.example.moldMaker.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SomethingUseless {

    //변환 파일 전송
    @GetMapping("/download")
    public ResponseEntity<Resource> sendConvertedFile(@RequestParam("fileName") String fileName) {
        try {
            // 서비스 레이어에서 실제 파일이 저장된 경로 반환
            Path filePath = Paths.get(stlService.getConvertedFilePath(fileName));
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            // 파일 유형(MIME) 설정 (필요에 따라 적절히 변경)
            String contentType = "application/octet-stream";

            // Content-Disposition 헤더를 통해 파일 다운로드 설정
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
*/
