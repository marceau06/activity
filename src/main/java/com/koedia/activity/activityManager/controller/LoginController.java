package com.koedia.activity.activityManager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	// Go to Home page
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public ModelAndView goToHome(Model m) {
    	ModelAndView mav = new ModelAndView("home");
    	return mav;
    }
    
	// Go to login page
    @GetMapping("/login")
    public ModelAndView goToLogin(Model m) {
    	ModelAndView mav = new ModelAndView("home");
    	return mav;
    }
    
    // Go to registration page
	@GetMapping("/registration")
	public ModelAndView registration(){
	
		ModelAndView modelAndView = new ModelAndView("registration");
		User user = new User();
		modelAndView.addObject("user", user);
		
		return modelAndView;
	}
	
	// Submit registration form
	@PostMapping("/registration")
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
		
		ModelAndView mav = new ModelAndView("registration");
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			// TODO Redirect to home with error message parameter
		} else {
			mav.setViewName("home");
			userService.saveUser(user);
			mav.addObject("succesLogin", "User has been registered successfully");
			mav.addObject("user", new User());
		}
		return mav;
	}
	
	// Go to admin home page
	@GetMapping("/admin/home")
	public ModelAndView goToAdminHome(){
		
		ModelAndView modelAndView = new ModelAndView("admin/home");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		
		modelAndView.addObject("userName", "Welcome " + user.getFirstname() + " " + user.getLastname() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		
		return modelAndView;
	}
	
	/***************************** Oublie du mot de passe ***************************/
	
	@GetMapping("/forgot-password")
	public ModelAndView goToForgotPassword(){
		ModelAndView modelAndView = new ModelAndView("forgot-password");
		return modelAndView;
	}
    
	/***************************** FAQ ***************************/

	@GetMapping("/faq")
	public ModelAndView goToFaq(){
		ModelAndView modelAndView = new ModelAndView("faq");
		return modelAndView;
	}
	
}
