package com.scm.Config;

import com.scm.Service.Impl.CustomUserDetailService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(authorize ->{
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        })
        .csrf(csrf -> csrf.disable())
        .formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile",true); // The browser makes a new GET request.
            // formLogin.successForwardUrl("/user/dashboard"); // The same POST login request is internally forwarded to your controller.
            formLogin.failureUrl("/login?error"); // To make failure also do a redirect (new GET request), use:
            // formLogin.failureForwardUrl("/login?error=true");

            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            formLogin.failureHandler(authFailureHandler);

        })
        .logout(formLogout -> {
            formLogout.logoutUrl("/logout");                   // URL to trigger logout
            formLogout.logoutSuccessUrl("/login?logout=true"); // Where to go after logout   
        });
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(customSuccessHandler);
        });
        return httpSecurity.build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
