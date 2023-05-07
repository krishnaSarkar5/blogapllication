package com.blogapplication.blogapplication.blog.entity;


import com.blogapplication.blogapplication.blog.dto.request.SaveCategoryRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blog_category")
public class BlogCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String image;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Integer status;


    public static BlogCategory getCategoryInstanceFromDyo(SaveCategoryRequestDto request){
        return BlogCategory.builder()
                .title(request.getTitle())
                .image(request.getImage())
                .build();
    }
}
