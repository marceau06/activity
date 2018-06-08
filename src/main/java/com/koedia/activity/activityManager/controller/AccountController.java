package com.koedia.activity.activityManager.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.koedia.activity.activityManager.model.entity.Activity;
import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.service.ActivityService;
import com.koedia.activity.activityManager.service.UserService;
import com.koedia.common.tool.StringUtil;
import com.koedia.common.tool.email.SimpleEmailSender;

@Controller
@Scope("session")
@RequestMapping("/account")
public class AccountController {
	
	protected final static Logger logger = Logger.getLogger(AccountController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	HttpSession httpSession;
	
	/********************************** Redirection par défaut vers espace perso **********************************/
	
	@GetMapping
	public ModelAndView goToAccount(){
		
		// When login success
		ModelAndView mav = new ModelAndView("account");

		// Store user informations in session object
		initDataStoreSessionObject();

		// Retrieve user
		User user = (User)httpSession.getAttribute("user");
		
		logger.info("[AccountController] - goToAccount() : Loggin success : Redirect to Account for id's user n°" + httpSession.getAttribute("id"));
		
		// Retrieve activities created by user
		List<Activity> allActivitiesForUser = activityService.findAllByUserId((Integer)httpSession.getAttribute("userId"));
		
		// Add users informations in account view
		mav.addObject("activitiesList", allActivitiesForUser);
		mav.addObject("numberActivities", allActivitiesForUser.size());
		mav.addObject("userId", user.getId());
		mav.addObject("email", user.getEmail());
		mav.addObject("address", user.getAddress());
		mav.addObject("websiteAddress", user.getWebsiteAddress());
		mav.addObject("phone", user.getPhone());
		
		// TODO Add all activities in the model to update them
		for(int i= 0; i < allActivitiesForUser.size(); i++) {
			mav.addObject(allActivitiesForUser.get(i));
		}
		
		return mav;
	}
	

	/********************************** Redirection vers modification du compte **********************************/

	@GetMapping("/update")
	public ModelAndView goToUpdateAccountUser() {
		
		ModelAndView mav = new ModelAndView("account-update");
		mav.addObject("user", httpSession.getAttribute("user"));

		return mav;
	}

	
	/********************************** Modifier infos d'un membre **********************************/

	@PostMapping("/update")
	@ModelAttribute("user")
	public ModelAndView updateAccountUser(@ModelAttribute("user") User userUpdated) {
		
		String updateInfoMessage = "La mise à jour a bien été prise en compte";
		
		ModelAndView mav = new ModelAndView("account-update");
		
		if(userUpdated != null) {
			userUpdated.setId((Integer)httpSession.getAttribute("userId"));
			
			try {
				// TODO récupérer le mot de passe avant enregistrement Retrieve encrypted user password
				userUpdated.setPassword("azerty");
				// Save updated user
				userService.saveUser(userUpdated);
			} catch(Exception ex) {
				updateInfoMessage = "Erreur lors de la mise à jour des informations";
				logger.debug("[AccountController]-updateAccountUser() : ERREUR Lors de la mise à jour des informations de l'utilisateur : \n" +ex.getMessage());
			}
			httpSession.setAttribute("user", userUpdated);
			
			mav.addObject("userInfos", userUpdated);
			mav.addObject("userId", userUpdated.getId());
			mav.addObject("updateInfoMessage", updateInfoMessage);
		}
		
		return mav;
	}
	/********************************** Envoie d'un email **********************************/

	@PostMapping("/send-link-password")
	public ModelAndView sendMailAndGoToAccount(@RequestParam(value="email",required=false) String email) {
		
		ModelAndView mav = new ModelAndView("home");
		System.out.println(email);
		logger.info("[AccountController]-sendMailAndGoToAccount : Email du destinataire (Utilisateur en demande de changement de mot de passe) : " + email); 
		
		String errorMessage = "";
		// TODO change port number
		String retrievePasswordUri = "http://localhost:8080/account/reset-password";
		String parameterEmailUser = "?email=";
		
		
		if(StringUtil.isPresent(email)) {
			String retrievePasswordLink = retrievePasswordUri + parameterEmailUser + email ;
			String recipient = email;
			String subject = "TEST ENVOIE EMAIL DE RECUPERATION DE MOT DE PASSE";
			
			String body = "TEST "
					+ "\n"
					+ "Réception email de modification de mot de passe "
					+ "\n"
					+ "Cliquer sur le lien suivant : "
					+ "\n"
					+ retrievePasswordLink;
			
			String sender = "ml@koedia.com";
			String protocolServer = "smtp.koedia.com";
			
			try {
				SimpleEmailSender.send(protocolServer, recipient, sender, subject, body);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "Erreur lors de l'envoie du mail"
						+ "Message : \n"
						+ e.getMessage()
						+"\n"
						+ "Cause : \n"
						+"\n"
						+ e.getCause();
			}
		} else {
			errorMessage = "Votre adresse email est invalide ou probleme de récupération par le controleur";
			mav.addObject("message d'erreur envoie du mail", errorMessage);
			mav.setViewName("forgot-password");
		}

		return mav;
	}
	
	/***************************** Réinitialisation du mot de passe ***************************/
	
	// Redirection depuis lien email de réinitialisation
	@GetMapping("/reset-password")
	public ModelAndView goToResetPassword(@RequestParam("email") String emailUser){
		
		
		if(StringUtil.isPresent(emailUser)) {
			logger.info("[AccountController]-goToResetPassword : "
					+ "Email de l'utilisateur = " + emailUser);
			
			httpSession.setAttribute("email", emailUser);
			
			// Récupérer l'utilisateur en DB
			User user = userService.findUserByEmail(emailUser);
			
			if(user != null) {
				httpSession.setAttribute("user", user);
			} else {
				logger.info("[AccountController]-goToResetPassword : "
						+ "Email de l'utilisateur non récupéré");
				emailUser = "Email inconnu";
			}
			
		} else {
			logger.info("[AccountController]-goToResetPassword : "
					+ "Email de l'utilisateur non récupéré");
		}
		
		ModelAndView mav = new ModelAndView("reset-password");
		mav.addObject("emailUser", emailUser);
		return mav;
	}
	
	
	@PostMapping("/reset-password")
	public ModelAndView resetPassword(@RequestParam("newPassword") String newPassword){
		
		ModelAndView mav = new ModelAndView("home");
		String emailUser = "";
		
		if(StringUtil.isPresent(newPassword)) {
			if(StringUtil.isPresent((String) httpSession.getAttribute("email"))) {
				emailUser = (String) httpSession.getAttribute("email");
			} else {
				// TODO
				// Impossible de récupérer l'utilisateur en DB (dataStore=null)
			}
					
			// Récupérer utilisateur en DB
			User userToUpdate = userService.findUserByEmail(emailUser);
			
			if(userToUpdate != null) {
				userToUpdate.setPassword(newPassword);
				try {
					// Mettre à jour le mot de passe
					userService.saveUser(userToUpdate);
				} catch(Exception ex) {
					logger.debug("[AccountController]-goToResetPassword : ERREUR Lors de la mise à jour des informations de l'utilisateur : \n" +ex.getMessage());
				}
				httpSession.setAttribute("user", userToUpdate);
			}
			mav.addObject("userInfos", httpSession.getAttribute("user"));
			mav.addObject("userId", httpSession.getAttribute("id"));
			mav.addObject("password", null);
		}
		return mav;
	}
	
	private void initDataStoreSessionObject() {
		
		// Retrieve user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		

		if(user != null) {
			// Save user's information in Datastore session object
			httpSession.setAttribute("user", user);
			httpSession.setAttribute("userId", user.getId());
			httpSession.setAttribute("email", user.getEmail());
			httpSession.setAttribute("address", user.getAddress());
			httpSession.setAttribute("websiteAddress", user.getWebsiteAddress());
			httpSession.setAttribute("phone", user.getPhone());
			httpSession.setAttribute("active", user.getActive());
			httpSession.setAttribute("birthdate", user.getBirthdate());
			httpSession.setAttribute("city", user.getCity());
			httpSession.setAttribute("country", user.getCountry());
			httpSession.setAttribute("firstname", user.getFirstname());
			httpSession.setAttribute("lastname", user.getLastname());
		} else {
			// TODO Default user if Db access failed
			user = userService.findUserById(0);
		}
	}
	
}
