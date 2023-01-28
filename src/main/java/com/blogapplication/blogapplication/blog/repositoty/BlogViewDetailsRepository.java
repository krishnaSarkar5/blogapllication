package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.BlogViewDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogViewDetailsRepository extends JpaRepository<BlogViewDetails,Long> {

    public Optional<BlogViewDetails> findByBlogIdAndViewedById(Long blogId,Long userId);

    public Integer countByBlogId(Long blogId);
}
