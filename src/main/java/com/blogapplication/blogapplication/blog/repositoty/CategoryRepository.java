package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<BlogCategory,Long> {

    List<BlogCategory> findAllByStatus(Integer status);


    Optional<BlogCategory> findByIdAndStatus(Long id , Integer status);
}
