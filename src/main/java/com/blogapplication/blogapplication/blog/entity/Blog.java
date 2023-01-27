package com.blogapplication.blogapplication.blog.entity;

import com.blogapplication.blogapplication.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name="description" , length = 5000)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer status;

    private Boolean featured;

    @ElementCollection // 1
    @CollectionTable(name = "image_list", joinColumns = @JoinColumn(name = "blog_id")) // 2
    @Column(name = "image") // 3
    private List<String> images;

    @ManyToOne
    private User createdBy;
}
