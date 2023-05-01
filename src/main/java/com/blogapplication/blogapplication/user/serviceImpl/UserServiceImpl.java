package com.blogapplication.blogapplication.user.serviceImpl;

import com.blogapplication.blogapplication.authentication.dto.ResponseDto;
import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.CommonUtils;
import com.blogapplication.blogapplication.user.dto.CreateUserRequestDto;
import com.blogapplication.blogapplication.user.entity.User;
import com.blogapplication.blogapplication.user.repository.UserRepository;
import com.blogapplication.blogapplication.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private Environment environment;


    @Override
    public ResponseDto createUser(CreateUserRequestDto createUserRequestDto) {

//       int x= 1/0;

        this.validateCreateUserRequestDto(createUserRequestDto);

        User newUser = new User(createUserRequestDto);

        newUser.setProfileId(this.getProfileId());
        newUser.setStatus(Integer.parseInt(environment.getProperty("active")));

        userRepository.save(newUser);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(true);
        responseDto.setMessage(environment.getProperty("successResponse"));
        responseDto.setData(environment.getProperty("userCreated"));
        return responseDto;
    }

    private void validateCreateUserRequestDto(CreateUserRequestDto createUserRequestDto){

        Map<String,String> errorMap = new HashMap<>();

        if(Objects.isNull(createUserRequestDto)){
            errorMap.put("Invalid Data","Request body is null");
        } else {
            if (Objects.isNull(createUserRequestDto.getFirstName())
                    || createUserRequestDto.getFirstName().trim().equals("")
                    || createUserRequestDto.getFirstName().trim().length()<=2) {
                errorMap.put("First Name","Enter a valid First Name");
            }

            if (Objects.isNull(createUserRequestDto.getLastName())
                    || createUserRequestDto.getLastName().trim().equals("")
                    || createUserRequestDto.getLastName().trim().length()<=2) {
                errorMap.put("Last Name","Enter a valid Last Name");
            }
            if (Objects.isNull(createUserRequestDto.getLastName())
                    || createUserRequestDto.getLastName().trim().equals("")
                    || createUserRequestDto.getLastName().trim().length()<=2 ) {
                errorMap.put("Last Name","Enter a valid Last Name");
            }

            if (Objects.isNull(createUserRequestDto.getEmail())
                    || createUserRequestDto.getEmail().trim().equals("")
                    || !this.validatedEmailAddress(createUserRequestDto.getEmail().trim()) ) {
                errorMap.put("Email ","Enter a valid Email");
            }

            if (Objects.isNull(createUserRequestDto.getPhone())
                    || createUserRequestDto.getPhone().trim().equals("")
                    || !this.validatedPhone(createUserRequestDto.getPhone().trim()) ) {
                errorMap.put("Phone","Enter a valid Phone");
            }

            if (Objects.isNull(createUserRequestDto.getPassword())
                    || createUserRequestDto.getPassword().trim().equals("")
                    || !this.validatePassword(createUserRequestDto.getPassword().trim()) ) {
                errorMap.put("Password","Invalid Password");
            }
        }

        if(errorMap.size()>0){
            throw  new ServiceException("Invalid Request Data ",errorMap);
        }
    }

    private Boolean validatedEmailAddress(String emailAddress){
       return commonUtils.isEmailValid(emailAddress);
    }

    private Boolean validatedPhone(String phone){
        return commonUtils.isPhoneNumberValid(phone);
    }

    private Boolean validatePassword(String password){
        return  commonUtils.isValidPassword(password);
    }

    private  String getProfileId(){
        return commonUtils.generateProfileId();
    }
}
