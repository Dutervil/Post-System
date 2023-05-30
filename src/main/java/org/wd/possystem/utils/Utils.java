package com.wadson.pos.org.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

public class Utils {

    private static Path foundFile;

    private Utils(){

    }
 public static ResponseEntity<?> getResponseEntity(String responseMessage, HttpStatus httpStatus){
     return new ResponseEntity<>(Map.of("message",responseMessage),httpStatus);
 }

 public static String getCode(){
        return ""+(int)(Math.random()*10000)+1+"-"+(int)(Math.random()*10000000)+1;
 }

    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadDirectory= Paths.get("Files-Upload");
        String fileCode= UUID.randomUUID().toString();
        try {
            InputStream is=multipartFile.getInputStream();
            Path filePath=uploadDirectory.resolve(fileCode+"_"+fileName);
            Files.copy(is,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Error saving uploaded file : " +fileName,e);
        }
        return fileCode+"_"+fileName;
    }


    public static Resource download(String fileName) throws IOException {
        Path uploadDirectory= Paths.get("Files-Upload");
        String fileCode= UUID.randomUUID().toString();
        Files.list(uploadDirectory).forEach(file->{
            if (file.getFileName().toString().startsWith(fileCode)){
                foundFile=file;
                return;
            }
        });

        if (foundFile!=null){
            return new UrlResource(foundFile.toUri());
        }
        return null;
    }
}
