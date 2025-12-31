package com.scm.Repositary;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.Entity.Contact;
import com.scm.Entity.User;

public interface ContactRepositary extends JpaRepository<Contact,String>{

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUser(User user,Pageable pageable);

    Page<Contact> findByUserAndNameContaining(User user,String name,Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user,String email,Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user,String phoneNumber,Pageable pageable);


} 
