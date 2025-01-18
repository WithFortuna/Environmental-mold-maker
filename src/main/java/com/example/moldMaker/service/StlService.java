package com.example.moldMaker.service;

import com.example.moldMaker.constants.PathConstants;
import com.example.moldMaker.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class StlService {

    public byte[] processStlFile(byte[] stlFile) throws IOException, NotFoundException {
        if (stlFile == null || stlFile.length == 0) {
            throw new NotFoundException("Empty stl data");
        }
        // 1) 저장할 파일 경로 지정
        String directoryPath = PathConstants.MODEL2_PATH;
        String fileName = "model2.stl";
        Path stlSavePath = Paths.get(directoryPath, fileName);
        //2) 파일 저장(model2.stl)
        Files.write(stlSavePath, stlFile);

        try {
            String pythonPath = PathConstants.YOUR_PYTHON_PATH; // 가상환경의 Python 경로
            String pythonScriptPath = "src/main/java/com/example/moldMaker/pyvista/index.py";
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, pythonScriptPath);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            boolean volumeFlag = false;
            float volume = 0.0f;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print the output from the Python script
                try {
                    volume = Float.parseFloat(line.trim());
                    volumeFlag = true;
                } catch (NumberFormatException e) {
                    log.error("처리된 stl파일의 부피값이 실수가 아닙니다",e);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Python script executed with exit code: " + exitCode);

            //부피값을 파일로 저장
            if (volumeFlag) {
                String directoryPathVolume = PathConstants.RESULT_FOLDER_PATH;
                        String fileNameVolume="volume.txt";
                Path volumeFilePath = Paths.get(directoryPathVolume,fileNameVolume);
                //저장
                Files.write(volumeFilePath, String.valueOf(volume).getBytes());
            } else {
                log.warn("부피값을 구하는 과정에서 오류가 발생하였습니다.");
            }

            // output.stl 파일을 읽어서 byte[]로 변환
            Path stlPath = Paths.get(PathConstants.OUTPUTSTL_PATH);
            if (!Files.exists(stlPath)) {
                throw new NotFoundException("Output STL file not found");
            }
            byte[] convertedFile = Files.readAllBytes(stlPath);

//            String convertedFileString = pyResult.toString();
//            byte[] convertedFile = convertedFileString.getBytes(StandardCharsets.UTF_8);

            return convertedFile;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 2) STL 파일을 삭제하는 메서드
    public void deleteOutputStlFile() {
        Path stlPath = Paths.get(PathConstants.OUTPUTSTL_PATH);

        try {
            if (Files.exists(stlPath)) {
                Files.delete(stlPath);
                System.out.println("output.stl file deleted successfully.");
            } else {
                System.out.println("output.stl file does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete output.stl file");
        }
    }

    public void deleteInputFile() {
        Path stlPath = Paths.get(PathConstants.NOW_MODEL2_PATH);

        try {
            if (Files.exists(stlPath)) {
                Files.delete(stlPath);
                System.out.println("model2.stl file deleted successfully.");
            } else {
                System.out.println("model2.stl file does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete model2.stl file");
        }
    }

    public void saveOutputFile(byte[] outputFile) throws IOException {
        // 1) 저장할 파일 경로 지정
        String directoryPath = PathConstants.RESULT_FOLDER_PATH;

        String fileName = "output.stl";
        Path stlSavePath = Paths.get(directoryPath, fileName);
        //2) 파일 저장(model2.stl)
        Files.write(stlSavePath, outputFile);
    }
}

































/*

public byte[] processStlFile(byte[] stlFile) throws IOException, NotFoundException {
    if (stlFile == null || stlFile.length == 0) {
        throw new NotFoundException("Empty stl data");
    }

    try {
        String pythonScriptPath = "src/main/java/com/example/moldMaker/pyvista/index.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python3 ", pythonScriptPath);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder pyResult = new StringBuilder();

        String line;
        System.out.println("-------------------------------");
        while ((line = reader.readLine()) != null) {
            pyResult.append(line).append('\n');  //'\n'추가해야하는가
            System.out.println(line); // Print the output from the Python script
        }
        System.out.println("------------------------------");
        int exitCode = process.waitFor();
        System.out.println("Python script executed with exit code: " + exitCode);

        String convertedFileString = pyResult.toString();
        byte[] convertedFile = convertedFileString.getBytes(StandardCharsets.UTF_8);

        return convertedFile;
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        throw new RuntimeException();
    }
}
}
*/
