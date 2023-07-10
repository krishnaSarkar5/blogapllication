package com.blogapplication.blogapplication.blog.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.GetAllBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.request.GetBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.response.*;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.entity.BlogReactionDetails;
import com.blogapplication.blogapplication.blog.entity.BlogViewDetails;
import com.blogapplication.blogapplication.blog.entity.Comment;
import com.blogapplication.blogapplication.blog.repositoty.BlogReactionDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogViewDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.CommentRepository;
import com.blogapplication.blogapplication.blog.specification.BlogSpecification;
import com.blogapplication.blogapplication.common.dto.SeacrhCriteria;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import com.blogapplication.blogapplication.user.serviceImpl.serviceMethods.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GetBlog {


    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Environment environment;


    @Value("${views.duration}")
    private Long viewDuration;

    @Value("${views.list-limit}")
    private Integer viewsListLimit;


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










//   -----------------------------     Get All Blog  ----------------------------



    public ResponseDto getAllBlogs(GetAllBlogRequestDto requestDto) {

        requestDto.validateData();

        GetAllBlogResponseWithCountDto blogsFromDb = getBlogsResponseDtoFromDb(requestDto);


        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(blogsFromDb);

        return responseDto;
    }


    private GetAllBlogResponseWithCountDto getBlogsResponseDtoFromDb(GetAllBlogRequestDto request){




        PageRequest pageInformation = getPageInformation(request);

        if(request.getSearchField().size()>0){

            List<SeacrhCriteria> criteriaList = getCriteriaList(request);

            BlogSpecification specification = new BlogSpecification(criteriaList);

            List<Blog> allBlogsWithPage = blogRepository.findAll(specification, pageInformation);

            List<Blog> allBlogs = blogRepository.findAll(specification);



            return getGetAllBlogResponseWithCountDto(allBlogsWithPage, allBlogs.size());


        }else {
            List<Blog> allBlogs = blogRepository.findAll(pageInformation).getContent();



            List<Blog> allBlogsWOPageInfo = blogRepository.findAll();

            return getGetAllBlogResponseWithCountDto(allBlogs,allBlogsWOPageInfo.size());
        }


    }


//    private GetAllBlogResponseWithCountDto getAllBlogsResponseDto(List<Blog> allBlogs){
//        return getGetAllBlogResponseWithCountDto(allBlogs,allBlogs.size());
//    }

    private PageRequest getPageInformation(GetAllBlogRequestDto request){

        int pageSize = request.getPageSize()==0?Integer.parseInt(environment.getProperty("defaultPageSize")):request.getPageSize();
        int offset = request.getOffset()==0?Integer.parseInt(environment.getProperty("defaultOffset")):request.getOffset();

        String sortBy =!Objects.isNull(request.getSortBy()) && !request.getSortBy().trim().equalsIgnoreCase("")?request.getSortBy():"id";

        String orderType = !Objects.isNull(request.getOrderType()) && !request.getOrderType().trim().equalsIgnoreCase("")?request.getOrderType().trim():"asc";

        return PageRequest.of(offset,pageSize).withSort(Sort.by(Sort.Direction.fromString(orderType),sortBy));
    }



    private List<SeacrhCriteria> getCriteriaList(GetAllBlogRequestDto request){

        List<SeacrhCriteria> criteriaList = new ArrayList<>();

        for(int i=0;i<request.getSearchField().size();i++){
            SeacrhCriteria seacrhCriteria = new SeacrhCriteria(request.getSearchField().get(i),request.getSearchFieldValue().get(i));
            criteriaList.add(seacrhCriteria);
        }


        return criteriaList;


    }

    private GetAllBlogResponseWithCountDto getGetAllBlogResponseWithCountDto(List<Blog> blogList, Integer allPossibleResultSize) {
        List<Long> blogIdList = blogList.stream().map(b -> b.getId()).collect(Collectors.toList());

        Map<Long, Integer> viewDetailsOfBlog = getViewDetailsOfBlog(blogIdList);

        List<GetAllBlogResponseDto> blogResponseDtoList = getBlogResponseDtoList(blogList);


        Map<Long, Tuple> trendingBlogsMap = getTrendingBlogsMap();

        for (GetAllBlogResponseDto blogResponseDto : blogResponseDtoList){
            blogResponseDto.setViews(viewDetailsOfBlog.get(blogResponseDto.getId()));
            if(!Objects.isNull(trendingBlogsMap.get(blogResponseDto.getId()))){
                blogResponseDto.setTrending(true);
            }

        }

        GetAllBlogResponseWithCountDto getAllBlogResponseWithCountDto = new GetAllBlogResponseWithCountDto(allPossibleResultSize,blogResponseDtoList);
        return getAllBlogResponseWithCountDto;
    }



    private Map<Long,Integer> getViewDetailsOfBlog(List<Long> blogIdList){

        List<Tuple> tuples = blogViewDetailsRepository.countOfViewsByBlogId(blogIdList);

        return convertTupleToMap(tuples);
    }



    private List<GetAllBlogResponseDto> getBlogResponseDtoList(List<Blog> allBlogs){

        List<GetAllBlogResponseDto> blogResponseDtoList = new ArrayList<>();

        return allBlogs.stream().map(b-> new GetAllBlogResponseDto(b)).collect(Collectors.toList());

    }

    private Map<Long,Integer> convertTupleToMap(List<Tuple> tuples){

        Map<Long,Integer> map = new HashMap<>();

        for(Tuple t : tuples){
            map.put(Long.parseLong(t.get("blog_id").toString()),Integer.parseInt(t.get("views").toString()));
        }
        return  map;
    }





    private  Map<Long, Tuple> getTrendingBlogsMap(){

        List<Tuple> blogViewDetails = getBlogViewDetailsOrderByViewsFromDB();

        Map<Long, Tuple> blogViewMap = blogViewDetails.stream().collect(Collectors.toMap(b -> Long.parseLong(String.valueOf(b.get("id"))), b -> b));


        return blogViewMap;

    }










//    --------------------------- Get All Trending Blog -----------------------------

    public  ResponseDto getAllTrendingBlog(){

        List<Tuple> blogViewDetailsOrderByViewsFromDB = getBlogViewDetailsOrderByViewsFromDB();

        List<GetAllBlogResponseDto> getAllBlogResponseDtos = convertTupleToGetAlLBlogResponseDto(blogViewDetailsOrderByViewsFromDB);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(getAllBlogResponseDtos);

        return responseDto;


    }


    private  List<Tuple> getBlogViewDetailsOrderByViewsFromDB(){

        LocalDateTime from = LocalDateTime.now().minusSeconds(viewDuration);

        LocalDateTime to = LocalDateTime.now();

        List<Tuple> allTrendingBlogBetweenDateRange = blogViewDetailsRepository.getAllTrendingBlogBetweenDateRange(from, to, viewsListLimit);

        return  allTrendingBlogBetweenDateRange;
    }


    private List<GetAllBlogResponseDto> convertTupleToGetAlLBlogResponseDto(List<Tuple> list){
        List<GetAllBlogResponseDto> dtoList = list.stream().map(e -> new GetAllBlogResponseDto(e,true)).collect(Collectors.toList());

        return dtoList;
    }
}
