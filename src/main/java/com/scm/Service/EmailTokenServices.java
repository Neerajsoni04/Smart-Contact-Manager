package com.scm.Service;

import com.scm.Entity.User;

public interface EmailTokenServices {
    public User getUserByEmailToken(String token); 
    
    public User saveUser(User user);
} 
