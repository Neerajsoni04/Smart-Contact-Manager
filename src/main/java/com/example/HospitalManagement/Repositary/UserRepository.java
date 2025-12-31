package com.example.HospitalManagement.Repositary;

import com.example.HospitalManagement.Entity.Type.AuthProviderType;
import com.example.HospitalManagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    Optional<User> findByProviderTypeAndProviderId(AuthProviderType providerType, String providerId);
}