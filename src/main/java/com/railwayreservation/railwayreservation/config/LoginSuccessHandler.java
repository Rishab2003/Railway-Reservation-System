package com.railwayreservation.railwayreservation.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        // TODO Auto-generated method stub

        UserDetailsImpl userDetailsImpl  =(UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetailsImpl.getUsername()+"AAAAA");
        Collection<? extends GrantedAuthority> authorities=  userDetailsImpl.getAuthorities();


        String redirectURL = request.getContextPath();
        System.out.println(userDetailsImpl.getRole()+"AAAAAA");
        if(userDetailsImpl.getRole().equals("USER")){
            redirectURL+="/user/home";
        }
        if(userDetailsImpl.getRole().equals("ADMIN")){
            redirectURL+="/admin";
        }
        

        response.sendRedirect(redirectURL);
    }

    
    
}
