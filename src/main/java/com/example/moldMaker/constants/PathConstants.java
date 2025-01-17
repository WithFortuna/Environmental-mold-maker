package com.example.moldMaker.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathConstants {
    //위치: StlController 클래스, handleStlFile2함수
    public static final Path RESULT_FOLDER_PATH = Paths.get("src/main/java/com/example/moldMaker/presentBox");
    public static final String MODEL2_PATH = "src/main/java/com/example/moldMaker/pyvista";
    public static final String YOUR_PYTHON_PATH = "/Users/yjk/Developer/Environmental-mold-maker/src/main/java/com/example/moldMaker/pyvista/.venv/bin/python";
    public static final String OUTPUTSTL_PATH = "src/main/java/com/example/moldMaker/pyvista/output.stl";
    public static final String NOW_MODEL2_PATH = "src/main/java/com/example/moldMaker/pyvista/model2.stl";

}

