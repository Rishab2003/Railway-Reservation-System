package com.railwayreservation.railwayreservation.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.railwayreservation.railwayreservation.entities.User;

public class UserDetailsImpl implements UserDetails{

    private User user;

    public UserDetailsImpl(User user){
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        SimpleGrantedAuthority simpleGrantedAuthority= new SimpleGrantedAuthority(user.getRole());
        
        return List.of(simpleGrantedAuthority); 
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
       return true;
    }

    public String getRole(){
        return user.getRole();
    }
    
}
