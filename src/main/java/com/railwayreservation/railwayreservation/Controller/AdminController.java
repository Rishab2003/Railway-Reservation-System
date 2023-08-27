package com.railwayreservation.railwayreservation.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.railwayreservation.railwayreservation.Repositories.TrainDataRepsitory;
import com.railwayreservation.railwayreservation.Repositories.UserRepository;
import com.railwayreservation.railwayreservation.entities.TrainData;
import com.railwayreservation.railwayreservation.entities.User;

@Controller
@Secured("ADMIN")
public class AdminController {

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TrainDataRepsitory trainDataRepository;

    @RequestMapping("/admin")
    public String admin(Principal principal, Model model){
        
        model.addAttribute("train", new TrainData());
        
        return "adminpage/form";
        
        
    }

    @PostMapping("/admin/register")
    public String register(@ModelAttribute("train") TrainData train,Model model){
        
        if(trainDataRepository.findByTrain_Name(train.getTrain_name()).isPresent()){
            model.addAttribute("Failure", "Train name exists already");
            return "adminpage/form";
        }
        if(trainDataRepository.findByTrain_No(train.getTrain_no())!=null){
            model.addAttribute("Failure", "Train no already exists");
        }
        
        trainDataRepository.save(train);

        model.addAttribute("Success", "Train added Successfully");

        return "adminpage/form";
    }
    
    
}
