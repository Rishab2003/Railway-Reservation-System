package com.railwayreservation.railwayreservation.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.railwayreservation.railwayreservation.Repositories.UserRepository;
import com.railwayreservation.railwayreservation.entities.Train;
import com.railwayreservation.railwayreservation.entities.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
   private PasswordEncoder passwordEncoder;

    @RequestMapping("/")
    public String test(Model model){
        model.addAttribute("title", "Home");
        
        return "redirect:/home";
                
    }
    @RequestMapping("/home")
    public String testhome(Model model){
        model.addAttribute("title", "Home");
        
        return "new_home";
                
    }

    @RequestMapping("/signin")
    public String signin(Model model){
        model.addAttribute("title", "Sign in");
        return "home";
    }
   


    
    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("title", "Sign up");
        return "signin";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,BindingResult result, Model model,HttpSession session  ){

        try {
            if(result.hasErrors()){
                model.addAttribute("user",user);
                throw new Exception();
                
                
            }
            if(userRepository.findUserByEmail(user.getEmail()).isPresent()){
               model.addAttribute("user", user);
               model.addAttribute("userfound", "Account exists already. Use different email");

            }
            user.setTrain(null);
            user.setRole("USER");
            System.out.println(user.getPassword());
            System.out.println(user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            userRepository.save(user);
            model.addAttribute("Message", "Successful");
          
            
            return "signin";
        } catch (Exception e) {
            // TODO: handle exception
            ObjectError objectError=new ObjectError("email", "User already exixts");
            result.addError(objectError);

            return "signin";
        }



    


        
    }
    
}
