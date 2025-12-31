package com.scm.Service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.Repositary.UserRepositay;
import com.scm.Service.EmailService;
import com.scm.Service.UserServices;
import com.scm.Entity.User;
import com.scm.Helper.Helper;
import com.scm.Helper.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    private UserRepositay userRepositary;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User saveUser(User user) {
        String userId= UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of("ROLE_USER"));
        user.setEnabled(false);

        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepositary.save(user);
        emailService.sendEmail(user.getEmail(),"Account Verification: Smart Contact Manager.",Helper.getLinkFromEmailToken(emailToken)  );
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepositary.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepositary.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found !!"));

        // Copy all properties from `user` to `user2`
        // BeanUtils.copyProperties(user, user2);

        modelMapper.map(user, user2);

        return Optional.of(userRepositary.save(user2));
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepositary.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found !!"));
        userRepositary.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepositary.findById(userId).orElse(null);
        return user2 != null ? true: false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user2 = userRepositary.findByEmail(email).orElse(null);
        return user2 != null ? true: false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositary.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepositary.findByEmail(email).orElse(null);
    }

    

}
