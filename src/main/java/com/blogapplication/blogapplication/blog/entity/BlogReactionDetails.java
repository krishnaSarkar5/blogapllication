package com.blogapplication.blogapplication.blog.entity;

import com.blogapplication.blogapplication.blog.enums.Reaction;
import com.blogapplication.blogapplication.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class BlogReactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Blog blog;

    @ManyToOne
    private User reactedBy;

    private LocalDateTime reactedAt;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    private Boolean isReacted;
}
