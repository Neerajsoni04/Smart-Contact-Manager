package com.scm.Entity;

import com.scm.Entity.Type.Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder.Default;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    private String userId;
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String profilepic;
    private String phoneNumber;


    // information
    @Default
    private boolean enabled = false;

    private boolean emailVarified = false;
    private boolean phoneVarified = false;

    
    @Enumerated(value = EnumType.STRING)
    private Provider provider = Provider.SELF;
    private String provideUserId;

    private String emailToken;

    // add more fields if needed
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Contact> contactList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         
        return roleList.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;    
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

    
}