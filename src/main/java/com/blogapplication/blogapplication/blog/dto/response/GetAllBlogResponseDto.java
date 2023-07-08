package com.blogapplication.blogapplication.blog.dto.response;

import com.blogapplication.blogapplication.blog.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBlogResponseDto {


    private Long id;

    private String title;

    private String description;

    private String createdAt;

    private Integer status;

    private Boolean featured;

    private Boolean trending;

    private String image;

    private Long createdBy;

    private Integer ownReaction;

    private boolean edited;

    private Integer views;

    private Integer reactionCount;


    public GetAllBlogResponseDto(Blog blog){

        this.id = blog.getId();

        this.title = blog.getTitle();

        this.description = blog.getDescription();

        this.createdAt = blog.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        this.status = blog.getStatus();

        this.featured = blog.getFeatured();

        this.image = blog.getImage();

        this.createdBy = blog.getCreatedBy().getId();

//        this.ownReaction = b;

        this.edited = blog.getCreatedAt().equals(blog.getUpdatedAt());



    }


    public GetAllBlogResponseDto(Tuple blogTuple){

        this.id = Long.valueOf(String.valueOf(blogTuple.get("id")));

        this.title = String.valueOf(blogTuple.get("title"));

        this.description = String.valueOf(blogTuple.get("description"));

        this.createdAt = parseDateTime(String.valueOf(blogTuple.get("created_at")),"yyyy-MM-dd HH:mm:ss.SSSSSS").format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        this.status = Integer.parseInt(String.valueOf(blogTuple.get("status")));

        this.featured = Boolean.parseBoolean(String.valueOf(blogTuple.get("featureD")));

        this.image = String.valueOf(blogTuple.get("image"));

        this.createdBy = Long.parseLong(String.valueOf(blogTuple.get("created_by_id")));

//        this.ownReaction = b;

        this.edited = parseDateTime(String.valueOf(blogTuple.get("created_at")),"yyyy-MM-dd HH:mm:ss.SSSSSS").equals(parseDateTime(String.valueOf(blogTuple.get("updated_at")),"yyyy-MM-dd HH:mm:ss.SSSSSS"));



    }


// date format method
    private static LocalDateTime parseDateTime(String dateTimeString, String pattern) {
        if (dateTimeString.length() == 26) {
            // Handle six digits for fractional seconds
            dateTimeString = dateTimeString.substring(0, 26);
        } else if (dateTimeString.length() == 25) {
            // Handle five digits for fractional seconds
            dateTimeString = dateTimeString + "0";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

}
