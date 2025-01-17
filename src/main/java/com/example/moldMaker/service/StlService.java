package com.example.moldMaker.service;

import com.example.moldMaker.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

        try {
            String pythonScriptPath = "src/main/java/com/example/moldMaker/pyvista/index.py";
            ProcessBuilder processBuilder = new ProcessBuilder("python3 ", pythonScriptPath);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print the output from the Python script
            }

            int exitCode = process.waitFor();
            System.out.println("Python script executed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        byte[] convertedStlData = stlFile;

        return convertedStlData;
    }
}
