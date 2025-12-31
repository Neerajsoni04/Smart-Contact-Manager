package com.scm.Controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.Entity.User;
import com.scm.Helper.Helper;
import com.scm.Service.Impl.UserServiceImpl;

@ControllerAdvice
public class RootController { // “This class will give common things to all controllers.”
    
    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserServiceImpl userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        // This method (addLoggedInUserInformation) runs before every controller method.
        // That means: every time your controller returns a page (like dashboard.html), the loggedInUser info will 
        // already be available in that page.
        // If no user is logged in → do nothing.
        if (authentication == null) {
            return;
        }
        System.out.println("Adding logged in user information to the model");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);
        // database se data ko fetch : get user from db :
        User user = userService.getUserByEmail(username);
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);

    }
}
