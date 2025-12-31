package com.scm.Service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.Repositary.UserRepositay;



@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepositay userRepositary;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepositary.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email : "+username));
    }
}