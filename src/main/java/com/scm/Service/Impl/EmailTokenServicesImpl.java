package com.scm.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.Entity.User;
import com.scm.Repositary.UserRepositay;
import com.scm.Service.EmailTokenServices;

@Service
public class EmailTokenServicesImpl implements EmailTokenServices{

    @Autowired
    private UserRepositay userRepositay ;

    @Override
    public User getUserByEmailToken(String token) {
        return userRepositay.findByEmailToken(token).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepositay.save(user);
    }
    
}
