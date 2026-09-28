package com.sharesphere.service.impl;

import com.sharesphere.exception.ShareSphereException;
import com.sharesphere.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service @Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try { Files.createDirectories(uploadPath); }
        catch (IOException e) { log.error("Could not create upload dir: {}", uploadPath, e); }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ShareSphereException("File is empty", HttpStatus.BAD_REQUEST);
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) ext = original.substring(original.lastIndexOf("."));
        String filename = UUID.randomUUID() + ext;
        try {
            Path target = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new ShareSphereException("Failed to store file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteFile(String imageUrl) {
        if (imageUrl == null) return;
        String filename = imageUrl.replace("/uploads/", "");
        try { Files.deleteIfExists(uploadPath.resolve(filename)); }
        catch (IOException e) { log.warn("Could not delete file: {}", filename); }
    }
}
