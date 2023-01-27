package com.blogapplication.blogapplication.blog.serviceImpl;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.CreateBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.request.GetBlogRequestDto;
import com.blogapplication.blogapplication.blog.dto.request.GetBlogResponseDto;
import com.blogapplication.blogapplication.blog.dto.request.ReactBlogRequestDto;
import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.entity.BlogReactionDetails;
import com.blogapplication.blogapplication.blog.enums.Reaction;
import com.blogapplication.blogapplication.blog.repositoty.BlogReactionDetailsRepository;
import com.blogapplication.blogapplication.blog.repositoty.BlogRepository;
import com.blogapplication.blogapplication.blog.service.BlogService;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseDto getABlog(GetBlogRequestDto request) {

        this.validateIncomingRequest(request);

        User loggedInUser = getLoggedInUser();

        Blog existedBlog = blogRepository.findById(request.getId()).orElseThrow(() -> new ServiceException("BLOG_NOT_FOUND"));

        if(existedBlog.getStatus()==Integer.parseInt(environment.getProperty("inactive"))
        || existedBlog.getStatus()==Integer.parseInt(environment.getProperty("delete"))){
            throw new ServiceException("Invalid Blog");
        }

        Optional<BlogReactionDetails> reaction = blogReactedDetailsRepository.findByBlogIdAndReactedByIdAndIsReacted(existedBlog.getId(), loggedInUser.getId(), true);

        GetBlogResponseDto blogResponseDto = this.getBlogResponseDto(existedBlog, reaction);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(blogResponseDto);

        return responseDto;
    }

    private GetBlogResponseDto getBlogResponseDto(Blog blog, Optional<BlogReactionDetails> reaction){

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

        if(reaction.isPresent() && reaction.get().getIsReacted()){
            switch (reaction.get().getReaction()){

                case LIKE:{
                        blogResponseDto.setReaction(Integer.parseInt(environment.getProperty("like")));
                        break;
                }

                case LOVE:{
                    blogResponseDto.setReaction(Integer.parseInt(environment.getProperty("love")));
                    break;
                }

                case SUPPORT:{
                    blogResponseDto.setReaction(Integer.parseInt(environment.getProperty("support")));
                    break;
                }

                case FUNNY:{
                    blogResponseDto.setReaction(Integer.parseInt(environment.getProperty("funny")));
                    break;
                }

                default:{
                    blogResponseDto.setReaction(-1);

                }
            }
        }else {
            blogResponseDto.setReaction(-1);
        }

        return blogResponseDto;
    }

    private User getLoggedInUser() {
        User loggedInUser = authenticationUtil.currentLoggedInUser().getUser();

        if(loggedInUser.getStatus()!=Integer.parseInt(environment.getProperty("active"))){
            throw new ServiceException("USER_NOT_ACTIVE");
        }
        return loggedInUser;
    }

    @Override
    public ResponseDto reactBlog(ReactBlogRequestDto request) {

        this.validateIncomingRequest(request);

        User loggedInUser = getLoggedInUser();

        Blog existedBlog = blogRepository.findById(request.getId()).orElseThrow(() -> new ServiceException("BLOG_NOT_FOUND"));

        Optional<BlogReactionDetails> reactionOptional = blogReactedDetailsRepository.findByBlogIdAndReactedById(request.getId(), loggedInUser.getId());

        BlogReactionDetails reaction = new BlogReactionDetails();

        if(reactionOptional.isEmpty()){
            reaction = new BlogReactionDetails();
            reaction.setBlog(existedBlog);
            reaction.setReactedBy(loggedInUser);
            reaction.setReactedAt(LocalDateTime.now(ZoneId.of("UTC")));
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
        else {
            throw new ServiceException("PROCESS_ERROR");
        }
    }
}
