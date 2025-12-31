package com.scm.Repositary;

import com.scm.Entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositay extends JpaRepository<User,String>{

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailToken(String token);

} 
