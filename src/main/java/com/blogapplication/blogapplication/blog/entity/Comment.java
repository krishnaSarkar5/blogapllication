package com.blogapplication.blogapplication.blog.entity;


import com.blogapplication.blogapplication.blog.dto.request.PostCommentRequestDto;
import com.blogapplication.blogapplication.blog.enums.Reaction;
import com.blogapplication.blogapplication.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    private Blog blog;

    @ManyToOne
    private User commentedBy;

    private LocalDateTime commentedAt;

    private LocalDateTime updatedAt;


    private Long referencedCommentId;

//    @OneToMany
//    private List<Comment> reply;


    private Integer status;

//    // 1 = comment, 2= reply
//    private Integer type;


}
