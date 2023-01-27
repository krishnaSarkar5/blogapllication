package com.blogapplication.blogapplication.authentication.serviceImpl;

import com.blogapplication.blogapplication.authentication.dto.LoginDto;
import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.authentication.entity.UserLoginToken;
import com.blogapplication.blogapplication.authentication.repository.UserLoginTokenRepository;
import com.blogapplication.blogapplication.authentication.service.AuthenticationService;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.security.JwtUserDetailService;
import com.blogapplication.blogapplication.common.security.JwtUtils;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.common.utility.CommonUtils;
import com.blogapplication.blogapplication.user.entity.User;
import com.blogapplication.blogapplication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Environment environment;

    @Autowired
    private UserLoginTokenRepository userLoginTokenRepository;

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Override
    public ResponseDto loginWithPassword(LoginDto loginDto) {
        validateLoginRequestData(loginDto);


        Optional<User> existedUserOptional = userRepository.findByEmail(loginDto.getUsername());

        if(existedUserOptional.isEmpty()) {
            throw new ServiceException("INVALID_DATA");
        }

        User existedUser = existedUserOptional.get();

        if(existedUser.getStatus()!=Integer.parseInt(environment.getProperty("active"))) {
            throw new ServiceException("USER_DISABLED");
        }

//		UserDetails userDetails = jwtUserDetailService.loadUserByUsername(existedUser.getProfileId());

        ResponseDto responseDto = this.getJwtToken(existedUser, loginDto);
        return responseDto;
    }

    @Override
    public ResponseDto logout(String token) {
        ResponseDto responseDto = new ResponseDto();

        User user = authenticationUtil.currentLoggedInUser().getUser();

        UserLoginToken existedToken = userLoginTokenRepository.findByTokenAndStatus( token, Integer.parseInt(environment.getProperty("active")));

        if(Objects.isNull(existedToken)) {
            throw new ServiceException("INVALID_DATA");
        }

        existedToken.setStatus(Integer.parseInt(environment.getProperty("inactive")));
        existedToken.setLogoutTime(LocalDateTime.now());

        userLoginTokenRepository.save(existedToken);

        responseDto.setStatus(true);
        responseDto.setMessage("SUCCESSFULL");
        responseDto.setData("Log out successfully");

        return responseDto;
    }

    private void validateLoginRequestData(LoginDto loginDto) {

        Map<String ,String > errorMap = new HashMap<>();

        if(Objects.isNull(loginDto)){
            errorMap.put("requestBody","Request Body Is null");
        }else {
                if(Objects.isNull(loginDto.getUsername())
                    || loginDto.getUsername().trim().equals("")
                    || Objects.isNull(loginDto.getPassword())
                    || loginDto.getPassword().trim().equals("")
                    || Objects.isNull(loginDto.getChannel())
                    || loginDto.getChannel().trim().equals("")){
                    errorMap.put("requestBody","INVALID_REQUEST_BODY");
                }

            if(!commonUtils.isEmailValid(loginDto.getUsername()) && !commonUtils.isPhoneNumberValid(loginDto.getUsername())) {
                errorMap.put("username","INVALID_USERNAME");
            }
        }

        if(errorMap.size()>0){
            throw  new ServiceException("INVALID_DATA",errorMap);
        }

    }


    private ResponseDto getJwtToken(User existedUser, LoginDto loginDto) {

        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(existedUser.getProfileId());


        String channel = "";

        if(!Objects.isNull(loginDto)) {
            try
            {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, loginDto.getPassword()));
//				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, password));
            }
            catch (DisabledException e)
            {
                throw new ServiceException("USER_DISABLED", HttpStatus.UNAUTHORIZED);
            }
            catch (BadCredentialsException e)
            {
                throw new ServiceException("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                throw new ServiceException("Please put valid credentials!", HttpStatus.UNAUTHORIZED);
            }

            channel = loginDto.getChannel();

        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("FirstName", existedUser.getFirstName());
        claims.put("LastName", existedUser.getLastName());

        claims.put("roleId", existedUser.getRole());
        claims.put("status", existedUser.getStatus());
        claims.put("channel", channel);




        String token = "Bearer "+jwtUtils.generateToken(userDetails, claims);

        UserLoginToken userLoginToken = new UserLoginToken();
        userLoginToken.setToken(token);
        userLoginToken.setChannel(channel);
        userLoginToken.setLoginTime(LocalDateTime.now());
        userLoginToken.setCreatedAt(LocalDateTime.now());
        userLoginToken.setUpdatedAt(LocalDateTime.now());
        userLoginToken.setUser(existedUser);
        userLoginToken.setStatus(Integer.parseInt(environment.getProperty("active")));
        System.out.println(userLoginToken.getChannel());
        userLoginTokenRepository.save(userLoginToken);


       Map<String ,String > responeMap = new HashMap();
        responeMap.put("token",token);

        ResponseDto responseDto=new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(responeMap);
        return responseDto;
    }
}
