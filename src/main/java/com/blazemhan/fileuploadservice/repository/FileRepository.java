package com.blazemhan.fileuploadservice.repository;

import com.blazemhan.fileuploadservice.entity.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetaData, String> {
}
