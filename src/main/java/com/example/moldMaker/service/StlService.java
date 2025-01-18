package com.example.moldMaker.service;

import com.example.moldMaker.constants.PathConstants;
import com.example.moldMaker.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class StlService {

    public static class StlProcessingResult {
        private final byte[] convertedFile;
        private final float volume;

        public StlProcessingResult(byte[] convertedFile, float volume) {
            this.convertedFile = convertedFile;
            this.volume = volume;
        }

        public byte[] getConvertedFile() {
            return convertedFile;
        }

        public float getVolume() {
            return volume;
        }
    }

    public StlProcessingResult processStlFile(byte[] stlFile) throws IOException, NotFoundException {
        if (stlFile == null || stlFile.length == 0) {
            throw new NotFoundException("Empty STL data");
        }

        String directoryPath = PathConstants.MODEL2_PATH;
        String fileName = "model2.stl";
        Path stlSavePath = Paths.get(directoryPath, fileName);

        // Save the input STL file
        Files.write(stlSavePath, stlFile);

        try {
            String pythonPath = PathConstants.YOUR_PYTHON_PATH; // Path to Python interpreter
            String pythonScriptPath = "src/main/java/com/example/moldMaker/pyvista/index.py";
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, pythonScriptPath);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            float volume = 0.0f;
            boolean volumeFlag = false;

            while ((line = reader.readLine()) != null) {
                try {
                    volume = Float.parseFloat(line.trim());
                    volumeFlag = true;
                } catch (NumberFormatException e) {
                    log.error("Failed to parse volume as a float from Python script output: {}", line, e);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script execution failed with exit code: " + exitCode);
            }

            if (!volumeFlag) {
                throw new RuntimeException("Volume value was not successfully calculated.");
            }

            // Read the output STL file
            Path outputStlPath = Paths.get(PathConstants.OUTPUTSTL_PATH);
            if (!Files.exists(outputStlPath)) {
                throw new NotFoundException("Output STL file not found");
            }

            byte[] convertedFile = Files.readAllBytes(outputStlPath);

            return new StlProcessingResult(convertedFile, volume);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during STL processing", e);
        }
    }

    public void deleteOutputStlFile() {
        Path stlPath = Paths.get(PathConstants.OUTPUTSTL_PATH);

        try {
            if (Files.exists(stlPath)) {
                Files.delete(stlPath);
                log.info("output.stl file deleted successfully.");
            } else {
                log.info("output.stl file does not exist.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete output.stl file", e);
        }
    }

    public void deleteInputFile() {
        Path stlPath = Paths.get(PathConstants.NOW_MODEL2_PATH);

        try {
            if (Files.exists(stlPath)) {
                Files.delete(stlPath);
                log.info("model2.stl file deleted successfully.");
            } else {
                log.info("model2.stl file does not exist.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete model2.stl file", e);
        }
    }
}