package com.koedia.activity.activityManager.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.koedia.activity.activityManager.model.entity.Activity;
import com.koedia.activity.activityManager.model.entity.Schedule;
import com.koedia.activity.activityManager.model.entity.Session;
import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.service.ActivityService;
import com.koedia.common.tool.StringUtil;


@Controller
@ComponentScan
@RequestMapping("activity")
public class ActivityController {

	private static String UPLOADED_FOLDER_ABSOLUTE= "/home/user/workspace_web_escurity/activityManager/src/main/resources/static/img/stored/";
	
	private static String UPLOAD_FOLDER_RELATIVE= "../img/stored/";
	
	private final static Map<String, Integer> inputStepNumber = new HashMap<String, Integer>();

	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	HttpSession httpSession;
	
	protected final static Logger logger = Logger.getLogger(ActivityController.class);
	
	
	/********************************** Init method ******************************/
	@PostConstruct
	private static void init() {
	   inputStepNumber.put("title", 0);
	   inputStepNumber.put("category", 1);
	   inputStepNumber.put("description", 2);
	   inputStepNumber.put("meeting_point", 3);
	   inputStepNumber.put("schedule", 4);
	   inputStepNumber.put("images", 5);
	   inputStepNumber.put("participation_criterias", 6);
	   inputStepNumber.put("price", 7);
	   inputStepNumber.put("pax_type", 8);
	   inputStepNumber.put("group_capacity", 9);
	}
	
	/********************************** Redirection to activity creation form ******************************/
	
	@GetMapping("/create")
	public ModelAndView goToCreationActivity(Activity activity) {
		
		// Initialiser la liste des horaires en créant un horaire pour chacun des jours de la semaine
		List<Schedule> usualSchedules = new ArrayList<Schedule>();
		for (int i = 0; i < 7; i++) {
			Schedule s = new Schedule(i);
			for (int j = 0; j < 9; j++) {
				s.addSession(new Session(j));
			}
			usualSchedules.add(s);
		}
		activity.setUsualSchedules(usualSchedules);
		
		ModelAndView mav = new ModelAndView("activities-creation");

		return mav;
	}
	
	
	/********************************** Redirection to activity update form *******************************/
	
	@GetMapping("/modification")
	public ModelAndView goToUpdateActivity(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-update");
		return mav;
	}
	
	/********************************** Redirection activities list ***************************************/
	
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
	public ModelAndView createActivity(@ModelAttribute("activity") @Valid Activity activity, BindingResult bindingResult) {
		
		// Redirection vers page aperçu activité
		ModelAndView mav = new ModelAndView();
				
		// 
		int nextTabToShowNumber = 0;
		
		String creationInfoMessage = "L'activité a bien été créée";
		
		// If we are validating inputs one by one
		if (StringUtil.isPresent(activity.getFieldToValidate())) {
			
			// Redirect to form creation page
			// TODO change it to activities-creation
			mav.setViewName("test");
			
			// Instanciate validator interface
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			
			// constraintViolations contains all binding errors for property's name in fiedlToValidate
			Set<ConstraintViolation<Activity>> constraintViolations = validator.validateProperty(activity, activity.getFieldToValidate());
			
			if (constraintViolations.size() > 0) {
				System.out.println("Impossible de valider les donnees du bean");
				// Go to the next form step
				nextTabToShowNumber = inputStepNumber.get(activity.getFieldToValidate());
				System.out.println("nextTab invalid = " + nextTabToShowNumber);
			} else {
				System.out.println("Les donnees du bean sont validees");
				// Go to the current form step
				nextTabToShowNumber = inputStepNumber.get(activity.getFieldToValidate()) + 1;
				System.out.println("nextTab valid = " + nextTabToShowNumber);
			}
		// We validate the entire bean with all properties
		} else { 
				// Redirect to description page
				mav.setViewName("activities-overview");
				
				// Sauvegarde des images
				saveImage(activity);
							
				// Convert date and hours in the right format (java.sql.Time)
				if (StringUtil.isPresent(activity.getBeginHourText()) && StringUtil.isPresent(activity.getEndHourText())) {
					String beginHour = activity.getBeginHourText() + ":00";
					String endHour = activity.getEndHourText() + ":00";
					activity.setBeginHour(Time.valueOf(beginHour));
					activity.setEndHour(Time.valueOf(endHour));
				}
				activity.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
				
				try{
					activityService.saveActivity(activity);
				} catch(Exception ex) {
					creationInfoMessage = "Erreur lors de la création de l'activité";
					logger.debug("ERREUR Lors de la mise à jour des informations de l'utilisateur : \n" +ex.getMessage());
				}
			}
		
		
//		if (bindingResult.hasErrors()) {
//			System.out.println("error formulaire");
//		}

		mav.addObject("creationInfoMessage", creationInfoMessage);
		mav.addObject("activity", activity);
		mav.addObject("stepNumber", nextTabToShowNumber);
		return mav;
	}

	
	/********************************** Mettre à jour une activité ***********************************************/

