package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    public Optional<Comment> findByIdAndStatusAndType(Long commentId, Integer status, Integer type);

    public List<Comment> findByBlogIdAndStatusAndType(Long blogId,Integer status,Integer type);
}
