package com.railwayreservation.railwayreservation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class MyConfig  {

    @Autowired private LoginSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    //authentication
    public UserDetailsService userDetailsService(){
        return new UserServiceImpl();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
    
    


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

       return http.authorizeHttpRequests().requestMatchers("/admin/**").authenticated().
      
       and().authorizeHttpRequests().
        requestMatchers("/user/**").authenticated().and().formLogin()
        .loginPage("/signin").usernameParameter("email")
        .loginProcessingUrl("/dosignin").successHandler(successHandler)
        // .defaultSuccessUrl("/user/home",true)
        .and().authorizeHttpRequests().requestMatchers("/**").permitAll()
        // .and().logout((logout)-> logout.logoutSuccessUrl("/Logout").permitAll(true))
       // .failureUrl("/login_fail") for error page
        .and().csrf().disable().build();

        
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring().requestMatchers("static/**");

    }


    


        
    
}
