package com.blogapplication.blogapplication.firebaseservice;

import com.blogapplication.blogapplication.common.utility.Base64Converter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

//@Service
public class ImageService {

//    @Autowired
    private Storage storage;


    public String uploadImage(String base64code){
        try {
            // Convert the Base64 image to MultipartFile
            MultipartFile imageFile = Base64Converter.convertBase64ToMultipartFile(base64code,"abc");

            // Generate a unique filename for the image
            String filename = "blog-image/"+UUID.randomUUID().toString();

            // Set the bucket name and create a BlobId
            String bucketName = "blog-application-ec7cc.appspot.com";
            BlobId blobId = BlobId.of(bucketName, filename);

            // Convert the MultipartFile to a BlobInfo
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(imageFile.getContentType())
                    .build();

            // Upload the image to Firebase Storage
            Blob blob = storage.create(blobInfo, imageFile.getBytes());

            // Get the public URL of the uploaded image
            String imageUrl = blob.getMediaLink();


            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Image upload failed";
        }
    }



}
