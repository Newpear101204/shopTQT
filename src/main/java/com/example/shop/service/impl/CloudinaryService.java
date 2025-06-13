package com.example.shop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service

public class CloudinaryService {

    private  Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "dmkxepkiw");
        valuesMap.put("api_key", "248851859778315");
        valuesMap.put("api_secret", "hFEwWUlpvqSDOG1PP5VlZ9oL9TU");
        cloudinary = new Cloudinary(valuesMap);
    }

    public List<String> uploadMultipleFiles(MultipartFile[] multipartFiles) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            BufferedImage bi = ImageIO.read(file.getInputStream());
            if (bi == null) {
                throw new IOException("Một trong các file ảnh là null hoặc không hợp lệ.");
            }

            // Upload file lên Cloudinary
            Map<String, Object> result = uploadFile(file);
            String fileUrl = (String) result.get("url");

            uploadedUrls.add(fileUrl);
        }

        return uploadedUrls;
    }

    public Map<String, Object> uploadFile(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        try {
            Map<String, Object> result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return result;
        } finally {
            if (!Files.deleteIfExists(file.toPath())) {
                throw new IOException("Không thể xóa file tạm: " + file.getAbsolutePath());
            }
        }
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("upload-", ".tmp");
        try (FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(multipartFile.getBytes());
        }
        return file;
    }

}
