package com.railwayreservation.railwayreservation.config;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.railwayreservation.railwayreservation.Repositories.UserRepository;
import com.railwayreservation.railwayreservation.entities.User;

public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String    email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        
        
        Optional<User> user=userRepository.findUserByEmail(email);
        
        
        
        return user.map(UserDetailsImpl::new).orElseThrow(()-> new UsernameNotFoundException("User not found" +email));

        



        
    }

    
    
}
