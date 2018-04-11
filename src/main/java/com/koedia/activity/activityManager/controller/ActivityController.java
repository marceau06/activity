package com.koedia.activity.activityManager.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.rmi.CORBA.UtilDelegate;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.koedia.activity.activityManager.model.entity.Activity;
import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.service.ActivityService;
import com.koedia.common.tool.StringUtil;


@Controller
@ComponentScan
@RequestMapping("activity")
public class ActivityController {

	private static String UPLOADED_FOLDER_ABSOLUTE= "/home/user/workspace_web_escurity/activityManager/src/main/resources/static/img/stored/";
	
	private static String UPLOAD_FOLDER_RELATIVE= "../img/stored/";
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	HttpSession httpSession;
	
	protected final static Logger logger = Logger.getLogger(ActivityController.class);
	
	/********************************** Redirection vers test **********************************/
	
	@GetMapping("/testForm")
	public ModelAndView goToTest(Activity activity) {
		ModelAndView mav = new ModelAndView("test");
		return mav;
	}
	
	
	/********************************** Redirection vers création d'activité **********************************/
	
	@GetMapping("/create")
	public ModelAndView goToCreationActivity(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-creation");
		return mav;
	}
	
	
	/********************************** Redirection vers modification d'activité *******************************/
	
	@GetMapping("/modification")
	public ModelAndView goToUpdateActivity(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-update");
		return mav;
	}
	
	/********************************** Redirection vers liste des activités **********************************/
	
	@GetMapping("/list")
	public ModelAndView goToListActivity(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-list");
		
		// Récupérer les activités créées par l'utilisateur
		List<Activity> allActivitiesForUser = activityService.findAllByUserId(8);
		
		mav.addObject("activitiesList", allActivitiesForUser);
		
		System.out.println(allActivitiesForUser.size());
		
		return mav;
	}
	
	
	/********************************** Créer une nouvelle activité *******************************************/

	@PostMapping("/create")
	public ModelAndView createActivity(Activity activity) {
		
		String creationInfoMessage = "L'activité a bien été créée";
		// TODO
		saveImage(activity);
		
		// Redirection vers page aperçu activité
		ModelAndView mav = new ModelAndView("activities-overview");
		
		// Traitement & enregistrement en DB
		if (activity != null) {
			activity.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
			try{
				activityService.saveActivity(activity);
			} catch(Exception ex) {
				creationInfoMessage = "Erreur lors de la création de l'activité";
				logger.debug("ERREUR Lors de la mise à jour des informations de l'utilisateur : \n" +ex.getMessage());
			}
		}
		mav.addObject("creationInfoMessage", creationInfoMessage);
		mav.addObject("activityCreated", activity);
		return mav;
	}

	
	/********************************** Mettre à jour une activité ***********************************************/

	@PostMapping("update")
	@ModelAttribute("activity")
	public ModelAndView updateActivity(@ModelAttribute("activity") Activity activity) {
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("activities-overview");
		
		// Traitement & enregistrement en DB
		if(activity != null) {
			activityService.saveActivity(activity);
		}
		return mav;
	}
	
	
	/********************************** Activer / Désactiver une activité ****************************************/
	
	@PostMapping("toggle")
	public ModelAndView toggleActivity(@RequestParam String activityId) {	
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("redirect:/account");
		
		if(StringUtil.isPresent(activityId)) {
		
			// Récupérer l'activité à activer/désactiver
			Activity activityToUpdate = activityService.findById(Integer.valueOf(activityId));
			
			if(activityToUpdate != null) {
				
				// Modifier la colonne "active" pour activer/desactiver l'activite
				activityToUpdate.setActive(!activityToUpdate.isActive());
	
				// Traitement & enregistrement en DB
				activityService.saveActivity(activityToUpdate);
			}
			// Retrieve user's informations
			retrieveUserInfos(mav);
		}
		
		return mav;
	}
	
	/********************************** Supprimer une activité **********************************************************/
	@PostMapping("remove")
	public ModelAndView removeActivity(@RequestParam String activityId) {
		
		if(StringUtil.isPresent(activityId)) {
				// Traitement & enregistrement en DB
				activityService.deleteById(Integer.valueOf(activityId));
			}
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		// Retrieve user's informations
		retrieveUserInfos(mav);
		
		return mav;
	}
	
	
	/********************************** Modifier une activité **********************************************************/
	@GetMapping("edit")
	public ModelAndView editActivity(@RequestParam String activityId) {
		
		if(StringUtil.isPresent(activityId)) {
				// Mise a jour de l'activité
			}
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		return mav;
	}
	
	/********************************** Méthodes utiles **********************************************************/

	private void retrieveUserInfos(ModelAndView mav) {
		// Retrieve user
		User user = (User)httpSession.getAttribute("user");
		
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
	}
	
	
	private void saveImage(Activity activity) {
		
		String picFileEndName = "mainpic" + Math.round(Math.random()*101);
		
		Integer userId = (Integer)httpSession.getAttribute("userId");
		
		if (userId != null) {
			activity.setUserId(userId);
			picFileEndName += "_user" + userId.toString();
		}
		
		String absolutePicturePath = UPLOADED_FOLDER_ABSOLUTE + picFileEndName + ".jpg";
		String relativePicturePath = UPLOAD_FOLDER_RELATIVE + picFileEndName + ".jpg";

		MultipartFile mainPictureFile = activity.getMainPictureFile();
				 
		if (mainPictureFile != null && mainPictureFile.getSize() > 0) {
			
	        if (!mainPictureFile.getOriginalFilename().equals("")) {
	        	activity.setMainPicture(relativePicturePath);
	            try {
					 mainPictureFile.transferTo(new File(absolutePicturePath));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		}

	}
	
}

