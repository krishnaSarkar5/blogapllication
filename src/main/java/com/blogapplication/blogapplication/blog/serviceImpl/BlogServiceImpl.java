package com.blogapplication.blogapplication.blog.serviceImpl;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.*;
import com.blogapplication.blogapplication.blog.dto.response.CommentResponseDto;
import com.blogapplication.blogapplication.blog.dto.response.GetBlogResponseDto;
import com.blogapplication.blogapplication.blog.dto.response.ReactionDto;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.entity.BlogReactionDetails;
import com.blogapplication.blogapplication.blog.entity.BlogViewDetails;
import com.blogapplication.blogapplication.blog.entity.Comment;
import com.blogapplication.blogapplication.blog.enums.BlogSortField;
import com.blogapplication.blogapplication.blog.enums.Reaction;
import com.blogapplication.blogapplication.blog.repositoty.BlogReactionDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogViewDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.CommentRepository;
import com.blogapplication.blogapplication.blog.service.BlogService;
import com.blogapplication.blogapplication.common.dto.requestDto.IdDto;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

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

    @Override
    public ResponseDto createBlog(CreateBlogRequestDto request) {
        this.validateIncomingRequest(request);

        User loggedInUser = authenticationUtil.currentLoggedInUser().getUser();

        Blog newBlogEntity = request.getNewBlogEntity();

        newBlogEntity.setCreatedBy(loggedInUser);
        newBlogEntity.setStatus(Integer.parseInt(environment.getProperty("active")));

        blogRepository.save(newBlogEntity);

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setMessage(environment.getProperty("blogCreated"));

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto getABlog(GetBlogRequestDto request) {

        this.validateIncomingRequest(request);

        User loggedInUser = getLoggedInUser();

        Blog existedBlog = blogRepository.findByIdAndStatus(request.getId(),Integer.parseInt(Objects.requireNonNull(environment.getProperty("active")))).orElseThrow(() -> new ServiceException("BLOG_NOT_FOUND"));

        if(existedBlog.getStatus()==Integer.parseInt(environment.getProperty("inactive"))
        || existedBlog.getStatus()==Integer.parseInt(environment.getProperty("delete"))){
            throw new ServiceException("Invalid Blog");
        }

        Optional<BlogReactionDetails> reaction = blogReactedDetailsRepository.findByBlogIdAndReactedByIdAndIsReacted(existedBlog.getId(), loggedInUser.getId(), true);

        GetBlogResponseDto blogResponseDto = this.getBlogResponseDto(existedBlog,loggedInUser ,reaction);


        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(blogResponseDto);

        return responseDto;
    }

    @Override
    public ResponseDto getAllBlogs(GetAllBlogRequestDto requestDto) {

        this.validateIncomingRequest(requestDto);

        this.getLoggedInUser();


        return null;
    }

    private List<Blog> getBlogEntityList(GetAllBlogRequestDto requestDto){



        return null;
    }

    private Pageable getPageInfo(GetAllBlogRequestDto requestDto){

        BlogSortField blogSortField = BlogSortField.valueOf(requestDto.getSortBy());

        switch (blogSortField){
            case TITLE:{

            }
            case ID:{

            }
            case CREATEDAT:{

            }
        }
        return null;
    }
    private void getSpecification(){

    }


    private List<CommentResponseDto> getBlogCommentsByBlogId(Long blogId){

        List<Comment> allComments = commentRepository.findByBlogIdAndStatusAndType(blogId, Integer.parseInt(Objects.requireNonNull(environment.getProperty("active"))), Integer.parseInt(Objects.requireNonNull(environment.getProperty("comment"))));


        return allComments.stream().map(c-> this.getResponseDtoForCommentWithReply(c)).collect(Collectors.toList());
    }


    private CommentResponseDto getResponseDtoForCommentWithReply(Comment comment){
        List<CommentResponseDto> commentsReplyDto = this.getCommentsReplyDto(comment.getReply());
        CommentResponseDto commentResponseDto = this.convertCommentEntityToCommentResponseDto(comment);
        commentResponseDto.setReplies(commentsReplyDto);
        return commentResponseDto;
    }

    private List<CommentResponseDto> getCommentsReplyDto(List<Comment> replies){

        return replies.stream().map(r -> this.convertCommentEntityToCommentResponseDto(r)).collect(Collectors.toList());
    }


    private CommentResponseDto convertCommentEntityToCommentResponseDto(Comment comment){

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId(comment.getId());
        commentResponseDto.setBlogId(comment.getId());
        commentResponseDto.setComment(comment.getComment());
        commentResponseDto.setCommentedAt(comment.getCommentedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        commentResponseDto.setCommentedBy(comment.getCommentedBy().getId());
        commentResponseDto.setStatus(comment.getStatus());
        commentResponseDto.setEdited(comment.getCommentedAt().equals(comment.getUpdatedAt()));
        return  commentResponseDto;
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

    private GetBlogResponseDto getBlogResponseDto(Blog blog,User loggedInUser, Optional<BlogReactionDetails> reaction){

        GetBlogResponseDto blogResponseDto = new GetBlogResponseDto();

        blogResponseDto.setId(blog.getId());
        blogResponseDto.setStatus(blog.getStatus());
        blogResponseDto.setFeatured(blog.getFeatured());
        blogResponseDto.setDescription(blog.getDescription());
        blogResponseDto.setTitle(blog.getTitle());
        blogResponseDto.setImages(blog.getImages());
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

    private List<ReactionDto> getAllReactions(Blog blog){
        List<BlogReactionDetails> reactionList = blogReactedDetailsRepository.findByBlogIdAndIsReacted(blog.getId(), true);

        return reactionList.stream().map((e) -> new ReactionDto(e, environment)).collect(Collectors.toList());
    }

    private User getLoggedInUser() {
        User loggedInUser = authenticationUtil.currentLoggedInUser().getUser();

        if(loggedInUser.getStatus()!=Integer.parseInt(Objects.requireNonNull(environment.getProperty("active")))){
            throw new ServiceException("USER_NOT_ACTIVE");
        }
        return loggedInUser;
    }

    @Override
    public ResponseDto reactBlog(ReactBlogRequestDto request) {

        this.validateIncomingRequest(request);

        User loggedInUser = getLoggedInUser();

        Blog existedBlog = blogRepository.findByIdAndStatus(request.getId(),Integer.parseInt(Objects.requireNonNull(environment.getProperty("active")))).orElseThrow(() -> new ServiceException("BLOG_NOT_FOUND"));

        Optional<BlogReactionDetails> reactionOptional = blogReactedDetailsRepository.findByBlogIdAndReactedById(request.getId(), loggedInUser.getId());

        BlogReactionDetails reaction = new BlogReactionDetails();

        if(reactionOptional.isEmpty()){
            reaction = new BlogReactionDetails();
            reaction.setBlog(existedBlog);
            reaction.setReactedBy(loggedInUser);
            reaction.setReactedAt(LocalDateTime.now(ZoneId.of("UTC")));
            reaction.setIsReacted(request.isReacted());
        }else {
            reaction = reactionOptional.get();

        }


        if(request.isReacted() && !Objects.isNull(request.getReactionValue())){

            switch (request.getReactionValue()){

                case 1 : {
                    reaction.setReaction(Reaction.LIKE);
                    break;
                }
                case 2 : {
                    reaction.setReaction(Reaction.LOVE);
                    break;
                }
                case 3 : {
                    reaction.setReaction(Reaction.SUPPORT);
                    break;
                }
                case 4 : {
                    reaction.setReaction(Reaction.FUNNY);
                    break;
                }
                default: {
                    reaction.setReaction(null);
                }


            }
            reaction.setIsReacted(true);
        }else {

            if(!reaction.getIsReacted()){
                throw new ServiceException("NOT_REACTED");
            }
            reaction.setReaction(null);
            reaction.setIsReacted(false);
        }

        reaction.setReactedAt(LocalDateTime.now(ZoneId.of("UTC")));
        blogReactedDetailsRepository.save(reaction);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(environment.getProperty("reactionAdded"));
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setStatus(true);

        return responseDto;
    }

    @Override
    public ResponseDto postComment(PostCommentRequestDto request) {

        Blog existedBlog = blogRepository.findByIdAndStatus(request.getBlogId(), Integer.parseInt(Objects.requireNonNull(environment.getProperty("active")))).orElseThrow(() -> new ServiceException("BLOG_NOT_FOUND"));

        User loggedInUser = this.getLoggedInUser();

        Comment newComment = this.getCommentEntity(request, existedBlog, loggedInUser);

        commentRepository.save(newComment);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(environment.getProperty("commentAdded"));
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setStatus(true);
        return responseDto;
    }

    @Override
    public ResponseDto replyComment(ReplyCommentRequestDto request) {

        this.validateIncomingRequest(request);

        User loggedInUser = this.getLoggedInUser();

        Comment existedComment = commentRepository.findByIdAndStatusAndType(request.getCommentId(), Integer.parseInt(Objects.requireNonNull(environment.getProperty("active"))), Integer.parseInt(Objects.requireNonNull(environment.getProperty("comment")))).orElseThrow(()-> new ServiceException("Invalid comment id"));

        Comment replyEntity = this.getReplyEntity(request, existedComment.getBlog(), loggedInUser);

        List<Comment> existedReplies = null;


        // in future test casecade type all
        Comment savedReply = commentRepository.save(replyEntity);

        if(existedComment.getReply().isEmpty()){
            existedReplies =  new ArrayList<>();
        }else {
            existedReplies = existedComment.getReply();
        }

        existedReplies.add(savedReply);

        existedComment.setReply(existedReplies);

        commentRepository.save(existedComment);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setData(environment.getProperty("replyAdded"));
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setStatus(true);


        return responseDto;
    }

    @Override
    public ResponseDto getViews(IdDto idDto) {
        validateIncomingRequest(idDto);

        Integer count = getBlogViews(idDto.getId());

        ResponseDto responseDto = new ResponseDto();

        Map<String ,Integer> responseCount = new HashMap<>();
        
        responseCount.put("views",count);
        responseDto.setData(responseCount);
        responseDto.setMessage("SUCCESS");
        responseDto.setStatus(true);
        return responseDto;
    }

    private Integer getBlogViews(Long id) {
        Integer count = blogViewDetailsRepository.countDistinctUserIdAndId(id);
        return count;
    }


    @Override
    public ResponseDto getReactions(IdDto idDto) {
        return null;
    }


    private Comment getCommentEntity(PostCommentRequestDto request,Blog blog,User loggedInUser){

        Comment comment = new Comment();

        comment.setComment(request.getComment());
        comment.setBlog(blog);
        comment.setCommentedBy(loggedInUser);
        comment.setCommentedAt(LocalDateTime.now(ZoneId.of("UTC")));
        comment.setUpdatedAt(comment.getCommentedAt());
        comment.setStatus(Integer.parseInt(Objects.requireNonNull(environment.getProperty("active"))));
        comment.setType(Integer.parseInt(Objects.requireNonNull(environment.getProperty("comment"))));
        return comment;
    }

    private Comment getReplyEntity(ReplyCommentRequestDto request,Blog blog,User loggedInUser){

        Comment comment = new Comment();

        comment.setComment(request.getReply());
        comment.setBlog(blog);
        comment.setCommentedBy(loggedInUser);
        comment.setCommentedAt(LocalDateTime.now(ZoneId.of("UTC")));
        comment.setUpdatedAt(comment.getCommentedAt());
        comment.setStatus(Integer.parseInt(Objects.requireNonNull(environment.getProperty("active"))));
        comment.setType(Integer.parseInt(Objects.requireNonNull(environment.getProperty("reply"))));
        return comment;
    }

    private void validateIncomingRequest(Object object){
        if(object instanceof CreateBlogRequestDto){
            CreateBlogRequestDto request =   (CreateBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof GetBlogRequestDto) {
            GetBlogRequestDto request =   (GetBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof ReactBlogRequestDto) {
            ReactBlogRequestDto request =   (ReactBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof PostCommentRequestDto) {
            PostCommentRequestDto request = (PostCommentRequestDto) object;
            request.validateData();
        }

        else if (object instanceof ReplyCommentRequestDto) {
            ReplyCommentRequestDto request = (ReplyCommentRequestDto) object;
            request.validateData();
        }
        else if (object instanceof GetAllBlogRequestDto) {
            GetAllBlogRequestDto request = (GetAllBlogRequestDto) object;
            request.validateData();
        }

        else if (object instanceof IdDto) {
            IdDto request = (IdDto) object;
            request.validateData();
        }
        else {
            throw new ServiceException("PROCESS_ERROR");
        }
    }
}
