package com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.GetBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.response.CommentResponseDto;
import com.blogapplication.blogapplication.blog.dto.response.GetBlogResponseDto;
import com.blogapplication.blogapplication.blog.dto.response.ReactionDto;
import com.blogapplication.blogapplication.blog.dto.response.ReplyResponseDto;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.entity.BlogReactionDetails;
import com.blogapplication.blogapplication.blog.entity.BlogViewDetails;
import com.blogapplication.blogapplication.blog.entity.Comment;
import com.blogapplication.blogapplication.blog.repositoty.BlogReactionDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogViewDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.CommentRepository;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import com.blogapplication.blogapplication.user.serviceImpl.serviceMethods.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GetBlog {


    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Environment environment;


    @Autowired
    private AuthenticationUtil authenticationUtil;


    @Autowired
    private BlogReactionDetailsRepository blogReactedDetailsRepository;
    @Autowired
    private BlogViewDetailsRepository blogViewDetailsRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ValidateRequest validateRequest;

    @Autowired
    private LoggedInUser loggedInUser;

    @Transactional
    public ResponseDto getABlog(GetBlogRequestDto request) {

        validateRequest.validateIncomingRequest(request);

        User user = loggedInUser.getLoggedInUser();

        Blog existedBlog = blogRepository.findByIdAndStatus(request.getId(),Integer.parseInt(Objects.requireNonNull(environment.getProperty("active")))).orElseThrow(() -> new ServiceException("BLOG_NOT_FOUND"));

        if(existedBlog.getStatus()==Integer.parseInt(environment.getProperty("inactive"))
                || existedBlog.getStatus()==Integer.parseInt(environment.getProperty("delete"))){
            throw new ServiceException("Invalid Blog");
        }

        Optional<BlogReactionDetails> reaction = blogReactedDetailsRepository.findByBlogIdAndReactedByIdAndIsReacted(existedBlog.getId(), user.getId(), true);

        GetBlogResponseDto blogResponseDto = this.getBlogResponseDto(existedBlog,user ,reaction);


        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(blogResponseDto);

        return responseDto;
    }


    private GetBlogResponseDto getBlogResponseDto(Blog blog,User loggedInUser, Optional<BlogReactionDetails> reaction){

        GetBlogResponseDto blogResponseDto = new GetBlogResponseDto();

        blogResponseDto.setId(blog.getId());
        blogResponseDto.setStatus(blog.getStatus());
        blogResponseDto.setFeatured(blog.getFeatured());
        blogResponseDto.setDescription(blog.getDescription());
        blogResponseDto.setTitle(blog.getTitle());
        blogResponseDto.setImage(blog.getImage());
        blogResponseDto.setCreatedBy(blog.getCreatedBy().getId());
        blogResponseDto.setCreatedAt(blog.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        blogResponseDto.setEdited(!blog.getUpdatedAt().isEqual(blog.getCreatedAt()));
        blogResponseDto.setViews(this.viewDetailsOfBlog(blog,loggedInUser));
        List<ReactionDto> allReactions = this.getAllReactions(blog);
        blogResponseDto.setReactionList(allReactions);
        blogResponseDto.setReactionCount(allReactions.size());
        blogResponseDto.setComments(this.getBlogCommentsByBlogId(blog.getId()));


        if(reaction.isPresent() && reaction.get().getIsReacted()){
            switch (reaction.get().getReaction()){

                case LIKE:{
                    blogResponseDto.setOwnReaction(Integer.parseInt(Objects.requireNonNull(environment.getProperty("like"))));
                    break;
                }

                case LOVE:{
                    blogResponseDto.setOwnReaction(Integer.parseInt(Objects.requireNonNull(environment.getProperty("love"))));
                    break;
                }

                case SUPPORT:{
                    blogResponseDto.setOwnReaction(Integer.parseInt(Objects.requireNonNull(environment.getProperty("support"))));
                    break;
                }

                case FUNNY:{
                    blogResponseDto.setOwnReaction(Integer.parseInt(Objects.requireNonNull(environment.getProperty("funny"))));
                    break;
                }

                default:{
                    blogResponseDto.setOwnReaction(-1);

                }
            }
        }else {
            blogResponseDto.setOwnReaction(-1);
        }

        return blogResponseDto;
    }


    private Integer viewDetailsOfBlog(Blog blog, User user){

//        Optional<BlogViewDetails> previousView = blogViewDetailsRepository.findFirstByBlogIdAndViewedByIdOrderByViewedAtDesc(blog.getId(), user.getId());

//        if(previousView.isEmpty()){
        saveNewView(blog, user);
//        }

        return getBlogViews(blog.getId());
    }


    private void saveNewView(Blog blog, User user) {
        BlogViewDetails newView = new BlogViewDetails();
        newView.setBlog(blog);
        newView.setViewedBy(user);
        newView.setViewedAt(LocalDateTime.now(ZoneId.of("UTC")));
        blogViewDetailsRepository.save(newView);
    }

    private Integer getBlogViews(Long id) {
        Integer count = blogViewDetailsRepository.countDistinctUserIdAndId(id);
        return count;
    }

    private List<ReactionDto> getAllReactions(Blog blog){
        List<BlogReactionDetails> reactionList = blogReactedDetailsRepository.findByBlogIdAndIsReacted(blog.getId(), true);

        return reactionList.stream().map((e) -> new ReactionDto(e, environment)).collect(Collectors.toList());
    }

    private List<CommentResponseDto> getBlogCommentsByBlogId(Long blogId){

        List<Comment> allComments = commentRepository.findByBlogIdAndStatusAndReferencedCommentId(blogId, Integer.parseInt(Objects.requireNonNull(environment.getProperty("active"))),Long.parseLong(environment.getProperty("parentComment")));


        return allComments.stream().map(c-> this.getResponseDtoForCommentWithReply(c)).collect(Collectors.toList());
    }

    private CommentResponseDto getResponseDtoForCommentWithReply(Comment comment){
        List<Comment> repliesOfComment = getRepliesOfComment(comment.getId());
        List<ReplyResponseDto> commentsReplyDto = this.getCommentsReplyDto(repliesOfComment);
        CommentResponseDto commentResponseDto = this.convertCommentEntityToCommentResponseDto(comment);
        commentResponseDto.setReplies(commentsReplyDto);
        return commentResponseDto;
    }

    private List<Comment> getRepliesOfComment(Long commentId){
        return commentRepository.findByReferencedCommentIdAndStatus(commentId,Integer.parseInt(environment.getProperty("active")));
    }

    private List<ReplyResponseDto> getCommentsReplyDto(List<Comment> replies){

        return replies.stream().map(r -> this.convertCommentEntityToReplyResponseDto(r)).collect(Collectors.toList());
    }

    private ReplyResponseDto convertCommentEntityToReplyResponseDto(Comment comment){

        ReplyResponseDto replyResponseDto = new ReplyResponseDto();

        replyResponseDto.setId(comment.getId());
        replyResponseDto.setBlogId(comment.getId());
        replyResponseDto.setReply(comment.getComment());
        replyResponseDto.setReplyAt(comment.getCommentedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        replyResponseDto.setRepliedBy(comment.getCommentedBy().getId());
        replyResponseDto.setStatus(comment.getStatus());
        replyResponseDto.setEdited(!comment.getCommentedAt().equals(comment.getUpdatedAt()));
        return  replyResponseDto;
    }

    private CommentResponseDto convertCommentEntityToCommentResponseDto(Comment comment){

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId(comment.getId());
        commentResponseDto.setBlogId(comment.getId());
        commentResponseDto.setComment(comment.getComment());
        commentResponseDto.setCommentedAt(comment.getCommentedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        commentResponseDto.setCommentedBy(comment.getCommentedBy().getId());
        commentResponseDto.setStatus(comment.getStatus());
        commentResponseDto.setEdited(!comment.getCommentedAt().equals(comment.getUpdatedAt()));
        return  commentResponseDto;
    }
}