	@PostMapping("update")
	@ModelAttribute("activity")
	public ModelAndView updateActivity(@ModelAttribute("activity") Activity activity) {
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("activities-overview");
		
		// Traitement & enregistrement en DB
		if (activity != null) {
			activityService.saveActivity(activity);
		}
		return mav;
	}
	
	
	/********************************** Activer / Désactiver une activité ****************************************/
	
	@PostMapping("toggle")
	public ModelAndView toggleActivity(@RequestParam String activityId) {	
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("redirect:/account");
		
		if (StringUtil.isPresent(activityId)) {
		
			// Récupérer l'activité à activer/désactiver
			Activity activityToUpdate = activityService.findById(Integer.valueOf(activityId));
			
			if (activityToUpdate != null) {
				
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
		
		if (StringUtil.isPresent(activityId)) {
				// Traitement & enregistrement en DB
				activityService.deleteById(Integer.valueOf(activityId));
			}
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		// Retrieve user's informations
		retrieveUserInfos(mav);
		
		return mav;
	}
	
	@GetMapping("/remove")
	public ModelAndView returnToAccountAfterRemove(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-update");
		return mav;
	}
	
	
	/********************************** Exporter une activité **********************************************************/
	@PostMapping("export")
	public ModelAndView exportActivity(@RequestParam String activityId) {
		
	 String CSV_SEPARATOR = ",";
	 
	 if (StringUtil.isPresent(activityId)) {
			
			// Récupérer l'activité à activer/désactiver
			Activity activityToexport = activityService.findById(Integer.valueOf(activityId));

			 try
				{
				    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("activity.csv"), "UTF-8"));
		
				    StringBuffer oneLine = new StringBuffer();
				    oneLine.append(activityToexport.getId() <=0 ? "" : activityToexport.getId());
				    oneLine.append(CSV_SEPARATOR);
				    oneLine.append(activityToexport.getTitle().trim().length() == 0? "" : activityToexport.getTitle());
				    oneLine.append(CSV_SEPARATOR);
		            bw.write(oneLine.toString());
				    bw.newLine();
				    bw.flush();
				    bw.close();
				}
			 	catch (IOException e){}
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
		
		if (StringUtil.isPresent(activityId)) {
				// Mise a jour de l'activité
			}
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		return mav;
	}
	
	/********************************** Page de TEST **************************************************************/
	@GetMapping("test")
	private ModelAndView timepickerExample(Activity activity, Integer stepNumber) {
	
		// Initialiser la liste des horaires en créant un horaire pour chacun des jours de la semaine
		List<Schedule> usualSchedules = new ArrayList<Schedule>();
		for (int i = 0; i < 7; i++) {
			Schedule s = new Schedule(i);
			for (int j = 0; j < 9; j++) {
				s.addSession(new Session(j));
			}
			usualSchedules.add(s);
		}
		activity.setUsualSchedules(usualSchedules);
		
		stepNumber = 0;
		
		ModelAndView mav = new ModelAndView("test");
		mav.addObject("stepNumber", stepNumber);
		return mav;
	}
	
	@PostMapping("/testTitle")
	public ModelAndView testTitle(@ModelAttribute("title") @Valid final String title, final BindingResult bindingResult) {
		
		String t = title;
		System.out.println(title);
		
		// Redirection vers page aperçu activité
		ModelAndView mav = new ModelAndView("account");
		
		if (bindingResult.hasErrors()) {
			System.out.println("error formulaire");
		}
		
		return mav;
	}
	
	@PostMapping("/testCategory")
	public ModelAndView testCategory(@ModelAttribute("category") @Valid final String category, final BindingResult bindingResult) {
		
		String t = category;
		System.out.println(category);
		
		// Redirection vers page aperçu activité
		ModelAndView mav = new ModelAndView("account");
		
		if (bindingResult.hasErrors()) {
			System.out.println("error formulaire");
		}
		
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
		for (int i= 0; i < allActivitiesForUser.size(); i++) {
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

