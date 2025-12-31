package com.scm.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Configuration
public class AppConfig {

    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.api.key}")
    private String api_Key;

    @Value("${cloudinary.api.secret}")
    private String secret_Key;

    @Bean
    Cloudinary cloudinary(){
        return new Cloudinary(
            ObjectUtils.asMap(
                "cloud_name",cloudName,
                "api_key",api_Key,
                "api_secret",secret_Key
            )
        );
    }
}
