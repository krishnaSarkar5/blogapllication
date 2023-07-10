package com.blogapplication.blogapplication.common;

import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import com.blogapplication.blogapplication.blog.repositoty.CategoryRepository;
import com.blogapplication.blogapplication.common.cloudservice.service.UploadFileService;
import com.blogapplication.blogapplication.common.utility.Base64Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class InsertTableBean {

    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${food}")
    private String food;

    @Value("${health}")
    private String health;

    @Value("${travel}")
    private String travel;

    @Value("${science}")
    private String science;

    @Value("${politics}")
    private String politics;

    @Autowired
    private UploadFileService uploadFileService;


    @Bean
    public void insertDataToCategoryTable(){


        List<BlogCategory> all = categoryRepository.findAll();



        if(all.isEmpty()){
            BlogCategory category = new BlogCategory();

//            String foodImagePath =


            List<BlogCategory> categories = List.of(
                    new BlogCategory(1l,"Travel",uploadImage(travel,"travel"), LocalDateTime.now(),1),
                    new BlogCategory(2l,"Health",uploadImage(health,"health"), LocalDateTime.now(),1),
                    new BlogCategory(3l,"Politics",uploadImage(politics,"politics"), LocalDateTime.now(),1),
                    new BlogCategory(4l,"Science",uploadImage(science,"science"), LocalDateTime.now(),1),
                    new BlogCategory(5l,"Food",uploadImage(food,"food"), LocalDateTime.now(),1)

            );
            categoryRepository.saveAll(categories);
        }




    }

    private String uploadImage(String base64Code,String fileName){

        MultipartFile multipartFile = Base64Converter.convertBase64ToMultipartFile(base64Code, fileName);

        String imageUrl="";

        try {
            imageUrl=uploadFileService.uploadFile(multipartFile,"Blog category");
        } catch (IOException e) {
            imageUrl=base64Code;
//            throw new RuntimeException(e);
        }
        return imageUrl;
    }
}
