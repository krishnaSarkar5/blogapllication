package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    public Optional<Comment> findByIdAndStatus(Long commentId, Integer status);

//    public List<Comment> findByBlogIdAndStatus(Long blogId,Integer status,Integer type);

    public List<Comment> findByBlogIdAndStatusAndReferencedCommentId(Long blogId, Integer status, Long refenecedCommentId);

    List<Comment> findByReferencedCommentIdAndStatus(Long blogId,Integer status);
}
