package com.railwayreservation.railwayreservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // TODO Auto-generated method stub
        
        registry.addViewController("/user/home").setViewName("user/user_home");
        registry.addViewController("/admin").setViewName("adminpage/form");

    }

    
    
}
