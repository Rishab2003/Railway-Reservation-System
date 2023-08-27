package com.railwayreservation.railwayreservation.Controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.railwayreservation.railwayreservation.Repositories.TrainDataRepsitory;
import com.railwayreservation.railwayreservation.Repositories.TrainRepository;
import com.railwayreservation.railwayreservation.Repositories.UserRepository;
import com.railwayreservation.railwayreservation.entities.Train;
import com.railwayreservation.railwayreservation.entities.TrainData;
import com.railwayreservation.railwayreservation.entities.User;
import com.railwayreservation.railwayreservation.extras.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
@Secured("USER")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainDataRepsitory trainDataRepsitory;

    @Autowired
    private TrainRepository trainRepository;

    @ModelAttribute
    public void commondata(Principal principal, Model model){
        
        String username=principal.getName();
        Optional<User> user=userRepository.findUserByEmail(username);
        model.addAttribute("user",user.get());
        model.addAttribute("welcome", user.get().getName());

    }

    @RequestMapping("/home")
    public String user(Principal principal,Model model){
        model.addAttribute("title", "Home");
        return "user/user_home";
    }

    @RequestMapping("/show-trains")
    public String showtickets(Principal principal,Model model){
        
    
        List<TrainData> list=trainDataRepsitory.findAll();
        
        
        model.addAttribute("train",list);
        model.addAttribute("message", "yes");
          model.addAttribute("title", "Book tickets");
        return "user/show_trains";

    }

    @GetMapping("/show-trains/filter")
    public String filter(Model model,HttpSession session, 
                         @RequestParam("start") String start
                         ,@RequestParam("destination") String destination
                         ,@RequestParam("date") String date
                         ,@RequestParam("class") String clString
                         ,@RequestParam("seats") Integer seats) throws ParseException
    {

        //removing attribute wheen user tries to book another traiin
        session.removeAttribute("class");
        session.removeAttribute("seats");
        session.removeAttribute("train_numbers");
        session.removeAttribute("train");

        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);  
        List<TrainData> train= this.trainDataRepsitory.findTrainByFilter(start,destination,date1);
        
        for( int i=0;i<train.size();i++){
            TrainData t=train.get(i);
            boolean seat_available=true;

            switch (clString) {
                    case "SL":
                        if(t.getSL()<seats) seat_available=false;
                        break;
                    
                    case "2AC":
                         if(t.getAC2()<seats) seat_available=false;
                        break;
            
                    case "3AC":
                         if(t.getAC3()<seats) seat_available=false;
                        break;
                    
                    case "CC":
                         if(t.getCC()<seats) seat_available=false;
                        break;
                
                    default:
                        break;
                }
            
            if(!seat_available)
            train.remove(t);
        }
        


        List<Long> train_numbers=new ArrayList<>();
        for(TrainData t:train){
            train_numbers.add(t.getTrain_data_id());;
        }
        
        if(!train.isEmpty()){
            model.addAttribute("train", train); 
            session.setAttribute("train", train);
            session.setAttribute("class",clString);
            session.setAttribute("seats", seats);
            session.setAttribute("train_numbers", train_numbers);
            
            
            model.addAttribute("Message", "found");
        
        }
        else{
            
            model.addAttribute("Message", "not found");
        }

        //sending details again
        model.addAttribute("start", start);
        model.addAttribute("destination",destination);
        model.addAttribute("date", date);
        model.addAttribute("clString", clString);
        model.addAttribute("trainSeats", seats);
        model.addAttribute("title", "Book tickets");


        return "user/show_trains";

    }
    @RequestMapping("/process-booking/{train_id}")
    public String processbooking(@PathVariable("train_id") Long train_id,HttpSession session,Principal principal,Model model){

      
        
        List<TrainData> data=(ArrayList) (session.getAttribute("train"));

        boolean traind_id_present=false;
        for(TrainData t:data){
            if(train_id==t.getTrain_data_id())
            traind_id_present=true;
        }
    
        if(traind_id_present){

             TrainData trainData=trainDataRepsitory.findById(train_id).get();

            

             String username=principal.getName();
            Optional<User> user =userRepository.findUserByEmail(username);

            Integer seats=(Integer)session.getAttribute("seats");
            String seat_class=(String)session.getAttribute("class");

            int ticketprice=0;

            switch (seat_class) {
                case "SL":
                    ticketprice=trainData.getPrice_SL();
                    break;
                
                case "2AC":
                    ticketprice=trainData.getPrice_AC2();
                    break;
                
                case "3AC":
                    ticketprice=trainData.getPrice_AC3();
                    break;
                
                case "CC":
                    ticketprice=trainData.getPrice_CC();
                    break;
            
                default:
                    break;
            }
            

            
            Train train=new Train(train_id, trainData.getTrain_no(), trainData.getTrain_name(), trainData.getStart(), trainData.getDestination()
                                , trainData.getDate(), ticketprice*seats,seats,seat_class,user.get());
            
           user.get().getTrain().add(train);
            userRepository.save(user.get());

             Integer totseats=0;
            switch (seat_class) {
                case "SL":
                     totseats=trainData.getSL();
                    trainDataRepsitory.reduceSL(totseats-seats, train_id);
                    break;
                case "3AC":
                     totseats=trainData.getAC3();
                    trainDataRepsitory.reduceAC3(totseats-seats, train_id);
                    break;
                
                case "2AC":
                     totseats=trainData.getAC2();
                    trainDataRepsitory.reduceAC2(totseats-seats, train_id);
                    break;
                
                case "CC":
                     totseats=trainData.getCC();
                    trainDataRepsitory.reduceCC(totseats-seats, train_id);
                    break;
            
                default:
                    break;
            }

            
            
            model.addAttribute("Message","booked");
            model.addAttribute("title", "Book tickets");

            return "user/show_trains";


        }
        else
        {

           return "redirect:user/user_home";

        }
    
    
        
        
    }


    @RequestMapping("/view-tickets")
    public String viewtickets(Principal principal,Model model){

        String username=principal.getName();
        User user =userRepository.findUserByEmail(username).get();
        List<Train> train   = trainRepository.findAllByUser(user);
        model.addAttribute("train", train);
        model.addAttribute("title", "View tickets");
        return "user/view_tickets";
    
    }
     @RequestMapping("/view-tickets/filter")
    public String viewticketsPost(@ModelAttribute("pnr") String pnr,Principal principal,Model model){

        try{
            
            for(char c:pnr.toCharArray()){
                if(Character.isAlphabetic(c)){
                    throw new Exception();
                }
            }
        
        Long p=Long.parseLong(pnr);
        
         String username=principal.getName();
        User user =userRepository.findUserByEmail(username).get();
        List<Train> train = user.getTrain();
        Train temp=null;
        for(Train x:train){
            if(x.getTrain_id()==p){
                temp=x;
                break;
            }
        }
        model.addAttribute("title", "View tickets");
        if(train!=null)
        model.addAttribute("train", temp);
        
        else
        throw new Exception();
        return "user/view_tickets";
        }catch(Exception e){
            return "user/view_tickets";
        }
    
    }
    

    @RequestMapping("/view-tickets/{train_id}")
    public String viewticketDetails(@PathVariable("train_id") Long train_id,Principal principal,Model model)
    {
          
        String username=principal.getName();
        User user =userRepository.findUserByEmail(username).get();
         List<Train> train   = trainRepository.findAllByUser(user);
        Train temp=null;
        for(Train x:train){
            if(x.getTrain_id()==train_id){
                temp=x;
                break;
            }
        }
        
        model.addAttribute("user", user);
        model.addAttribute("train", temp);
        model.addAttribute("title", "View tickets");
        return "user/full_details";
        
    }


    @RequestMapping("/cancel-tickets")
    public String cancelTicket(Principal principal,Model model){

       
        String username=principal.getName();
        User user =userRepository.findUserByEmail(username).get();
         List<Train> train   = trainRepository.findAllByUser(user);
        model.addAttribute("train", train);
        model.addAttribute("title", "Cancel tickets");
       

        return"user/cancel";
    }
    @GetMapping("cancel-tickets/filter")
    public String cancelTicketFilter(@ModelAttribute("pnr_cancel") String pnr,Principal principal,Model model){

        try{
            
            for(char c:pnr.toCharArray()){
                if(Character.isAlphabetic(c)){
                    throw new Exception();
                }
            }
        
        Long p=Long.parseLong(pnr);
        
         String username=principal.getName();
        User user =userRepository.findUserByEmail(username).get();
         List<Train> train   = trainRepository.findAllByUser(user);
        Train temp=null;
        for(Train x:train){
            if(x.getTrain_id()==p){
                temp=x;
                break;
            }
        }
        model.addAttribute("title", "Cancel tickets");

        if(train!=null){
        model.addAttribute("train", temp);
        
        }
        else
        throw new Exception();
        return "user/cancel";
        }catch(Exception e){
            return "user/cancel";
        }


    }
    @GetMapping("cancel-tickets/process/{train_id}")
    public String cancelTicketPost(@PathVariable("train_id") Long train_id ,Principal principal,Model model){
        
        String username=principal.getName();
        User user =userRepository.findUserByEmail(username).get();
         List<Train> train   = trainRepository.findAllByUser(user);
        
        Train temp=null;

        for(Train x:train){
            if(x.getTrain_id()==train_id){
            temp=x;
            break;
            }
        }

        TrainData trainData=trainDataRepsitory.findByTrain_Name(temp.getTrain_name()).get();
        System.out.println(trainData.getTrain_no()+"efesoinfofse");
        Integer seats=temp.getPassengers();
       user.getTrain().remove(temp);
       temp.setUser(null);
       
       
        Integer totseats=0;
        String seat_class=temp.getSeat_class();
        switch (seat_class) {
                case "SL":
                     totseats=trainData.getSL();
                    trainDataRepsitory.addSL(totseats+seats, trainData.getTrain_name());
                    break;
                case "3AC":
                     totseats=trainData.getAC3();
                    trainDataRepsitory.addAC3(totseats+seats, trainData.getTrain_name());
                    break;
                
                case "2AC":
                     totseats=trainData.getAC2();
                    trainDataRepsitory.addAC2(totseats+seats, trainData.getTrain_name());
                    break;
                
                case "CC":
                     totseats=trainData.getCC();
                    trainDataRepsitory.addCC(totseats+seats, trainData.getTrain_name());
                    break;
            
                default:
                    break;
            }

        trainRepository.delete(temp);
        
        train   = trainRepository.findAllByUser(user);

        model.addAttribute("train", train);
        model.addAttribute("Message", "Deleted");
        model.addAttribute("title", "Cancel tickets");

        return "redirect:/user/cancel-tickets";
        
    }
    
}
