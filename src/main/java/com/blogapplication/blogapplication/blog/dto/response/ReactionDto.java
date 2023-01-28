package com.blogapplication.blogapplication.blog.dto.response;

import com.blogapplication.blogapplication.blog.entity.BlogReactionDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.core.env.Environment;

@Data
public class ReactionDto {

    private Long blogId;

    private Long userId;

    private Integer reaction;


    @JsonIgnore
    public ReactionDto(BlogReactionDetails reactionDetails,Environment environment){

        this.blogId=reactionDetails.getBlog().getId();
        this.userId=reactionDetails.getReactedBy().getId();


        switch (reactionDetails.getReaction()){

            case LIKE:{
                this.reaction=Integer.parseInt(environment.getProperty("like"));
                break;
            }

            case LOVE:{
                this.reaction=Integer.parseInt(environment.getProperty("love"));
                break;
            }

            case SUPPORT:{
                this.reaction=Integer.parseInt(environment.getProperty("support"));
                break;
            }

            case FUNNY:{
                this.reaction=Integer.parseInt(environment.getProperty("funny"));
                break;
            }

            default:{
                this.reaction=-1;

            }
        }
    }
}
