package com.blogapplication.blogapplication.user.serviceImpl.serviceMethods;

import com.blogapplication.blogapplication.common.exceptiom.ServiceException;
import com.blogapplication.blogapplication.common.utility.AuthenticationUtil;
import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoggedInUser {


    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Autowired
    private Environment environment;


    public User getLoggedInUser() {
        User loggedInUser = authenticationUtil.currentLoggedInUser().getUser();

        if(loggedInUser.getStatus()!=Integer.parseInt(Objects.requireNonNull(environment.getProperty("active")))){
            throw new ServiceException("USER_NOT_ACTIVE");
        }
        return loggedInUser;
    }
}
