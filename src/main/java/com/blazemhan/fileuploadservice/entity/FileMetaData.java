package com.blazemhan.fileuploadservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaData {

    @Id
    private String id;
    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String storedFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    }

