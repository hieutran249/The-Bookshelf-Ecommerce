package com.hieutt.ecommerceweb.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER = "C:\\Users\\PC\\OneDrive - Hanoi University\\Desktop\\study\\Spring Boot\\ecommerce-web\\src\\main\\resources\\static\\img\\img-books";

    // copy image to UPLOAD_FOLDER
    public boolean upload(MultipartFile image){
        try {
            Files.copy(image.getInputStream(),
                    Paths.get(UPLOAD_FOLDER + File.separator + image.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkExistence(MultipartFile image) {
        File file = new File(UPLOAD_FOLDER + "\\" + image.getOriginalFilename());
        return file.exists();
    }
}
