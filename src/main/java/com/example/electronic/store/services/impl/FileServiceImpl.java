package com.example.electronic.store.services.impl;

import com.example.electronic.store.exceptions.BadApiRequestException;
import com.example.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("originalFilename : {}", originalFilename);

        String filename = UUID.randomUUID().toString();
        //----Here we are getting .png from abc.png
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileNameWithExtension = filename + extension;
        logger.info("fileNameWithExtension : {}", fileNameWithExtension);

        String fullPathWithFileName = path + File.separator + fileNameWithExtension;
        logger.info("fullPathWithFileName : {}", fullPathWithFileName);

        if (extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")
                || extension.equalsIgnoreCase(".png")) {

            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequestException(" Invalid Image format" + extension);
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fileNameWithPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fileNameWithPath);
        return inputStream;
    }
}
