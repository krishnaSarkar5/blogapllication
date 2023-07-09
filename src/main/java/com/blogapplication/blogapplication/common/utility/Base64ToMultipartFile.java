package com.blogapplication.blogapplication.common.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64ToMultipartFile implements MultipartFile {

	private final byte[] fileContent;

    private final String extension;
    private final String contentType;
   
    private String fileName;
    

    public Base64ToMultipartFile(String base64, String dataUri, String fileName) {
        this.fileContent = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        this.extension = dataUri.split(";")[0].split("/")[1];
        this.contentType = dataUri.split(";")[0].split(":")[1];
        this.fileName= fileName;
        
        
        
    }

    public Base64ToMultipartFile(String base64, String extension, String contentType, String fileName) {
        this.fileContent = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        this.extension = extension;
        this.contentType = contentType;
        
        this.fileName= fileName;
    }

    @Override
    public String getName() {
        return "param_" + System.currentTimeMillis();
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName+ "." + extension;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileContent);
        }
    }

}
