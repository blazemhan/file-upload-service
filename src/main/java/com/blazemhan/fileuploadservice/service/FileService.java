package com.blazemhan.fileuploadservice.service;

import com.blazemhan.fileuploadservice.entity.FileMetaData;
import com.blazemhan.fileuploadservice.exceptions.FileNotFoundException;
import com.blazemhan.fileuploadservice.exceptions.FileValidationException;
import com.blazemhan.fileuploadservice.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final String uploadDir = "uploads/";
    private final VirusScannerService  virusScannerService;

    public FileService(FileRepository fileRepository, VirusScannerService virusScannerService) {
        this.fileRepository = fileRepository;
        this.virusScannerService = virusScannerService;
    }

    public FileMetaData uploadFile(MultipartFile file) throws IOException {

        validate(file);
        virusScannerService.scan(file);

        String id = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        String storedFilename = id + "." + extension;

        Path path = Paths.get(uploadDir, storedFilename);
        Files.createDirectories(path.getParent());

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        FileMetaData metadata = FileMetaData.builder()
                .id(id)
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFilename)
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();

        return fileRepository.save(metadata);

    }
    public Resource getFile(String id) throws IOException {

        FileMetaData metadata = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        Path path = Paths.get(uploadDir, metadata.getStoredFileName());

        if (!Files.exists(path)) {
            throw new java.io.FileNotFoundException("File missing on disk");
        }

        return new UrlResource(path.toUri());
    }

    public void deleteFile(String id) throws IOException {

        FileMetaData metadata = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        Path path = Paths.get(uploadDir, metadata.getStoredFileName());
        Files.deleteIfExists(path);

        fileRepository.deleteById(id);
    }

    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileNotFoundException("File is empty or missing. Please upload a valid file.");
        }

        if (file.getOriginalFilename() == null|| file.getOriginalFilename().isBlank()) {
            throw new FileValidationException("File must have a valid filename.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new FileValidationException(
                    "File size (" + formatFileSize(file.getSize()) + ") exceeds the maximum allowed limit of 5MB.");
        }

        String ext = getExtension(file.getOriginalFilename()).toLowerCase();

        if (!List.of("jpg", "png", "pdf").contains(ext)) {
            throw new FileValidationException("Only JPG, PNG, PDF allowed");
        }
    }


    private String getExtension(String filename) {
        if (!filename.contains(".")) {
            throw new FileValidationException("File has no extension");
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}
