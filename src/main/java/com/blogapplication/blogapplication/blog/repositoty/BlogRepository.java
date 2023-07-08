package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    public Optional<Blog> findByIdAndStatus(Long blogId,Integer status);


    List<Blog> findAll(Specification<Blog> specification, Pageable pageable);

    List<Blog> findAll(Specification<Blog> specification);

    Page<Blog> findAll(Pageable pageable);


    //    public Integer countByDistinctUserIdAndId(Long blogId);

//    countDistinctByUserIdAndId
}
