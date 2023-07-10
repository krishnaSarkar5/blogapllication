package com.blogapplication.blogapplication.common.demoController;

import com.blogapplication.blogapplication.common.cloudservice.service.UploadFileService;
import com.blogapplication.blogapplication.common.dto.demodto.ImageUploadDto;
import com.blogapplication.blogapplication.firebaseservice.ImageService;
import io.swagger.annotations.Authorization;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/demo")
public class DemoController {


//    @Autowired
    private ImageService imageService;

    @Autowired
    private UploadFileService uploadFile;

    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }

//    @PostMapping("/upload-image-test")
//    public String uploadImage(@RequestBody ImageUploadDto imageUploadDto){
//        return imageService.uploadImage(imageUploadDto.getBase64code());
//    }


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("image")MultipartFile multipartFile) throws IOException {
        String imageURL = uploadFile.uploadFile(multipartFile,"Blog category");
        return imageURL;
    }
}
