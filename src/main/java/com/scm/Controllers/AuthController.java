package com.scm.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.Entity.User;
import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Service.EmailTokenServices;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmailTokenServices emailTokenServices;

    @RequestMapping("/verify-email")
    public String verificationLink(@RequestParam("token") String token,HttpSession session) {
        
        User user = emailTokenServices.getUserByEmailToken(token);
        System.out.println("Now in Auth Controller");
        if (user != null) {
            System.out.println("User Get By Email Token");
            if (user.getEmailToken().equals(token)) {
                user.setEnabled(true);
                user.setEmailVarified(true);
                emailTokenServices.saveUser(user);
                System.out.println("Now User is Enabled");
                session.setAttribute("message", Message.builder().type(MessageType.green)
                            .content("You email is verified. Now you can login").build());
                return "success_page";
            }
        }
        session.setAttribute("message", Message.builder().type(MessageType.red)
                .content("Email not verified ! Token is not associated with user .").build());
        return "error_page";
    }
    
}
