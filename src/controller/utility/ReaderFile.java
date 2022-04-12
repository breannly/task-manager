package controller.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ReaderFile {
    public static String readFileContents(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
