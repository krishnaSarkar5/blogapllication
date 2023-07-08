package com.blogapplication.blogapplication.blog.repositoty;

import com.blogapplication.blogapplication.blog.entity.BlogViewDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BlogViewDetailsRepository extends JpaRepository<BlogViewDetails,Long> {

    public Optional<BlogViewDetails> findFirstByBlogIdAndViewedByIdOrderByViewedAtDesc(Long blogId, Long userId);

    public Integer countByBlogId(Long blogId);

    @Query(value = "select count(distinct(viewed_by_id)) from blog_view_details where blog_id = ?1",nativeQuery = true)
    public Integer countDistinctUserIdAndId(Long blogId);
    @Query(value = "select count(distinct(viewed_by_id)) as views ,blog_id from blog_view_details where blog_id in ?1 group by blog_id ",nativeQuery = true)
    public List<Tuple> countOfViewsByBlogId(List<Long> blogId);


//    select * from (select count(blog_id) as total_view ,blog_id
//    from blog_view_details
//    where viewed_at between '2023-05-08 00:03:35.923062' and now()
//    group by blog_id order by total_view desc , blog_id asc limit 5 ) temp
//    inner join blog
//    on temp.blog_id = blog.id

    @Query(value = "    select * from (select count(blog_id) as total_view ,blog_id " +
            "    from blog_view_details " +
            "    where viewed_at between ?1 and ?2 " +
            "    group by blog_id order by total_view desc , blog_id asc limit ?3 ) temp " +
            "    inner join blog " +
            "    on temp.blog_id = blog.id",nativeQuery = true)
    public List<Tuple> getAllTrendingBlogBetweenDateRange(LocalDateTime from , LocalDateTime to,Integer limit);


}
