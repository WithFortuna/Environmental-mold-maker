package com.example.moldMaker.controller;

import com.example.moldMaker.constants.PathConstants;
import com.example.moldMaker.exception.NotFoundException;
import com.example.moldMaker.service.StlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
/*
* Http content-type:
* 파일 수신: multipart/form-data => model/stl
* 파일 송신: application/octet-stream => model/stl
*
* */

@Slf4j
@RestController
@RequestMapping("/api")
public class StlController {

    private final StlService stlService;

    public StlController(StlService stlService) {
        this.stlService = stlService;
    }

    /*
    * model/stl 타입으로 송신 및 수신
    * (.stl파일, .txt)파일 경로 반환
    * */
    @PostMapping(
            value = "/upload",
            consumes = "model/stl", //수신 type
            produces = "application/json"   //송신 type
    )
    public ResponseEntity<Map<String, Object>> handleStlFile(@RequestBody byte[] uploadedFile) {
        try {
            // 1. 변환 로직 호출
            StlService.StlProcessingResult result = stlService.processStlFile(uploadedFile);
/*
            // 2. output파일 저장
            stlService.saveOutputFile(convertedFile);
*/


            Map<String, Object> response = new HashMap<>();
            response.put("fileContent", Base64.getEncoder().encodeToString(result.getConvertedFile())); // Base64 encoded file
            response.put("volume", result.getVolume()); // Volume included directly

            //교체 파일들 삭제
            stlService.deleteOutputStlFile();
            stlService.deleteInputFile();


            return ResponseEntity.ok(response);
        } catch (IOException | NotFoundException e) {
            // 예외 처리
            log.error("I/O eroor occured ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "File processing error"));
        }
    }





    @PostMapping(
            value = "/upload/test",
            consumes = "model/stl", //수신 type
            produces = "model/stl"  //송신 type

    )
    public ResponseEntity<ByteArrayResource> testfunc(@RequestBody byte[] uploadedFile) throws IOException {


            // 2) 저장/변환 로직 호출
//            byte[] convertedFile = stlService.processStlFile(uploadedFile);
            ByteArrayResource resource = new ByteArrayResource(uploadedFile);
            stlService.processStlFile(uploadedFile);

            //교체 파일들 삭제
            stlService.deleteOutputStlFile();
            stlService.deleteInputFile();

            String fileName = resource.getFilename();
            String contentType = "model/stl";

            // 3) 변환stl파일 전송
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);


    }












    //ver1: 파일 수신 및 송신 결합
    //1-1 multipart/form-data 타입으로 수신
    /*
    @PostMapping("/upload")
    public ResponseEntity<Resource> uploadStlFile(@RequestParam("uploadedFile") MultipartFile uploadedFile) {
        // 1) 수신 및 파일 유효성 검사
        String originalFilename = uploadedFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".stl")) {
            return ResponseEntity.badRequest().build();                                         //bad request 던짐
        }
        try {
            // 2) 저장/변환 로직 호출
            Path convertedFilePath = stlService.processStlFile(uploadedFile);
            Resource resource = new UrlResource(convertedFilePath.toUri());
            if (!resource.exists()) {
                throw new NotFoundException(convertedFilePath.toString());
            }

            String fileName = resource.getFilename();
            String contentType = "model/stl";

            // 3) 변환stl파일 전송
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (IOException e) {
            // 예외 처리
            log.error("I/O eroor occured ", e);
            return ResponseEntity.notFound().build();                                       //404 not found 던짐
        } catch (NotFoundException e) {
            log.error("Error occured. Processing file is finished but error occured when fetch convertedFile, 파일위치: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

/* ver2: 파일 수신과 송신을 분리

    //업로드 파일 수신
    @PostMapping("/upload")
    public ResponseEntity<Resource> uploadStlFile(@RequestParam("uploadedFile") MultipartFile uploadedFile) {
        // 1) 파일 유효성 검사
        String originalFilename = uploadedFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".stl")) {
            return ResponseEntity.badRequest().body("Invalid file type. Only .stl is allowed");
        }
        try {
            // 2) 저장/파싱 로직 호출
            Path convertedFilePath = stlService.processStlFile(uploadedFile);
            Resource resource = new UrlResource(convertedFilePath.toUri());
            if (!resource.exists()) {
                throw new NotFoundException(convertedFilePath.toString());
            }

            // 필요 시, 결과물 정보(파일 경로, 변환 상태 등)를 JSON 형태로 응답
            return ResponseEntity.ok("STL file processed. Saved at: " + convertedFilePath);
        } catch (IOException e) {
            // 예외 처리
            log.error("I/O eroor occured ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing STL file");
        } catch (NotFoundException e) {
            log.error("Error occured. Processing file is finished but error occured when fetch convertedFile, 파일위치: {}", e.getMessage(), e);
        }
    }
*/


    /*


    //1-2 model/stl 타입으로 수신 & .stl파일 반환
    @PostMapping(
            value = "/upload/v1",
            consumes = "model/stl", //수신 type
            produces = "model/stl"  //송신 type

    )
    public ResponseEntity<ByteArrayResource> handleStlFile1(@RequestBody byte[] uploadedFile) {

        try {
            // 2) 저장/변환 로직 호출
            byte[] convertedFile = stlService.processStlFile(uploadedFile);
            ByteArrayResource resource = new ByteArrayResource(convertedFile);

            String fileName = resource.getFilename();
            //교체 파일들 삭제
            stlService.deleteOutputStlFile();
            stlService.deleteInputFile();

            String contentType = "model/stl";
            // 3) 변환stl파일 전송
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (IOException e) {
            // 예외 처리
            log.error("I/O error occured ", e);
            return ResponseEntity.notFound().build();                                       //404 not found 던짐
        }
    }
*/

}