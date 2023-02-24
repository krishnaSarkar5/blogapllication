package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.BlogViewDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlogViewDetailsRepository extends JpaRepository<BlogViewDetails,Long> {

    public Optional<BlogViewDetails> findFirstByBlogIdAndViewedByIdOrderByViewedAtDesc(Long blogId, Long userId);

    public Integer countByBlogId(Long blogId);

    @Query(value = "select count(distinct(viewed_by_id)) from blog_view_details where blog_id = ?1",nativeQuery = true)
    public Integer countDistinctUserIdAndId(Long blogId);
}
