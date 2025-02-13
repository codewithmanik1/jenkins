package com.wave.matrix.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class FileUtil {

    public static void createDirectoryIfNotExists(String directoryPath) {
        System.out.println("Check Path is create !!!");
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String saveImage(String base64Image, String imageName, String uploadDir) throws IOException {
        System.out.println("Images saved in doirectory !!!!!!!!!!!!!!1");
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        String filePath = uploadDir + File.separator + imageName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(decodedBytes);
        }

        return filePath;
    }

    public static String convertImageToBase64(String imagePath) throws IOException {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }

        File file = new File(imagePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
