package com.blogapplication.blogapplication.common;

import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import com.blogapplication.blogapplication.blog.repositoty.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public void insertDataToCategoryTable(){


        List<BlogCategory> all = categoryRepository.findAll();

        if(all.isEmpty()){
            BlogCategory category = new BlogCategory();

            List<BlogCategory> categories = List.of(
                    new BlogCategory(1l,"Travel",travel, LocalDateTime.now(),1),
                    new BlogCategory(2l,"Health",health, LocalDateTime.now(),1),
                    new BlogCategory(3l,"Politics",politics, LocalDateTime.now(),1),
                    new BlogCategory(4l,"Science",science, LocalDateTime.now(),1),
                    new BlogCategory(5l,"Food",food, LocalDateTime.now(),1)

            );
            categoryRepository.saveAll(categories);
        }




    }
}
