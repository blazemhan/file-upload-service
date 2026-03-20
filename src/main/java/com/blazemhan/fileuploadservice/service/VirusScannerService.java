package com.blazemhan.fileuploadservice.service;

import com.blazemhan.fileuploadservice.exceptions.VirusScanException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;

@Service
public class VirusScannerService {
    //private static final Logger logger = (Logger) LoggerFactory.getLogger(VirusScannerService.class);


    public void scan(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();


        if (originalFilename != null && originalFilename.toLowerCase().contains("virus")) {

            throw new VirusScanException(
                    "File '" + originalFilename + "' failed virus scan and was rejected.");
        }


    }
}