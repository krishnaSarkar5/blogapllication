package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.BlogReactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogReactionDetailsRepository extends JpaRepository<BlogReactionDetails,Long> {

    public Optional<BlogReactionDetails> findByBlogIdAndReactedById(Long blogId, Long userId);

    public Optional<BlogReactionDetails> findByBlogIdAndReactedByIdAndIsReacted(Long blogId, Long userId,boolean isReacted);
}
