package com.blogapplication.blogapplication.common.utility;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Converter {

//    public static MultipartFile convertBase64ToMultipartFile(String base64Image) throws IOException {
//
//
//
//        // Decode the Base64 image
//        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
//
//        // Generate a unique filename for the image
//        String filename = "image.jpg"; // You can use any desired filename or modify this logic
//
//        // Create an InputStream from the decoded bytes
//        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
//
//        // Create a MultipartFile from the InputStream
//        return new CustomMultipartFile(inputStream, filename);
//    }



    public static MultipartFile convertBase64ToMultipartFile(String base64,String fileName) {

        String dataUir="";
        String   data="";

        final String[] base64Array = base64.split(",");

        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {

            dataUir = "data:image/png;base64";
            data = base64Array[0];
        }

        return new Base64ToMultipartFile(data, dataUir,fileName);

    }


    private static class CustomMultipartFile implements MultipartFile {

        private final InputStream inputStream;
        private final String filename;

        public CustomMultipartFile(InputStream inputStream, String filename) {
            this.inputStream = inputStream;
            this.filename = filename;
        }

        @Override
        public String getName() {
            return null; // Implement if needed
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return null; // Implement if needed
        }

        @Override
        public boolean isEmpty() {
            return false; // Implement if needed
        }

        @Override
        public long getSize() {
            try {
                return inputStream.available();
            } catch (IOException e) {
                return 0;
            }
        }

        @Override
        public byte[] getBytes() throws IOException {
            return StreamUtils.copyToByteArray(inputStream);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            // Implement if needed
        }
    }
}

