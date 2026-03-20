package com.blazemhan.fileuploadservice.controller;

import com.blazemhan.fileuploadservice.entity.FileMetaData;
import com.blazemhan.fileuploadservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a file",
            description = "Upload a file (JPG, PNG, or PDF). Maximum size: 5MB."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "File uploaded successfully",
                    content = @Content(schema = @Schema(implementation = FileMetaData.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file (wrong type, empty, etc.)"),
            @ApiResponse(responseCode = "413", description = "File size exceeds 5MB limit")
    })
    public ResponseEntity<FileMetaData> upload(@RequestParam("file") MultipartFile file) throws IOException, IOException {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve file metadata by ID",
            description = "Returns the metadata of a previously uploaded file."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File metadata retrieved",
                    content = @Content(schema = @Schema(implementation = FileMetaData.class))),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Resource> get(@PathVariable String id) throws IOException {

        Resource file = fileService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .body(file);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a file by ID",
            description = "Deletes both the file from disk and its metadata from the database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File deleted successfully"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<String> delete(@PathVariable String id) throws IOException {
        fileService.deleteFile(id);
        return ResponseEntity.ok("File deleted");
    }
}
