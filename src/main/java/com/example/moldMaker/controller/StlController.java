package com.example.moldMaker.controller;

import com.example.moldMaker.exception.NotFoundException;
import com.example.moldMaker.service.StlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class StlController {

    private final StlService stlService;

    public StlController(StlService stlService) {
        this.stlService = stlService;
    }

    @PostMapping(
            value = "/upload",
            consumes = "model/stl",
            produces = "application/json"
    )
    public ResponseEntity<Map<String, Object>> handleStlFile(@RequestBody byte[] uploadedFile) {
        try {
            StlService.StlProcessingResult result = stlService.processStlFile(uploadedFile);

            Map<String, Object> response = new HashMap<>();
            response.put("fileContent", Base64.getEncoder().encodeToString(result.getConvertedFile())); // Base64 encoded file
            response.put("volume", result.getVolume()); // Volume included directly

            // Clean up temporary files
            stlService.deleteOutputStlFile();
            stlService.deleteInputFile();

            return ResponseEntity.ok(response);
        } catch (IOException | NotFoundException e) {
            log.error("Error processing STL file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "File processing error"));
        }
    }
}