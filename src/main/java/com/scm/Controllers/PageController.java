package com.scm.Controllers;
import com.scm.Entity.User;
import com.scm.Forms.*;
import com.scm.Helper.Message;
import com.scm.Helper.MessageType;
import com.scm.Service.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class PageController {

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private  UserServices userServices ;

    PageController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("name", "Neeraj Soni");
        model.addAttribute("Email", "sonineeraj0405@gamil.com");
        model.addAttribute("githubRepo", "https://github.com/Neerajsoni04/WeatherPro/");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        return "about";
    }
    @GetMapping("/services")
    public String services(Model model){
        return "services";
    }
    @GetMapping("/contact")
    public String contact(Model model){
        return "contact";
    }
    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model){
        UserForm userForm = new UserForm();
        // userForm.setName("Neeraj Soni");
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // processing registration
    
    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult, HttpSession session){
        System.out.println("Processing Registration");
        // fetch form data
        System.out.println(userForm);
        // validate data
        if (rBindingResult.hasErrors()) {
            return "register";
        }
        // save to database 
        

        User user = modelMapper.map(userForm, User.class);

        User savedUser = userServices.saveUser(user);
        System.out.println("User Saved!!!");
        Message message = Message.builder()
                            .content("Registration Successfull!!!")
                            .type(MessageType.green).build();
        
        session.setAttribute("message", message); // here we are passing object
        // redirect login page
        return "redirect:/register";
    }  
}
