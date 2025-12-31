package com.scm.Config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import com.scm.Entity.User;
import com.scm.Entity.Type.Provider;
import com.scm.Repositary.UserRepositay;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepositay userRepositay;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        var authenticationToken = (OAuth2AuthenticationToken) authentication;
        String provider = authenticationToken.getAuthorizedClientRegistrationId();

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setEmailVarified(true);
        user.setEnabled(true);
        user.setRoleList(List.of("ROLE_USER"));
        user.setPassword("dummy");

        if (provider.equalsIgnoreCase("google")) {
            // set for google
            user.setEmail(oauthUser.getAttribute("email"));
            user.setName(oauthUser.getAttribute("name"));
            user.setProfilepic(oauthUser.getAttribute("picture"));
            user.setAbout("This user is created by GOOGLE...");
            user.setProvider(Provider.GOOGLE);
            user.setProvideUserId(oauthUser.getName());
        } else if (provider.equalsIgnoreCase("github")) {
            // set for github
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();

            user.setEmail(email);
            user.setProfilepic(picture);
            user.setName(name);
            user.setProvideUserId(oauthUser.getName());
            user.setProvider(Provider.GITHUB);

            user.setAbout("This account is created using github");
        } else {
            // Invalid Provider
            System.out.println("Unknown Provider");
        }

        User user2 = userRepositay.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            userRepositay.save(user);
            System.out.println("User Saved" + user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
