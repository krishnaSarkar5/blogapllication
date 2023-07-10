package com.blogapplication.blogapplication.common.cloudservice.service;

import com.blogapplication.blogapplication.common.utility.Base64Converter;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile multipartFile,String folder) throws IOException {

        Map params = ObjectUtils.asMap(
                "public_id", UUID.randomUUID().toString(),
                "folder", folder,
                "resource_type", "image"
        );

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        params)
                .get("url")
                .toString();
    }



    public String uploadImage(String base64Code,String fileName,String folder){

        MultipartFile multipartFile = Base64Converter.convertBase64ToMultipartFile(base64Code, fileName);

        String imageUrl="";

        try {
            imageUrl=this.uploadFile(multipartFile,folder);
        } catch (IOException e) {
            imageUrl=base64Code;
//            throw new RuntimeException(e);
        }
        return imageUrl;
    }
}
